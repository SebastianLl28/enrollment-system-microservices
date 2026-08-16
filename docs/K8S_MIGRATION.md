# Migración a Kubernetes

> Spec de trabajo. Docker Compose (`docker-compose.yml` + `docker-compose.override.yml`) sigue
> siendo el flujo de desarrollo local y **no se toca** en ningún paso de este documento — todo lo
> nuevo vive bajo `deploy/k8s/` y detrás de un perfil Spring `k8s` que Compose nunca activa.
> Objetivo final: mismo stack corriendo en minikube (local) y en un cluster cloud, sin Eureka.

## 0. Estado actual (ya implementado)

Base de código lista para correr con perfil `k8s`, verificado con Compose intacto (`mvn clean
verify` en los 8 módulos, `BUILD SUCCESS`, cero tests rotos):

- [x] **`api-gateway`**: rutas y `AuthFilter` ya no hardcodean `lb://`. Vienen de
      `services.authorization.uri` / `services.enrollment.uri`
      (`api-gateway/src/main/resources/application.yaml`), con default `lb://<servicio>`
      (Compose) y override en `application-k8s.yaml` a `http://<servicio>:8080` (DNS de
      Kubernetes). `WebClientConfig` tiene dos beans `webClientBuilder` por perfil: con
      `@LoadBalanced` en `!k8s`, plano en `k8s` — con `@LoadBalanced` activo y un URI HTTP plano,
      el filtro interpretaría el host como serviceId de Eureka y devolvería 503.
- [x] **Perfil `k8s` con `eureka.client.enabled: false`** en `api-gateway`, `authorization-server`,
      `enrollment-server`, `event-store-server` (`application-k8s.{yaml,yml}` en cada módulo).
      `notification-server` no tiene cliente Eureka, no necesita el perfil.
      `discovery-server` no se despliega en k8s (nada lo necesita sin Eureka activo).
- [x] **ShedLock** en `enrollment-server` (`OutboxProcessor`, los dos `@Scheduled`): prerequisito
      para correr más de una réplica sin publicar eventos duplicados en Kafka. Es el único
      `@Scheduled` de todo el repo (verificado por grep) — ningún otro servicio necesita esto.
- [x] **Flyway** en `enrollment-server`, `authorization-server`, `event-store-server`
      (`ddl-auto: update` → `validate` + `V1__baseline.sql` por servicio, `baseline-on-migrate`).
      Prerequisito para correr más de una réplica: `ddl-auto: update` no tiene lock, dos pods
      arrancando a la vez pueden chocar con `relation already exists`.
- [x] **JVM en contenedor**: los 6 Dockerfiles de servicios Java ya traen
      `-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0` en el `ENTRYPOINT`. Nada que hacer acá.
- [x] CI (`build-and-push.yml`) ya publica con `type=sha` (tag inmutable) además de `latest` —
      sirve tal cual para referenciar imágenes desde los manifiestos de k8s.

## 1. Decisiones a confirmar antes de escribir manifiestos

Bloquean el resto del trabajo — conviene resolverlas primero.

- [ ] **Kustomize vs Helm.** Recomendado: Kustomize (`base/` + `overlays/{minikube,cloud}`) — sin
      lenguaje de plantillas, `kubectl apply -k` directo, encaja con "mismo stack, dos entornos".
      Helm solo si se quiere publicar un chart reusable por terceros.
- [ ] **Kafka en k8s.** Recomendado: pasar a **KRaft** (sin Zookeeper — `cp-kafka:7.5.0` ya lo
      soporta) + **Strimzi** operator en minikube; gestionado (MSK / Confluent Cloud) en cloud.
      Alternativa manual (StatefulSet propio) no se recomienda: alto mantenimiento.
- [ ] **Postgres en k8s.** Minikube: StatefulSet + PVC (3 bases, una por servicio, como hoy).
      Cloud: gestionado (RDS / Cloud SQL) — correr Postgres sin operador en producción no vale la
      pena. Definir si el overlay `cloud` apunta a un host externo (`DB_HOST` externo, sin
      Postgres en el cluster) o si igual se corre en cluster con un operador (CloudNativePG).
- [ ] **Secretos versionados.** Sealed Secrets vs SOPS. Cualquiera de los dos sirve; definir cuál
      antes de escribir los `Secret` para no rehacer el trabajo.
- [ ] **TLS.** cert-manager + Let's Encrypt en cloud (dominio público ya existe:
      `course-hub.dev`, ver `frontend/nginx.conf`). En minikube: sin TLS o con `minikube tunnel` +
      cert self-signed, según si se necesita probar el flujo HTTPS completo (Mercado Pago webhook
      exige HTTPS público — ver §7).
