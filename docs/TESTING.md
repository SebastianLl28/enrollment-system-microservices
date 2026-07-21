# Guía de Tests

## Estructura general

| Convención | Plugin Maven | Cuándo corre | Necesita Docker |
|---|---|---|---|
| `*Test` | Surefire | `mvn test` | No |
| `*IT` | Failsafe | `mvn verify` | Sí (Testcontainers) |

JaCoCo genera cobertura XML en `target/site/jacoco/jacoco.xml` durante `verify` (requerido por SonarQube).

---

## Módulos con tests

| Módulo | Unit (`*Test`) | IT (`*IT`) | JaCoCo |
|---|---|---|---|
| `enrollment-server` | 67 | 8 | ✅ |
| `authorization-server` | 19 | — | ✅ |
| `notification-server` | 8 | — | ✅ |
| `api-gateway` | — | — | ✅ (config lista, sin tests) |
| `discovery-server` | — | — | ✅ (config lista, sin tests) |
| `event-store-server` | — | — | ✅ (config lista, sin tests) |
| `common` | — | — | ✅ (config lista, sin tests) |

> JaCoCo solo genera `jacoco.xml` si el módulo tiene tests que corran. Sin tests no hay `jacoco.exec` y el goal `report` se salta — SonarQube muestra esos módulos sin cobertura hasta que se escriban tests.

---

## Comandos

### Un módulo — solo unitarios (sin Docker)

```bash
mvn -pl common,enrollment-server -am test
mvn -pl common,authorization-server -am test
mvn -pl common,notification-server -am test
```

### Un módulo — unitarios + integración + cobertura (necesita Docker corriendo)

```bash
mvn -pl common,enrollment-server -am verify
```

### Todos los módulos con tests

```bash
# Solo unitarios
mvn -pl common,authorization-server,notification-server,enrollment-server -am test

# Con integración + cobertura
mvn -pl common,enrollment-server -am verify   # los otros no tienen ITs aún
```

### Desde la raíz (pipeline completo — necesita Docker)

```bash
mvn clean verify
```

> Útil antes de `make sonar` para confirmar que todo pasa.

### Una clase específica

```bash
mvn -pl common,enrollment-server -am test -Dtest=EnrollmentApplicationServiceTest
mvn -pl common,authorization-server -am test -Dtest=AuthApplicationServiceTest
```

### SonarQube (análisis completo)

```bash
make sonar-up        # levanta SonarQube en http://localhost:9000
make sonar           # mvn clean verify sonar:sonar (requiere SONAR_TOKEN en .env)
make sonar-down      # detiene SonarQube
```

---

## Flujo recomendado

### Desarrollo del día a día

1. Escribir código.
2. Correr unit tests del módulo afectado:
   ```bash
   mvn -pl common,<módulo> -am test
   ```
3. Si se tocó persistence o contexto: correr `verify` (Docker encendido).

### Antes de hacer commit/PR

```bash
# 1. Unit tests de los tres módulos con tests
mvn -pl common,enrollment-server,authorization-server,notification-server -am test

# 2. ITs de enrollment-server (Docker requerido)
mvn -pl common,enrollment-server -am verify
```

### Análisis de cobertura con SonarQube

```bash
make sonar-up
make sonar
# Abrir http://localhost:9000/dashboard?id=enrollment-system-microservices
make sonar-down
```

---

## Estructura de archivos de test

```
<módulo>/src/test/java/
└── com/app/.../
    ├── domain/model/           # Tests de dominio puro (sin Spring)
    │   └── valueobject/
    ├── application/service/    # Tests de servicios con Mockito
    ├── infrastructure/
    │   └── adapter/in/web/     # @WebMvcTest (enrollment-server)
    └── testsupport/            # Fixtures compartidos (enrollment-server)
        ├── Mothers.java        # Object mothers (constructores de agregados)
        └── PostgresContainerSupport.java  # Base para ITs con Testcontainers
```

---

## Configuración técnica relevante

**Testcontainers** — versión 1.21.4 (override en `enrollment-server/pom.xml`). La versión 1.19.x que gestiona Boot 3.2 no es compatible con la API mínima de Docker 29.

**Perfil `test`** — `enrollment-server/src/test/resources/application-test.yml` deshabilita Eureka, Kafka y Mercado Pago para los ITs de contexto completo.

**JaCoCo** — configurado en cada pom de módulo (los módulos heredan de `spring-boot-starter-parent`, no del pom raíz). Versión 0.8.11.

**`common`** — siempre incluir en `-pl` con `-am`. Sin él se usa el jar cacheado de `~/.m2` y los tests pueden fallar silenciosamente con clases desactualizadas.

---

## Agregar tests nuevos

### Unitario nuevo

1. Crear clase `*Test` en el paquete espejo dentro de `src/test/java/`.
2. Anotar con `@ExtendWith(MockitoExtension.class)` si usa mocks.
3. Ejecutar con `mvn -pl common,<módulo> -am test`.

### IT nuevo (solo enrollment-server por ahora)

1. Crear clase `*IT` extendiendo `PostgresContainerSupport`.
2. Anotar con `@SpringBootTest` + `@ActiveProfiles("test")`.
3. Ejecutar con `mvn -pl common,enrollment-server -am verify` (Docker encendido).

### Nuevo módulo con tests

Todos los módulos ya tienen Failsafe + JaCoCo configurados en su `pom.xml` (`common` solo JaCoCo). Para dar cobertura a un módulo basta con escribir tests `*Test`/`*IT`; el reporte `target/site/jacoco/jacoco.xml` se genera solo en `verify` y SonarQube lo recoge sin config adicional.
