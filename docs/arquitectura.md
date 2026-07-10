# Arquitectura del sistema

Vista técnica del sistema de inscripciones. Para el listado de funcionalidades por módulo ver [funcionalidades.md](./funcionalidades.md).

## Servicios

Stack: Java 17, Spring Boot 3.2.5, Spring Cloud 2023.0.2, PostgreSQL, Kafka, React 19 + TypeScript + Vite. Todo se orquesta con Docker Compose (`make up`).

| Servicio | Rol | Puerto (dev) |
|---|---|---|
| **discovery-server** | Registro de servicios (Eureka). | 8761 |
| **api-gateway** | Punto de entrada único (Spring Cloud Gateway). Enruta por nombre de servicio (`lb://`), valida JWT y publica auditoría. | 8080 |
| **authorization-server** | Autenticación (credenciales + OAuth2 GitHub/Google), MFA TOTP, emisión/validación de JWT y RBAC. | — |
| **enrollment-server** | Núcleo del negocio: catálogo académico, estudiantes, inscripciones, pagos y dashboard. | — (debug 5005) |
| **notification-server** | Consume eventos de inscripción y envía correos (SMTP/Mailtrap en dev). | — |
| **event-store-server** | Consume y persiste la auditoría de requests. | — |
| **frontend** | SPA React servida por nginx; proxy de `/webhooks` hacia el gateway. | 5173 (dev) |
| **common** | Librería compartida: anotaciones `@UseCase`/`@Adapter`, constantes, enums, `AuditEvent`. | n/a |

## Flujo de una petición

```
Cliente ──> api-gateway (8080)
              │
              ├─ /auth/**      ──> authorization-server (login, registro, RBAC, validateToken)
              │
              ├─ /api/**       ──> enrollment-server
              │     └─ AuthFilter: valida el JWT llamando a /auth/validateToken
              │        e inyecta X-User-Id, X-User-Name y X-User-Permissions;
              │        el PermissionInterceptor del servicio autoriza por RECURSO:OPERACIÓN
              │
              ├─ /webhooks/**  ──> enrollment-server (SIN AuthFilter; firma HMAC de Mercado Pago)
              │
              └─ /v3/api-docs/* ──> agrega Swagger de los servicios (/swagger-ui.html)
```

La autenticación es **centralizada**: solo el gateway valida tokens; los servicios downstream confían en los headers que este inyecta (y los sobreescribe en cada request, de modo que un cliente no puede falsificarlos).

## Seguridad

- **JWT** emitido por el authorization-server tras login (credenciales u OAuth2).
- **MFA (TOTP)** opcional por usuario: activación con QR (`/auth/2fa/init` + `confirm`) y verificación en el login (`/auth/2fa/verify`).
- **RBAC** con permisos `RECURSO:OPERACIÓN:ALCANCE` y *vistas de UI* por rol. Recursos: `STUDENT`, `ENROLLMENT` y `UI_VIEW` (catálogo académico + dashboard + módulos del frontend).
- **Webhooks** de Mercado Pago validados con HMAC (`x-signature`, secreto `MP_WEBHOOK_SECRET`); si el secreto no está configurado se procesa con una advertencia en logs (solo aceptable en dev).

## Eventos (Kafka)

Dos flujos independientes:

1. **Auditoría** — el `AuditGlobalFilter` del gateway publica cada request como `AuditEvent` (definido en `common`) al topic `audit.requests`; el event-store-server lo persiste. Publicación directa, sin outbox.

2. **Eventos de dominio con patrón Outbox** — el enrollment-server escribe los eventos de inscripción en la tabla `outbox_event` **dentro de la misma transacción** que el cambio de dominio; un `OutboxProcessor` programado los publica al topic `enrollment.notifications`, que consume el notification-server para enviar correos. Esto garantiza que no se pierdan eventos si Kafka está caído en el momento del cambio.

## Pagos (Mercado Pago Checkout Pro)

- Las inscripciones nacen `PENDING`; el pago las confirma (`PAID`).
- Al inscribir, se crea una preferencia de Checkout Pro (`external_reference` = id de inscripción, precio del curso en vigencia con fallback `MP_ENROLLMENT_FEE`). El `init_point` viaja en el evento outbox y llega al correo como botón de pago. Si MP falla, la inscripción igual se crea (fail-soft).
- El webhook `POST /webhooks/mercadopago` consulta el pago en la API de MP y aplica `PENDING → PAID` de forma **idempotente** (notificaciones repetidas no tienen efecto). Casos no recuperables (pago inexistente, inscripción no encontrada) responden 200 para cortar reintentos; errores transitorios responden 500 para que MP reintente.
- Variables: `MP_ACCESS_TOKEN`, `MP_WEBHOOK_SECRET`, `MP_NOTIFICATION_URL` (https pública), `MP_BACK_URLS_BASE`, `MP_ENROLLMENT_FEE`, `MP_CURRENCY_ID`. En el panel de MP suscribirse al topic **payment** ("Pagos").

## Persistencia

**Base de datos por servicio** (nunca compartida):

| Contenedor | Servicio | Puerto host (dev) |
|---|---|---|
| `postgres-enrollment` | enrollment-server | 5432 |
| `postgres-auth` | authorization-server | 5433 |
| `postgres-events` | event-store-server | 5434 |

## Arquitectura hexagonal (puertos y adaptadores)

`enrollment-server` y `authorization-server` comparten la misma forma de paquetes:

```
domain/          modelos, value objects, repositorios (interfaces), eventos, excepciones
application/     DTOs (command/query/response), mappers, port/in + port/out,
                 servicios @UseCase (orquestan los casos de uso)
infrastructure/
  adapter/in     controladores REST (dependen de port/in)
  adapter/out    persistencia JPA, mensajería Kafka, Mercado Pago (@Adapter)
```

Reglas: los controladores dependen de `port/in`; los servicios de aplicación de `port/out`; las entidades JPA nunca salen de `infrastructure`. Los agregados de solo lectura (p. ej. el dashboard) usan un puerto de lectura propio (`DashboardStatsPort`) que consulta la persistencia directamente, sin pasar por los repositorios de dominio.

## Frontend

- Estructura por *feature*: `src/features/<feature>/{components,hooks,pages,services,types}` (auth, faculty, career, course, course-offering, term, enrollment, students, profile, rbac, dashboard).
- Configuración transversal en `src/config/`: cliente axios (`apiClient`), `endpoints.ts`, claves de React Query (`keys.ts`), protección de rutas.
- Estado: Zustand (sesión/permisos) + TanStack Query (estado de servidor). Formularios con react-hook-form + Zod. UI con Tailwind 4 + shadcn/Radix. Gráficos con Recharts.
- Las rutas y módulos visibles se filtran por las vistas de UI y permisos del rol (espejo del `PermissionInterceptor` del backend).

## Observabilidad

- Prometheus + Grafana + Loki/Promtail integrados al stack de Compose.
- Cada servicio expone `/actuator/health` y `/actuator/prometheus`.

## Comandos útiles

```bash
make up      # construir y levantar todo (compose + override con puertos de debug)
make down    # detener conservando datos
make reset   # destruir volúmenes (incluye BDs) y reconstruir
make logs    # logs de todos los contenedores
make prod    # solo docker-compose.yml, sin puertos de debug expuestos

mvn clean package                       # compilar todos los módulos
mvn -pl enrollment-server -am package   # un servicio + sus dependencias (common)

cd frontend && npm run dev              # frontend en desarrollo (5173)
```

Requiere un `.env` en la raíz (`cp .env.example .env`; ejemplo completo en el README).