- [ ] **Observabilidad.** ¿Se reusa el stack actual (Prometheus/Grafana/Loki/Promtail, cada uno
      su propio Deployment) o se migra a `kube-prometheus-stack` + chart de Promtail? Recomendado
      lo segundo — Promtail vía `docker.sock` (como está hoy) no aplica en k8s, necesita
      `kubernetes_sd_configs` de todos modos, así que el rewrite es obligatorio en cualquier caso.

## 2. Estructura de manifiestos

```
deploy/
  k8s/
    base/                        # kustomize
      configmap.yaml
      secrets.yaml                # placeholders, NUNCA valores reales committeados
      frontend-client/            deployment + service
      api-gateway/                deployment + service
      authorization-server/       deployment + service
      enrollment-server/          deployment + service
      notification-server/        deployment + service
      event-store-server/         deployment + service
      postgres-enrollment/        statefulset + pvc + service   (solo overlay minikube)
      postgres-auth/              statefulset + pvc + service   (solo overlay minikube)
      postgres-events/            statefulset + pvc + service   (solo overlay minikube)
      ingress.yaml
      kustomization.yaml
    overlays/
      minikube/                   # replicas 1, recursos chicos, storage local, sin TLS
        kustomization.yaml
      cloud/                      # replicas >1, límites reales, TLS, DB/Kafka gestionados
        kustomization.yaml
```

- [ ] Crear el árbol de directorios y el `kustomization.yaml` base (lista de recursos, sin
      patches).
- [ ] Overlay `minikube`: namePrefix/replicas/resources reducidos, Postgres y Kafka en cluster.
- [ ] Overlay `cloud`: replicas ≥2 en los servicios sin estado local (todos menos, ver §5 sobre
      `enrollment-server`), `DB_*_HOST` apuntando a instancias gestionadas, Kafka gestionado,
      TLS vía Ingress.

## 3. Deployment + Service por servicio sin estado

Para cada uno de `frontend-client`, `api-gateway`, `authorization-server`, `enrollment-server`,
`notification-server`, `event-store-server`:

- [ ] `Deployment` con imagen `sllamuca/<servicio>:<tag>` (usar el tag `sha-<commit>` que ya
      publica CI, no `latest`, para que los rollouts sean reproducibles).
- [ ] `env: SPRING_PROFILES_ACTIVE=k8s` (los 4 backend con perfil `k8s`; `frontend-client` y
      `notification-server` no lo necesitan).
- [ ] `envFrom: configMapRef` + `secretRef` (ver §6 para el split).
- [ ] `Service` (`ClusterIP`) con el mismo nombre que usa Compose hoy — así
      `frontend/nginx.conf` (`proxy_pass http://api-gateway:8080`, `http://grafana:3000`, etc.)
      funciona sin editar una línea, siempre que el `Service` se llame igual que el contenedor en
      Compose.
- [ ] Probes de readiness/liveness (ver §8).
- [ ] `resources.requests/limits` (memoria coherente con `-XX:MaxRAMPercentage=75.0` — no poner
      un límite de memoria más chico que lo que el heap puede crecer, o el pod muere por OOM).

## 4. `enrollment-server`: consideración de réplicas

- [ ] Decidir arranque: `replicas: 1` es válido y no requiere nada extra (ShedLock igual protege,
      pero con una sola réplica el lock nunca se disputa).
- [ ] Si se escala a 2+: ya cubierto por ShedLock (§0) — no hay trabajo adicional de código, solo
      confirmar en el manifiesto `replicas: 2` y observar que `OutboxProcessor` sigue publicando
      sin duplicados (los tests `ShedLockConfigIT` ya prueban el lock en aislamiento; falta una
      prueba end-to-end con 2 réplicas reales en el cluster, ver checklist de validación §11).

## 5. Kafka

- [ ] Instalar Strimzi operator en minikube (`kubectl apply -f strimzi-cluster-operator.yaml` o
      su Helm chart).
- [ ] `Kafka` CR en modo KRaft (sin Zookeeper), 1 broker para minikube.
- [ ] Overlay cloud: sin `Kafka` CR — `KAFKA_BOOTSTRAP_SERVERS` apunta al endpoint gestionado
      (MSK/Confluent), con las credenciales correspondientes en el `Secret`.
- [ ] Verificar que los topics (`enrollment-events`, `audit-logs`, y el group id de
      `notification-server`) se crean automáticamente o vía `KafkaTopic` CR — Kafka en modo
      compose los auto-crea por config del broker; confirmar que el operator/servicio gestionado
      tenga el mismo comportamiento o crearlos explícitamente.

