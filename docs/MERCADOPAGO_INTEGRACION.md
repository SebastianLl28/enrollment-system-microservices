# Integración Mercado Pago — Checkout Pro

Sistema de matrícula universitaria · Microservicios Spring Boot + React 19

---

## Contexto

Cuando un administrador registra una matrícula en el sistema, el estudiante necesita pagar para confirmarla. En lugar de construir un procesador de pagos propio, se integró **Mercado Pago Checkout Pro**: MP muestra su propia página de pago (tarjetas, Yape, PagoEfectivo, etc.) y luego avisa al backend via webhook si el pago fue aprobado.

El flujo tiene tres momentos clave:

```
[1] Crear matrícula   →  backend crea preferencia MP  →  genera init_point (URL de pago)
[2] Estudiante paga   →  MP redirige al frontend       →  página de resultado
[3] MP avisa al back  →  webhook HMAC-firmado          →  matrícula PENDING → PAID
```

---

## 1. Configuración

### Variables de entorno (`../.env`)

```env
MP_ACCESS_TOKEN=APP_USR-...          # Token de la app MP (Credenciales → Producción/Test)
MP_WEBHOOK_SECRET=abc123...          # Clave secreta del webhook (panel MP → Webhooks)
MP_NOTIFICATION_URL=https://tu-dominio.com/webhooks/mercadopago  # URL pública que MP llama
MP_BACK_URLS_BASE=http://localhost:5173/payments  # Base de las URLs de retorno al frontend
MP_ENROLLMENT_FEE=150.00             # Precio por defecto si la carrera no tiene precio
MP_CURRENCY_ID=PEN                   # Moneda (PEN = Soles peruanos)
```

