import { Link, useParams, useSearchParams } from "react-router-dom";
import { CheckCircle2, Clock, XCircle } from "lucide-react";
import { ROUTE_PATHS } from "@/app/route/path";

/**
 * Página pública a la que Mercado Pago redirige después del checkout
 * (back_urls: /payments/success | /payments/pending | /payments/failure).
 * No requiere sesión: el estudiante puede llegar desde el correo sin estar logueado.
 */

const RESULT_CONTENT = {
  success: {
    icon: <CheckCircle2 className="h-16 w-16 text-green-500" />,
    title: "¡Pago realizado con éxito!",
    message:
      "Recibimos tu pago correctamente. En unos minutos te llegará un correo confirmando tu matrícula.",
  },
  pending: {
    icon: <Clock className="h-16 w-16 text-amber-500" />,
    title: "Pago en proceso",
    message:
      "Tu pago está siendo procesado por Mercado Pago. Te avisaremos por correo apenas se confirme.",
  },
  failure: {
    icon: <XCircle className="h-16 w-16 text-red-500" />,
    title: "El pago no se completó",
    message:
      "No se pudo procesar tu pago. Puedes intentarlo nuevamente desde el enlace que recibiste en tu correo.",
  },
} as const;

type ResultStatus = keyof typeof RESULT_CONTENT;

const PaymentResultPage = () => {
  const { status } = useParams();
  const [searchParams] = useSearchParams();

  const content =
    RESULT_CONTENT[(status ?? "") as ResultStatus] ?? RESULT_CONTENT.pending;

  const paymentId = searchParams.get("payment_id");

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50 p-6 text-center">
      <div className="w-full max-w-md rounded-xl bg-white p-8 shadow-md">
        <div className="flex justify-center">{content.icon}</div>
        <h1 className="mt-4 text-2xl font-semibold text-gray-900">
          {content.title}
        </h1>
        <p className="mt-2 text-gray-600">{content.message}</p>
        {paymentId && paymentId !== "null" && (
          <p className="mt-4 text-xs text-gray-400">
            N° de operación: <span className="font-mono">{paymentId}</span>
          </p>
        )}
        <Link
          to={ROUTE_PATHS.root}
          className="mt-6 inline-block rounded-md bg-blue-600 px-6 py-2 text-sm font-medium text-white hover:bg-blue-700"
        >
          Volver al inicio
        </Link>
      </div>
    </div>
  );
};

export default PaymentResultPage;