## 6. Secrets / ConfigMap — split de variables

Inventario completo de env vars usadas hoy en `docker-compose.yml` (44 variables). Clasificación
propuesta:

**`Secret`** (credenciales, tokens, claves):
`DB_*_PASSWORD` (x3), `JWT_SECRET`, `GITHUB_CLIENT_SECRET`, `GOOGLE_CLIENT_SECRET`,
`MP_ACCESS_TOKEN`, `MP_WEBHOOK_SECRET`, `MAIL_PASSWORD`, `GRAFANA_ADMIN_PASSWORD`.

**`ConfigMap`** (todo lo demás — nombres de host/puerto ya no aplican igual en k8s, ver nota):
`DB_*_NAME`, `DB_*_USER`, `KAFKA_*_TOPIC`, `KAFKA_CONSUMER_GROUP_ID`,
`KAFKA_AUTO_OFFSET_RESET`, `JWT_EXPIRATION`, `TWO_FACTOR_EXPIRATION`, `TWO_FACTOR_ISSUER`,
`FRONTEND_URL`, `GITHUB_REDIRECT_URI`, `GOOGLE_REDIRECT_URI`, `MP_BACK_URLS_BASE`,
`MP_CURRENCY_ID`, `MP_ENROLLMENT_FEE`, `MAIL_FROM`, `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`,
`GRAFANA_ADMIN_USER`, `GRAFANA_SERVER_ROOT_URL`, `PROMETHEUS_EXTERNAL_URL`.

**Ya no aplica en k8s** (Compose las resuelve por nombre de contenedor, k8s por `Service` DNS —
mismo mecanismo, no hace falta variable):
`DB_*_HOST`, `DB_*_PORT` (si Postgres corre en cluster con el mismo nombre de `Service` que el
contenedor hoy, los defaults de `application.yml` ya apuntan bien; solo hace falta sobreescribir
en el overlay `cloud` si la base es externa), `EUREKA_URI` (perfil `k8s` deshabilita Eureka),
`KAFKA_BOOTSTRAP_SERVERS` (apunta al `Service`/CR de Kafka, mismo criterio), `MP_NOTIFICATION_URL`
(pasa a ser la URL pública del Ingress).

- [ ] Elegir mecanismo de versionado del `Secret` (Sealed Secrets o SOPS, definido en §1) e
      instalar el controller/tooling correspondiente.
- [ ] Nunca commitear `Secret` en claro — solo la forma sellada/encriptada.

## 7. Ingress + `frontend/nginx.conf`

`frontend/nginx.conf` está escrito para el deploy actual (bare Docker/Compose detrás de un
dominio propio) y **no sirve tal cual en k8s** en tres puntos:

- [ ] **TLS hardcodeado**: `ssl_certificate /etc/letsencrypt/live/course-hub.dev/...` asume
      certbot corriendo en el mismo host. En k8s, TLS lo termina el `Ingress` (cert-manager monta
      el secret `tls.crt`/`tls.key` que genera, no el layout de certbot). Se necesita una variante
      `nginx-k8s.conf` que **no** tenga el bloque `listen 443 ssl` — el `Ingress` hace esa parte,
      el pod de `frontend-client` sirve HTTP plano puertas adentro del cluster.
- [ ] **`/eureka/` proxy_pass a `discovery-server`**: ese servicio no existe en el cluster k8s
      (§0). Quitar el `location /eureka/` de la variante k8s (o dejarlo devolviendo 404).
- [ ] **`/grafana/` y `/prometheus/`**: decidir si siguen proxeados por nginx (requiere que esos
      `Service` existan en el mismo namespace) o pasan a rutas propias del `Ingress` — más
      idiomático en k8s, evita que nginx sea punto único de fallo para observabilidad.
- [ ] El resto (`/api/`, `/webhooks/`, `/auth/`, `/login/oauth2/`, `/swagger-ui*`, `/v3/api-docs/`,
      `/version`) no necesita cambios: son `proxy_pass http://api-gateway:8080`, y el `Service`
      `api-gateway` en k8s responde al mismo nombre.
- [ ] `Ingress` con reglas de host (`www.course-hub.dev` → `frontend-client` Service) + TLS
      (cert-manager `Certificate`/`ClusterIssuer`).

## 8. Probes de salud

- [ ] Agregar `management.endpoint.health.probes.enabled: true` a los `application.yaml` (o solo
      al perfil `k8s`) de los 4 servicios backend — Boot expone automáticamente
      `/actuator/health/readiness` y `/actuator/health/liveness` al detectar que corre en k8s,
      pero el flag lo hace explícito y no depende de la detección automática.
