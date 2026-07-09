# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

University enrollment system built as Spring Boot microservices (Java 17, Spring Boot 3.2.5, Spring Cloud 2023.0.2) with a React 19 + TypeScript + Vite frontend. Everything runs via Docker Compose. Documentation and code comments are largely in Spanish.

## Commands

### Full stack (Docker)

```bash
make up      # build + start everything (uses docker-compose.yml + docker-compose.override.yml)
make down    # stop (keeps data)
make reset   # destroy volumes (DBs included) and rebuild from scratch
make logs    # tail all container logs
make prod    # docker-compose.yml only — no host-exposed debug ports
```

Requires a `.env` at repo root (`cp .env.example .env`; a full example is in the README). The override compose file exposes ports to the host (Eureka 8761, gateway `${API_GATEWAY_PORT}`=8080, Postgres 5432/5433/5434, Kafka on 29092 for host access, enrollment-server debug on 5005).

### Backend (Maven)

Root `pom.xml` is a multi-module aggregator (modules: `discovery-server`, `api-gateway`, `authorization-server`, `common`, `enrollment-server`, `notification-server`, `event-store-server`). There is no root `mvnw` — each service module has its own wrapper, or use system `mvn` from the root.

```bash
mvn clean package                          # build all modules from root
mvn -pl enrollment-server -am package      # build one service + its deps (common)
cd enrollment-server && ./mvnw test        # run one service's tests
./mvnw test -Dtest=SomeTest                # single test class
```

`common` is a shared library (annotations `@UseCase`/`@Adapter`, constants, enums, `AuditEvent`) consumed by the services — build it (`-am` handles this) before building a service standalone.

### Frontend (`frontend/`)

```bash
npm run dev      # Vite dev server (localhost:5173)
npm run build    # tsc -b && vite build
npm run lint     # eslint
```

There is no test suite beyond the default Spring Boot context-load tests and no frontend tests.

## Architecture

### Request flow

All client traffic enters through **api-gateway** (Spring Cloud Gateway, port 8080), which resolves services by name via **discovery-server** (Eureka, port 8761) using `lb://` URIs. Routes are defined in code, not YAML: `api-gateway/src/main/java/com/app/api/gateway/config/RouterConfig.java`:

- `/auth/**` → `authorization-server`
- `/api/**` → `enrollment-server`, guarded by `AuthFilter`, which validates the JWT by calling `authorization-server` `/auth/validateToken` (centralized auth — downstream services trust the gateway)
- `/webhooks/**` → `enrollment-server`, deliberately WITHOUT `AuthFilter` (external callers like Mercado Pago cannot present a JWT); authenticity is enforced downstream via HMAC signature validation. nginx (`frontend/nginx.conf`) also proxies this path.
- `/v3/api-docs/*` routes aggregate Swagger docs at the gateway (`/swagger-ui.html`)

Authentication is OAuth2 (GitHub/Google) + JWT + TOTP-based MFA, all owned by `authorization-server`.

### Event-driven pieces (Kafka)

- **Audit trail**: `AuditGlobalFilter` in the gateway publishes every request as an `AuditEvent` (defined in `common`) to the `audit.requests` topic; `event-store-server` consumes and persists it.
- **Domain events with Outbox pattern**: `enrollment-server` writes enrollment events to an `OutboxEventJpaEntity` table in the same transaction as the domain change; `OutboxProcessor` (scheduled) publishes them to the `enrollment.notifications` topic. Only critical domain events use Outbox — audit events do not.
- **notification-server** consumes `enrollment.notifications` and sends email (Mailtrap SMTP in dev).

### Payments (Mercado Pago Checkout Pro)

Enrollments are created `PENDING`; payment confirms them. On enrollment, `EnrollmentApplicationService` calls `PaymentGatewayPort` (adapter: `MercadoPagoPaymentAdapter`, official `com.mercadopago:sdk-java`) to create a Checkout Pro preference with `external_reference` = enrollment id and the `CourseOffering.price` (fallback: `MP_ENROLLMENT_FEE` config). The `init_point` travels in `EnrollmentAssignedEvent.paymentUrl` through the outbox so notification-server emails a "Pagar inscripción" button. Preference creation is fail-soft: if MP is down the enrollment is still created and the email goes out without the link.

`POST /webhooks/mercadopago` (`MercadoPagoWebhookController`, outside `/api` so it skips JWT and `PermissionInterceptor`) validates the `x-signature` HMAC (`MercadoPagoSignatureValidator`, secret `MP_WEBHOOK_SECRET`; skipped with a warning if unset), fetches the payment from the MP API, and if `approved` transitions the enrollment `PENDING → PAID` (`Enrollment.markAsPaid`, idempotent — repeated notifications are no-ops) storing `payment_id`/`payment_status`/`paid_at`, then emits a PAID outbox event (confirmation email). Nonexistent payments (MP 404, e.g. the panel's webhook simulator) and missing enrollments return 200 so MP stops retrying; transient errors return 500 so MP retries. `back_urls` point to the public frontend route `/payments/:status` (registered at router root, outside `PublicRoute`/`ProtectedRoute`). MP env vars: `MP_ACCESS_TOKEN`, `MP_WEBHOOK_SECRET`, `MP_NOTIFICATION_URL` (public https), `MP_BACK_URLS_BASE`, `MP_ENROLLMENT_FEE`, `MP_CURRENCY_ID` (see `.env.example`). In the MP panel subscribe to the **payment** topic ("Pagos"), not merchant orders.

### Database per service

Three separate PostgreSQL containers: `postgres-enrollment`, `postgres-auth`, `postgres-events`. Services never share a database.

### Hexagonal layout inside services

`enrollment-server` and `authorization-server` follow ports & adapters with the same package shape:

- `domain/` — models, repositories (interfaces), domain events, exceptions
- `application/` — DTOs (command/response), mappers, `port/in` + `port/out` interfaces, services annotated with `@UseCase`
- `infrastructure/adapter/in` — REST controllers; `infrastructure/adapter/out` — JPA persistence, Kafka messaging (annotated `@Adapter`)

Keep new code within this layering: controllers depend on `port/in`, application services depend on `port/out`, JPA entities stay under `infrastructure`.

### Frontend structure

Feature-based: `frontend/src/features/<feature>/{components,hooks,pages,services,types}` (features: auth, faculty, career, course, course-offering, term, enrollment, students, profile, rbac). Cross-cutting config lives in `frontend/src/config/` (axios `apiClient`, `endpoints.ts`, React Query `keys.ts`, `routeProtection.ts`). State: Zustand (`src/store`) + TanStack Query for server state; forms use react-hook-form + Zod; UI is Tailwind 4 + shadcn/Radix.

### Observability

Prometheus + Grafana + Loki/Promtail are wired into the compose stack; services expose `/actuator/health` and `/actuator/prometheus`.