> **Importante:** `MP_NOTIFICATION_URL` debe ser una URL **pública y accesible desde internet**.
> En desarrollo se puede usar [ngrok](https://ngrok.com) o [localtunnel](https://localtunnel.me).

### `application.yml` (enrollment-server)

```yaml
mercadopago:
  access-token: ${MP_ACCESS_TOKEN:}
  webhook-secret: ${MP_WEBHOOK_SECRET:}
  notification-url: ${MP_NOTIFICATION_URL:}
  back-urls-base: ${MP_BACK_URLS_BASE:http://localhost:5173/payments}
  enrollment-fee: ${MP_ENROLLMENT_FEE:150.00}
  currency-id: ${MP_CURRENCY_ID:PEN}
```

Spring mapea estas propiedades al record `MercadoPagoProperties`:

```java
@ConfigurationProperties(prefix = "mercadopago")
public record MercadoPagoProperties(
  String accessToken,
  String webhookSecret,
  String notificationUrl,
  String backUrlsBase,
  BigDecimal enrollmentFee,
  String currencyId
) {}
```

### Inicialización del SDK

Al arrancar el `enrollment-server`, `MercadoPagoConfig` inicializa el SDK oficial con el access token usando `@PostConstruct`:

```java
@Configuration
@EnableConfigurationProperties(MercadoPagoProperties.class)
public class MercadoPagoConfig {

  @PostConstruct
  public void init() {
    if (properties.accessToken() == null || properties.accessToken().isBlank()) {
      log.warn("MP_ACCESS_TOKEN no configurado: integración deshabilitada");
      return;
    }
    com.mercadopago.MercadoPagoConfig.setAccessToken(properties.accessToken());
    log.info("Mercado Pago SDK inicializado");
  }
}
```

Si no se configura el token, la integración se deshabilita con un warning pero el sistema sigue funcionando (la matrícula se crea sin link de pago).

---

## 2. Dependencia Maven

```xml
<!-- enrollment-server/pom.xml -->
<dependency>
  <groupId>com.mercadopago</groupId>
  <artifactId>sdk-java</artifactId>
  <version>2.1.24</version>
</dependency>
```

---

## 3. Arquitectura hexagonal del pago

El proyecto sigue el patrón de **puertos y adaptadores**. El dominio/aplicación nunca conoce Mercado Pago directamente; solo habla con una interfaz:

```
application/port/out/PaymentGatewayPort.java   ← PUERTO (interfaz)
        │
        │ implementa
        ▼
infrastructure/adapter/out/payment/
    MercadoPagoPaymentAdapter.java              ← ADAPTADOR (SDK de MP)
```

### Puerto de salida

```java
public interface PaymentGatewayPort {
  PaymentPreferenceResponse createPreference(CreatePaymentPreferenceCommand command);
  PaymentDetailsResponse getPayment(String paymentId);
}
```

Dos operaciones:
- `createPreference` — crea la preferencia de pago en MP y retorna el `init_point`
- `getPayment` — consulta el estado de un pago en MP por su ID

### Adaptador: `MercadoPagoPaymentAdapter`

#### Crear preferencia de pago

```java
@Override
public PaymentPreferenceResponse createPreference(CreatePaymentPreferenceCommand command) {

  BigDecimal unitPrice = command.amount() != null
      ? command.amount()
      : properties.enrollmentFee();  // fallback si la carrera no tiene precio

  PreferenceItemRequest item = PreferenceItemRequest.builder()
      .id(command.enrollmentId().toString())
      .title("Matrícula - " + command.careerName() + " (" + command.termCode() + ")")
      .description("Matrícula de " + command.studentFullName() + "...")
      .quantity(1)
      .currencyId(properties.currencyId())
      .unitPrice(unitPrice)
      .build();

  PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
      .success(backUrlsBase + "/success")
      .pending(backUrlsBase + "/pending")
      .failure(backUrlsBase + "/failure")
      .build();

  PreferenceRequest request = PreferenceRequest.builder()
      .items(List.of(item))
      .payer(PreferencePayerRequest.builder()
          .name(command.studentFullName())
          .email(command.studentEmail())
          .build())
      .externalReference(command.enrollmentId().toString())  // ← ID de matrícula
      .notificationUrl(properties.notificationUrl())
      .backUrls(backUrls)
      .build();

  Preference preference = new PreferenceClient().create(request);
  return new PaymentPreferenceResponse(preference.getId(), preference.getInitPoint());
}
```

Puntos clave:
- **`externalReference`** = ID de la matrícula. MP lo devuelve en el webhook, así el backend sabe a qué matrícula corresponde el pago.
- **`notificationUrl`** = `MP_NOTIFICATION_URL`: URL pública a la que MP llama cuando el estado del pago cambia.
- **`backUrls`** = Rutas del frontend donde MP redirige al estudiante después del checkout.
- **`auto_return`** solo se activa si `backUrlsBase` usa `https://` (requisito de MP).

#### Consultar un pago

```java
@Override
public PaymentDetailsResponse getPayment(String paymentId) {
  Payment payment = new PaymentClient().get(Long.valueOf(paymentId));

  return new PaymentDetailsResponse(
      payment.getId().toString(),
      payment.getStatus(),               // "approved", "pending", "rejected"...
      payment.getExternalReference(),    // ID de matrícula
      payment.getDateApproved() != null
          ? payment.getDateApproved().toInstant()
          : null
  );
}
```

Si MP responde 404, se lanza `PaymentNotFoundException` (el webhook lo captura y devuelve 200 para que MP deje de reintentar).

---

## 4. Flujo de creación de matrícula

Cuando el administrador envía el formulario, `EnrollmentApplicationService.createEnrollment` ejecuta todo en **una sola transacción**:

```
1. Verifica que el estudiante no tenga ya una matrícula PENDING/PAID para esa carrera-periodo
2. Crea Enrollment con status = PENDING
3. Incrementa enrolledCount en CareerOffering
4. Llama a createPaymentPreference (MP SDK) → obtiene init_point
5. Guarda evento en la tabla Outbox (incluye paymentUrl)
6. OutboxProcessor (scheduler) publica el evento a Kafka
7. notification-server consume el evento y envía email con el botón "Pagar matrícula"
```

La llamada a MP está envuelta en un try/catch:

```java
private String createPaymentPreference(Enrollment enrollment, Student student,
    Career career, Term term, CareerOffering careerOffering) {
  try {
    PaymentPreferenceResponse pref = paymentGatewayPort.createPreference(
        new CreatePaymentPreferenceCommand(
            enrollment.getID().getValue(),
            career.getName(),
            term.getCode(),
            student.getFullName(),
            student.getEmail().getValue(),
            careerOffering.getPrice()
        ));
    return pref.initPoint();
  } catch (Exception e) {
    log.error("No se pudo crear preferencia MP para matrícula {}: {}",
        enrollment.getID().getValue(), e.getMessage());
    return null;   // ← la matrícula se crea igualmente, email sale sin link
  }
}
```

**Comportamiento fail-soft:** si MP está caído, la matrícula igual queda registrada. El estudiante recibe el correo pero sin el botón de pago.

---

## 5. Webhook — Notificación de pago

### Por qué existe el webhook

El frontend solo sabe si el pago fue *iniciado* (MP redirigió al estudiante). El **estado real** (aprobado/rechazado) lo notifica MP al backend via HTTP POST.

### Ruta del gateway sin autenticación

```java
// api-gateway/RouterConfig.java
.route("payment-webhooks",
    r -> r.path("/webhooks/**")
        .uri("lb://enrollment-server"))  // sin AuthFilter ni JWT
```

MP no puede presentar un JWT. El gateway deja pasar `/webhooks/**` directamente; la autenticidad se verifica en el backend con **HMAC-SHA256**.

### `MercadoPagoWebhookController`

```java
@RestController
@RequestMapping("/webhooks")
public class MercadoPagoWebhookController {

  @PostMapping("/mercadopago")
  public ResponseEntity<Void> handleNotification(
      @RequestParam(name = "type", required = false) String type,
      @RequestParam(name = "data.id", required = false) String dataId,
      @RequestHeader(name = "x-signature", required = false) String xSignature,
      @RequestHeader(name = "x-request-id", required = false) String xRequestId,
      @RequestBody(required = false) JsonNode body) {

    // 1. Solo procesar notificaciones de tipo "payment"
    if (!"payment".equalsIgnoreCase(notificationType)) {
      return ResponseEntity.ok().build();  // merchant_order, chargebacks → ignorar
    }

    // 2. Verificar firma HMAC
    if (!signatureValidator.isValid(xSignature, xRequestId, dataId)) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // 3. Procesar el pago
    try {
      processPaymentNotificationUseCase.processPaymentNotification(paymentId);
    } catch (EnrollmentNotFoundException e) {
      log.error("Matrícula no encontrada: {}", e.getMessage());
      // 200 → MP no reintenta (no tiene caso)
    } catch (PaymentNotFoundException e) {
      log.warn("Pago no existe en MP (simulador del panel): {}", e.getMessage());
      // 200 → MP no reintenta
    }

    return ResponseEntity.ok().build();
  }
}
```

**Códigos de respuesta intencionales:**
| Situación | Respuesta | Por qué |
|-----------|-----------|---------|
| Pago procesado OK | 200 | Confirmar recepción |
| Matrícula no existe | 200 | Reintentar no lo arreglaría |
| Pago 404 en MP | 200 | Simulador del panel, no retryable |
| Error transiente (DB caída) | 500 | MP reintenta automáticamente |
| Firma inválida | 401 | Rechazar petición falsa |

### Validación HMAC (`MercadoPagoSignatureValidator`)

MP firma cada webhook con `HMAC-SHA256`. El header llega así:

```
x-signature: ts=1720000000,v1=abc123def456...
```

El backend reconstruye el mismo "manifest" que MP firmó y compara:

```java
public boolean isValid(String xSignature, String xRequestId, String dataId) {

  // Extraer ts y v1 del header
  String ts = ...; // timestamp
  String v1 = ...; // hash hex

  // Construir el mismo string que MP firmó
  StringBuilder manifest = new StringBuilder();
  if (dataId != null) manifest.append("id:").append(dataId.toLowerCase()).append(";");
  if (xRequestId != null) manifest.append("request-id:").append(xRequestId).append(";");
  manifest.append("ts:").append(ts).append(";");

  // HMAC-SHA256 con la clave secreta del webhook
  Mac mac = Mac.getInstance("HmacSHA256");
  mac.init(new SecretKeySpec(secret.getBytes(UTF_8), "HmacSHA256"));
  byte[] hash = mac.doFinal(manifest.toString().getBytes(UTF_8));
  String expected = HexFormat.of().formatHex(hash);

  // Comparación segura (evita timing attacks)
  return MessageDigest.isEqual(
      expected.getBytes(UTF_8),
      v1.toLowerCase().getBytes(UTF_8)
  );
}
```

Si `MP_WEBHOOK_SECRET` no está configurado, la validación se **omite con un warning** (útil en desarrollo local con el simulador del panel).

### Procesamiento del pago (`PaymentApplicationService`)

```java
@Transactional
public void processPaymentNotification(String paymentId) {

  // 1. Consultar el estado real en la API de MP
  PaymentDetailsResponse payment = paymentGatewayPort.getPayment(paymentId);

  if (!"approved".equalsIgnoreCase(payment.status())) {
    log.info("Pago {} con estado '{}', nada que hacer", paymentId, payment.status());
    return;
  }

  // 2. Buscar la matrícula por external_reference
  Integer enrollmentId = Integer.valueOf(payment.externalReference());
  Enrollment enrollment = enrollmentRepository.findByEnrollmentID(new EnrollmentID(enrollmentId))
      .orElseThrow(() -> new EnrollmentNotFoundException("..."));

  // 3. Marcar como pagada (idempotente: si ya es PAID retorna false y no hace nada)
  boolean updated = enrollment.markAsPaid(
      payment.paymentId(),
      payment.status(),
      payment.dateApproved()
  );

  if (!updated) {
    log.info("Matrícula {} ya estaba PAID, notificación duplicada ignorada", enrollmentId);
    return;
  }

  // 4. Persistir y emitir evento de dominio (correo de confirmación)
  enrollmentRepository.save(enrollment);
  saveOutboxEvent(enrollment, paidAt);  // → Kafka → notification-server → email
}
```

La transición de estado en el dominio:

```java
// Enrollment.java (dominio)
public boolean markAsPaid(String paymentId, String paymentStatus, Instant paidAt) {
  if (this.status == EnrollmentStatus.PAID) return false;   // idempotente
  if (this.status == EnrollmentStatus.CANCELLED)
    throw new CannotChangeStatusOfCancelledEnrollmentException("...");

  this.status = EnrollmentStatus.PAID;
  this.paymentId = paymentId;
  this.paymentStatus = paymentStatus;
  this.paidAt = paidAt;
  return true;
}
```

---

## 6. Frontend

### Formulario de matrícula

`EnrollmentForm.tsx` muestra dos selects: estudiante y carrera en vigencia (con precio visible):

```tsx
// features/enrollment/components/EnrollmentForm.tsx

const careerOfferingOptions = careerOfferings?.map((offering) => ({
  value: offering.id,
  label: `${offering.term.code} - ${offering.career.name}${
    offering.price != null ? ` (S/ ${offering.price.toFixed(2)})` : ""
  }`,
})) ?? [];
```

El precio del `CareerOffering` es lo que se cobra en MP. Si no tiene precio definido, el backend usa el fallback `MP_ENROLLMENT_FEE`.

### Servicio y mutación

```ts
// features/enrollment/services/EnrollmentService.ts
export const postEnrollment = async (enrollmentRequest: EnrollmentRequest) => {
  return await apiClient
    .post<EnrollmentResponse>(ENROLLMENT_ENDPOINT.base, enrollmentRequest)
    .then((res) => res.data);
};
```

```ts
// features/enrollment/hooks/useMutation.ts
export const usePostEnrollment = () => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: postEnrollment,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ENROLLMENT_LIST_QUERY });
      toast.success("Inscripción creada con éxito");
    },
    onError: (error) => {
      toast.error(getApiErrorMessage(error, "No se pudo crear la inscripción"));
    },
  });
};
```

El frontend **no maneja el `init_point`** directamente porque el link de pago llega al estudiante por **correo electrónico** (vía Kafka → notification-server). El administrador solo crea la matrícula y el sistema hace el resto.

### Página de resultado de pago (`PaymentResultPage.tsx`)

MP redirige al estudiante a `/payments/:status` con query params adicionales:

```
/payments/success?payment_id=123456&status=approved&...
/payments/pending?payment_id=null&...
/payments/failure?...
```

```tsx
// pages/PaymentResultPage.tsx

const RESULT_CONTENT = {
  success: {
    icon: <CheckCircle2 className="h-16 w-16 text-green-500" />,
    title: "¡Pago realizado con éxito!",
    message: "Recibimos tu pago. En unos minutos te llegará un correo confirmando tu matrícula.",
  },
  pending: {
    icon: <Clock className="h-16 w-16 text-amber-500" />,
    title: "Pago en proceso",
    message: "Tu pago está siendo procesado. Te avisaremos por correo apenas se confirme.",
  },
  failure: {
    icon: <XCircle className="h-16 w-16 text-red-500" />,
    title: "El pago no se completó",
    message: "No se pudo procesar tu pago. Puedes intentarlo desde el enlace en tu correo.",
  },
} as const;

const PaymentResultPage = () => {
  const { status } = useParams();           // "success" | "pending" | "failure"
  const [searchParams] = useSearchParams();

  const content = RESULT_CONTENT[(status ?? "") as ResultStatus] ?? RESULT_CONTENT.pending;
  const paymentId = searchParams.get("payment_id");

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50 p-6 text-center">
      <div className="w-full max-w-md rounded-xl bg-white p-8 shadow-md">
        <div className="flex justify-center">{content.icon}</div>
        <h1 className="mt-4 text-2xl font-semibold text-gray-900">{content.title}</h1>
        <p className="mt-2 text-gray-600">{content.message}</p>
        {paymentId && paymentId !== "null" && (
          <p className="mt-4 text-xs text-gray-400">
            N° de operación: <span className="font-mono">{paymentId}</span>
          </p>
        )}
        <Link to={ROUTE_PATHS.root} className="mt-6 inline-block rounded-md bg-blue-600 px-6 py-2 text-sm font-medium text-white hover:bg-blue-700">
          Volver al inicio
        </Link>
      </div>
    </div>
  );
};
```

### Registro de la ruta (sin protección de sesión)

```tsx
// app/route/routes.tsx

// Fuera de PublicRoute y ProtectedRoute:
// Un estudiante puede llegar desde el correo sin estar logueado,
// y uno con sesión activa no debe ser redirigido al dashboard.
{
  path: ROUTE_PATHS.paymentResult,   // "/payments/:status"
  element: <PaymentResultPage />,
},
```

```ts
// app/route/path.ts
export const ROUTE_PATHS = {
  paymentResult: "/payments/:status",
  // ...
};
```

### Vista de detalle de matrícula

Desde `EnrollmentDetailDialog.tsx` el administrador puede cambiar el estado manualmente (por si el webhook no llegó o para testing):

```tsx
const statusConfig = {
  PENDING:   { label: "Pendiente",   color: "bg-yellow-100 text-yellow-800", icon: Clock },
  PAID:      { label: "Pagado",      color: "bg-green-100 text-green-800",   icon: CheckCircle },
  CANCELLED: { label: "Cancelado",   color: "bg-red-100 text-red-800",       icon: XCircle },
  COMPLETED: { label: "Completado",  color: "bg-blue-100 text-blue-800",     icon: Award },
};
```

Solo usuarios con permiso `ENROLLMENT → UPDATE` ven el selector de estado.

---

## 7. Flujo completo de extremo a extremo

```
Administrador
    │
    │  POST /api/v1/enrollment  { studentId, careerOfferingId }
    ▼
API Gateway (puerto 8080)
    │  AuthFilter valida JWT
    ▼
enrollment-server
    │  EnrollmentApplicationService.createEnrollment()
    │  ├─ Crea Enrollment (status=PENDING) en PostgreSQL
    │  ├─ Incrementa enrolledCount del CareerOffering
    │  ├─ Llama MP SDK → PreferenceClient.create()
    │  │     └─ MP devuelve { id, init_point: "https://www.mercadopago.com.pe/checkout/..." }
    │  └─ Guarda OutboxEvent con paymentUrl=init_point
    │
    │  [Scheduler cada N segundos]
    │  OutboxProcessor → publica a Kafka topic "enrollment.notifications"
    │
    ▼
notification-server
    │  Consume el evento → envía email al estudiante
    │  Asunto: "Completa tu matrícula"
    │  Body: botón "Pagar matrícula" → init_point de MP
    ▼

Estudiante (recibe correo)
    │
    │  Clic en "Pagar matrícula" → abre init_point de MP
    ▼
Mercado Pago Checkout Pro
    │  Estudiante ingresa datos de pago
    │  Pago aprobado ✓
    │  ├─ Redirige al estudiante → https://frontend.com/payments/success?payment_id=123
    │  └─ POST a MP_NOTIFICATION_URL (webhook)
    │
    ▼ (webhook)
API Gateway
    │  /webhooks/** → sin AuthFilter, directo al enrollment-server
    ▼
enrollment-server
    │  MercadoPagoWebhookController.handleNotification()
    │  ├─ Filtra tipo "payment"
    │  ├─ Valida firma HMAC (x-signature) con MP_WEBHOOK_SECRET
    │  └─ PaymentApplicationService.processPaymentNotification(paymentId)
    │       ├─ Consulta pago en MP API → status="approved"
    │       ├─ Busca matrícula por external_reference
    │       ├─ enrollment.markAsPaid(...)  → status=PAID (idempotente)
    │       ├─ Persiste en PostgreSQL
    │       └─ Guarda OutboxEvent de PAID → email de confirmación
    ▼

Estudiante (recibe segundo correo)
    "Tu matrícula ha sido confirmada"
```

---

## 8. Configuración en el panel de Mercado Pago

1. Ir a **Mercado Pago Developers** → Tu aplicación → Credenciales
2. Copiar el **Access Token** de prueba (`TEST-...`) o producción (`APP_USR-...`) y ponerlo en `MP_ACCESS_TOKEN`
3. Ir a **Webhooks** → Agregar URL → `https://tu-dominio.com/webhooks/mercadopago`
4. Suscribirse al tópico **Pagos** (`payment`) — NO `merchant_order`
5. Copiar la **Clave secreta** generada y ponerla en `MP_WEBHOOK_SECRET`
6. Para probar: usar la opción **"Simular notificación"** del panel (envía un POST de prueba al webhook)

> En desarrollo local: el simulador del panel manda un pago inexistente (404 en la API de MP).
> El webhook lo maneja como `PaymentNotFoundException` y devuelve 200 para que MP no reintente.
> Para una prueba real usar tarjetas de prueba de MP Sandbox.