- [ ] `readinessProbe`/`livenessProbe` en cada `Deployment` apuntando a esos paths. Sin
      `readinessProbe`, el `Service` manda tráfico a pods que todavía no terminaron de levantar
      (JPA/Flyway/Kafka producer init) y el usuario ve 502/503 en el rollout.

## 9. Webhook de Mercado Pago

- [ ] Cloud: `MP_NOTIFICATION_URL` = URL pública del Ingress (`https://www.course-hub.dev/webhooks/mercadopago`
      o el path que corresponda) — ya HTTPS por el TLS del Ingress (§7), no requiere nada extra.
- [ ] Minikube: sin dominio público, MP no puede pegarle al webhook directo. Igual que en el flujo
      local actual: túnel (ngrok/cloudflared) apuntando al `Ingress`/`NodePort` si se necesita
      probar el flujo de pago end-to-end en minikube; si no, dejar `MP_ACCESS_TOKEN` vacío
      (fail-soft ya implementado, `enrollment-server` sigue creando la matrícula sin el link de
      pago).

## 10. Observabilidad

- [ ] Si se opta por `kube-prometheus-stack` (recomendado, §1): instalar el chart, borrar del
      overlay minikube los `Deployment` propios de Prometheus/Grafana.
- [ ] Promtail: reemplazar `promtail-config.yml` actual (scrapea logs por socket de Docker) por
      su chart oficial con `kubernetes_sd_configs` — no hay forma de reusar el archivo actual tal
      cual, el mecanismo de descubrimiento es distinto por diseño.
- [ ] Dashboards de Grafana existentes (si los hay versionados) — confirmar si se migran como
      `ConfigMap` con el label que el chart de Grafana espera para auto-importarlos.

## 11. CI/CD (opcional, fuera del alcance mínimo)

- [ ] Job adicional en `.github/workflows/build-and-push.yml` (o uno nuevo) que corra
      `kubectl apply -k deploy/k8s/overlays/cloud` tras el push de imágenes, o migrar a
      GitOps (ArgoCD/Flux) apuntando al mismo directorio. No es necesario para tener el cluster
      funcionando manualmente, pero cierra el ciclo de "cada merge a `master` despliega".

## 12. Checklist de validación

Antes de dar la migración por completa:

- [ ] `minikube start` + `kubectl apply -k deploy/k8s/overlays/minikube` levanta los 6 servicios
      + Postgres x3 + Kafka (Strimzi) sin CrashLoopBackOff.
- [ ] Login OAuth2 (GitHub/Google) funciona a través del Ingress.
- [ ] Flujo completo de matrícula: crear matrícula → outbox → Kafka → notification-server → email
      con link de pago (o sin link si `MP_ACCESS_TOKEN` vacío en minikube, ver §9).
- [ ] Con `enrollment-server` en `replicas: 2`: forzar varios eventos de outbox y confirmar en los
      logs de ambos pods que solo uno de los dos procesa cada lote (ShedLock funcionando en un
      cluster real, no solo en el `ShedLockConfigIT` de Testcontainers).
- [ ] `kubectl rollout restart` de cada servicio backend no genera errores de Flyway/Hibernate
      (`ddl-auto: validate` pasa, ningún pod se cae por baseline).
- [ ] `docker compose up` sigue funcionando exactamente igual que antes de este documento (nada
      en Compose se tocó, pero vale la pena confirmarlo una vez esté todo lo demás en pie).

## 13. Estimación

| Bloque | Esfuerzo |
|---|---|
| §0 (ya hecho) | — |
| §1 decisiones + §2 estructura base | 2–4 h |
| §3 Deployments/Services de los 6 servicios | 3–4 h |
| §5 Kafka (Strimzi + KRaft, minikube) | 2–3 h |
| §6 Secrets/ConfigMap + tooling de versionado | 2–3 h |
| §7 Ingress + variante k8s de nginx.conf | 2–3 h |
| §8 Probes | 1 h |
| §10 Observabilidad (kube-prometheus-stack + Promtail) | 3–4 h |
| §11 CI/CD (opcional) | 2–4 h |
| §12 Validación end-to-end | 2–3 h |
| **Total (sin §11)** | **~17–24 h** |

Postgres/DB gestionada en cloud y TLS de producción (§1, §3, §7 rama `cloud`) no están en esta
estimación — dependen del proveedor elegido y quedan fuera del alcance de "levantarlo en
minikube".
