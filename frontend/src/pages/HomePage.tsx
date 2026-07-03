import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { buttonVariants } from "@/components/ui/button";
import {
  BookOpen,
  Users,
  Workflow,
  Database,
  Server,
  Globe,
  ArrowRight,
  Shield,
  Mail,
  Network,
  Activity,
  ScrollText,
  KeyRound,
  Radio,
} from "lucide-react";
import { Link } from "react-router-dom";

const Chip = ({
  children,
  color = "blue",
}: {
  children: React.ReactNode;
  color?: "blue" | "green" | "purple" | "amber" | "rose" | "slate";
}) => {
  const colors: Record<string, string> = {
    blue: "bg-blue-100 text-blue-700",
    green: "bg-green-100 text-green-700",
    purple: "bg-purple-100 text-purple-700",
    amber: "bg-amber-100 text-amber-700",
    rose: "bg-rose-100 text-rose-700",
    slate: "bg-slate-200 text-slate-700",
  };
  return (
    <span className={`px-3 py-1 rounded-full text-xs font-medium ${colors[color]}`}>
      {children}
    </span>
  );
};

const FlowStep = ({
  n,
  title,
  detail,
  color,
}: {
  n: number;
  title: string;
  detail: string;
  color: "blue" | "green";
}) => (
  <div className="flex items-start gap-3">
    <div
      className={`w-8 h-8 rounded-full flex items-center justify-center font-bold shrink-0 ${
        color === "blue" ? "bg-blue-100 text-blue-600" : "bg-green-100 text-green-600"
      }`}
    >
      {n}
    </div>
    <div>
      <p className="font-semibold">{title}</p>
      <p className="text-sm text-slate-600">{detail}</p>
    </div>
  </div>
);

const FlowArrow = ({ label }: { label?: string }) => (
  <div className="flex flex-col items-center gap-1">
    <div className="w-1 h-6 bg-slate-300"></div>
    {label && (
      <span className="text-xs text-slate-500 font-mono -my-0.5">{label}</span>
    )}
    <div className="w-1 h-6 bg-slate-300"></div>
  </div>
);

export default function HomePage() {
  return (
    <div className="min-h-screen bg-linear-to-br from-slate-50 to-slate-100">
      {/* Header */}
      <header className="border-b bg-white/80 backdrop-blur-sm sticky top-0 z-50">
        <div className="container mx-auto px-6 py-4 flex justify-between items-center">
          <div className="flex items-center gap-2">
            <BookOpen className="w-8 h-8 text-blue-600" />
            <h1 className="text-2xl font-bold text-slate-800">CourseHub</h1>
          </div>
          <nav className="flex gap-6">
            <a
              href="#features"
              className="text-slate-600 hover:text-blue-600 transition"
            >
              Características
            </a>
            <a
              href="#flows"
              className="text-slate-600 hover:text-blue-600 transition"
            >
              Flujos
            </a>
            <a
              href="#architecture"
              className="text-slate-600 hover:text-blue-600 transition"
            >
              Arquitectura
            </a>
          </nav>
        </div>
      </header>

      {/* Hero Section */}
      <section className="container mx-auto px-6 py-20 text-center">
        <div className="max-w-3xl mx-auto">
          <h2 className="text-5xl font-bold text-slate-800 mb-6">
            Sistema de Inscripción de Cursos
          </h2>
          <p className="text-xl text-slate-600 mb-8">
            Plataforma universitaria construida con microservicios: gestión
            académica, inscripciones con notificaciones en tiempo real y
            seguridad basada en roles
          </p>
          <div className="flex gap-4 justify-center">
            <Link
              className={buttonVariants({ size: "lg", variant: "default" })}
              to="/login"
            >
              Ir al Login <ArrowRight className="ml-2 w-4 h-4" />
            </Link>
          </div>
        </div>
      </section>

      {/* Features Section */}
      <section id="features" className="container mx-auto px-6 py-16">
        <h3 className="text-3xl font-bold text-center text-slate-800 mb-12">
          ¿Qué hace nuestro sistema?
        </h3>
        <div className="grid md:grid-cols-3 gap-6">
          <Card className="hover:shadow-lg transition">
            <CardHeader>
              <BookOpen className="w-12 h-12 text-green-600 mb-4" />
              <CardTitle>Gestión Académica</CardTitle>
              <CardDescription>
                Facultades, carreras, cursos, periodos académicos y cursos en
                vigencia con control de cupos y secciones
              </CardDescription>
            </CardHeader>
          </Card>

          <Card className="hover:shadow-lg transition">
            <CardHeader>
              <Users className="w-12 h-12 text-blue-600 mb-4" />
              <CardTitle>Estudiantes e Inscripciones</CardTitle>
              <CardDescription>
                Registro de estudiantes, inscripción a cursos con validación de
                cupos y duplicados, historial académico y notificaciones por
                email
              </CardDescription>
            </CardHeader>
          </Card>

          <Card className="hover:shadow-lg transition">
            <CardHeader>
              <Shield className="w-12 h-12 text-purple-600 mb-4" />
              <CardTitle>Seguridad y Acceso</CardTitle>
              <CardDescription>
                Login con OAuth2 (GitHub/Google) o contraseña, doble factor
                TOTP y control de acceso por roles y permisos (RBAC) en cada
                módulo
              </CardDescription>
            </CardHeader>
          </Card>
        </div>
      </section>

      {/* Flows Section */}
      <section id="flows" className="bg-white py-16">
        <div className="container mx-auto px-6">
          <h3 className="text-3xl font-bold text-center text-slate-800 mb-12">
            Flujos del Sistema
          </h3>
          <div className="grid md:grid-cols-2 gap-8">
            <Card>
              <CardHeader>
                <Workflow className="w-10 h-10 text-blue-600 mb-2" />
                <CardTitle>Flujo de Inscripción</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <FlowStep
                    n={1}
                    color="blue"
                    title="Autenticación"
                    detail="Login con usuario/contraseña u OAuth2; el gateway valida el JWT y los permisos en cada petición"
                  />
                  <FlowStep
                    n={2}
                    color="blue"
                    title="Selección de Curso en Vigencia"
                    detail="Se elige un curso ofertado en el periodo académico activo, con sección y cupo disponibles"
                  />
                  <FlowStep
                    n={3}
                    color="blue"
                    title="Validación de Negocio"
                    detail="El servicio de inscripciones verifica cupo, evita duplicados y actualiza el conteo en la misma transacción"
                  />
                  <FlowStep
                    n={4}
                    color="blue"
                    title="Confirmación y Notificación"
                    detail="La inscripción genera un evento que dispara un correo de confirmación al estudiante"
                  />
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <Workflow className="w-10 h-10 text-green-600 mb-2" />
                <CardTitle>Flujo de Eventos (Kafka)</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <FlowStep
                    n={1}
                    color="green"
                    title="Outbox Transaccional"
                    detail="Los eventos de dominio se guardan en la misma transacción que el cambio de datos (patrón Outbox)"
                  />
                  <FlowStep
                    n={2}
                    color="green"
                    title="Publicación a Kafka"
                    detail="Un proceso programado publica los eventos pendientes al topic enrollment.notifications"
                  />
                  <FlowStep
                    n={3}
                    color="green"
                    title="Notificación por Email"
                    detail="El notification-server consume el evento y envía el correo al estudiante"
                  />
                  <FlowStep
                    n={4}
                    color="green"
                    title="Auditoría Completa"
                    detail="El gateway publica cada petición al topic audit.requests y el event-store la persiste como trazabilidad"
                  />
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Architecture Section */}
      <section id="architecture" className="container mx-auto px-6 py-16">
        <h3 className="text-3xl font-bold text-center text-slate-800 mb-4">
          Arquitectura de Microservicios
        </h3>
        <p className="text-center text-slate-600 mb-12 max-w-2xl mx-auto">
          Spring Boot 3 + Spring Cloud, servicios con arquitectura hexagonal,
          base de datos por servicio y comunicación asíncrona con Kafka. Todo
          orquestado con Docker Compose.
        </p>
        <Card className="max-w-5xl mx-auto">
          <CardContent className="p-8">
            <div className="space-y-2">
              {/* Cliente */}
              <div className="bg-blue-50 p-6 rounded-lg border-2 border-blue-200">
                <div className="flex items-center gap-3 mb-3">
                  <Globe className="w-8 h-8 text-blue-600" />
                  <h4 className="text-xl font-bold text-slate-800">Cliente</h4>
                </div>
                <p className="text-slate-600 mb-3">
                  SPA en React 19 + TypeScript + Vite
                </p>
                <div className="flex flex-wrap gap-2">
                  <Chip>TanStack Query</Chip>
                  <Chip>Zustand</Chip>
                  <Chip>Tailwind CSS 4</Chip>
                  <Chip>shadcn/ui</Chip>
                  <Chip>React Router</Chip>
                </div>
              </div>

              <div className="flex justify-center">
                <FlowArrow label="HTTP :8080" />
              </div>

              {/* Gateway + Discovery */}
              <div className="grid md:grid-cols-3 gap-4">
                <div className="md:col-span-2 bg-amber-50 p-6 rounded-lg border-2 border-amber-200">
                  <div className="flex items-center gap-3 mb-3">
                    <Server className="w-8 h-8 text-amber-600" />
                    <h4 className="text-xl font-bold text-slate-800">
                      API Gateway
                    </h4>
                  </div>
                  <p className="text-slate-600 mb-3">
                    Spring Cloud Gateway: punto único de entrada
                  </p>
                  <div className="flex flex-wrap gap-2">
                    <Chip color="amber">Validación JWT centralizada</Chip>
                    <Chip color="amber">Autorización RBAC</Chip>
                    <Chip color="amber">Auditoría → Kafka</Chip>
                    <Chip color="amber">Swagger agregado</Chip>
                  </div>
                </div>
                <div className="bg-slate-50 p-6 rounded-lg border-2 border-slate-200">
                  <div className="flex items-center gap-3 mb-3">
                    <Network className="w-8 h-8 text-slate-600" />
                    <h4 className="text-lg font-bold text-slate-800">
                      Discovery
                    </h4>
                  </div>
                  <p className="text-slate-600 mb-3 text-sm">
                    Eureka Server: registro y descubrimiento de servicios
                  </p>
                  <Chip color="slate">lb:// balanceo</Chip>
                </div>
              </div>

              <div className="flex justify-center">
                <FlowArrow label="rutas /auth/** y /api/**" />
              </div>

              {/* Servicios core */}
              <div className="grid md:grid-cols-2 gap-4">
                <div className="bg-purple-50 p-6 rounded-lg border-2 border-purple-200">
                  <div className="flex items-center gap-3 mb-3">
                    <KeyRound className="w-8 h-8 text-purple-600" />
                    <h4 className="text-lg font-bold text-slate-800">
                      Authorization Server
                    </h4>
                  </div>
                  <p className="text-slate-600 mb-3 text-sm">
                    Identidad, OAuth2 (GitHub/Google), 2FA TOTP y administración
                    de roles, permisos y vistas
                  </p>
                  <div className="flex flex-wrap gap-2">
                    <Chip color="purple">JWT</Chip>
                    <Chip color="purple">RBAC</Chip>
                    <Chip color="purple">PostgreSQL propia</Chip>
                  </div>
                </div>
                <div className="bg-green-50 p-6 rounded-lg border-2 border-green-200">
                  <div className="flex items-center gap-3 mb-3">
                    <BookOpen className="w-8 h-8 text-green-600" />
                    <h4 className="text-lg font-bold text-slate-800">
                      Enrollment Server
                    </h4>
                  </div>
                  <p className="text-slate-600 mb-3 text-sm">
                    Núcleo académico: facultades, carreras, cursos, periodos,
                    estudiantes e inscripciones
                  </p>
                  <div className="flex flex-wrap gap-2">
                    <Chip color="green">Arquitectura hexagonal</Chip>
                    <Chip color="green">Patrón Outbox</Chip>
                    <Chip color="green">PostgreSQL propia</Chip>
                  </div>
                </div>
              </div>

              <div className="flex justify-center">
                <FlowArrow label="eventos" />
              </div>

              {/* Kafka */}
              <div className="bg-rose-50 p-6 rounded-lg border-2 border-rose-200">
                <div className="flex items-center gap-3 mb-3">
                  <Radio className="w-8 h-8 text-rose-600" />
                  <h4 className="text-xl font-bold text-slate-800">
                    Apache Kafka
                  </h4>
                </div>
                <p className="text-slate-600 mb-3">
                  Bus de eventos para comunicación asíncrona entre servicios
                </p>
                <div className="flex flex-wrap gap-2">
                  <Chip color="rose">topic: enrollment.notifications</Chip>
                  <Chip color="rose">topic: audit.requests</Chip>
                </div>
              </div>

              <div className="flex justify-center">
                <FlowArrow label="consumen" />
              </div>

              {/* Consumidores */}
              <div className="grid md:grid-cols-2 gap-4">
                <div className="bg-sky-50 p-6 rounded-lg border-2 border-sky-200">
                  <div className="flex items-center gap-3 mb-3">
                    <Mail className="w-8 h-8 text-sky-600" />
                    <h4 className="text-lg font-bold text-slate-800">
                      Notification Server
                    </h4>
                  </div>
                  <p className="text-slate-600 mb-3 text-sm">
                    Consume eventos de inscripción y envía correos de
                    confirmación al estudiante
                  </p>
                  <Chip>SMTP</Chip>
                </div>
                <div className="bg-indigo-50 p-6 rounded-lg border-2 border-indigo-200">
                  <div className="flex items-center gap-3 mb-3">
                    <ScrollText className="w-8 h-8 text-indigo-600" />
                    <h4 className="text-lg font-bold text-slate-800">
                      Event Store Server
                    </h4>
                  </div>
                  <p className="text-slate-600 mb-3 text-sm">
                    Persiste la auditoría de todas las peticiones que pasan por
                    el gateway
                  </p>
                  <div className="flex flex-wrap gap-2">
                    <Chip>Trazabilidad</Chip>
                    <Chip>PostgreSQL propia</Chip>
                  </div>
                </div>
              </div>

              {/* Infraestructura transversal */}
              <div className="pt-4 grid md:grid-cols-2 gap-4">
                <div className="bg-slate-50 p-5 rounded-lg border border-slate-200">
                  <div className="flex items-center gap-3 mb-2">
                    <Database className="w-6 h-6 text-slate-600" />
                    <h4 className="font-bold text-slate-800">
                      Base de datos por servicio
                    </h4>
                  </div>
                  <p className="text-sm text-slate-600">
                    Tres instancias PostgreSQL independientes (auth, enrollment
                    y events): ningún servicio comparte base de datos.
                  </p>
                </div>
                <div className="bg-slate-50 p-5 rounded-lg border border-slate-200">
                  <div className="flex items-center gap-3 mb-2">
                    <Activity className="w-6 h-6 text-slate-600" />
                    <h4 className="font-bold text-slate-800">Observabilidad</h4>
                  </div>
                  <div className="flex flex-wrap gap-2 mt-2">
                    <Chip color="slate">Prometheus</Chip>
                    <Chip color="slate">Grafana</Chip>
                    <Chip color="slate">Loki + Promtail</Chip>
                    <Chip color="slate">Spring Actuator</Chip>
                  </div>
                </div>
              </div>
            </div>
          </CardContent>
        </Card>
      </section>

      {/* CTA Section */}
      <section className="bg-linear-to-r from-blue-600 to-purple-600 py-16">
        <div className="container mx-auto px-6 text-center text-white">
          <h3 className="text-3xl font-bold mb-4">
            ¿Listo para gestionar tus inscripciones?
          </h3>
          <p className="text-xl mb-8 text-blue-100">
            Accede al sistema y comienza a administrar tus cursos
          </p>
          <Link
            className={buttonVariants({ size: "lg", variant: "default" })}
            to="/login"
          >
            Iniciar Sesión <ArrowRight className="ml-2 w-4 h-4" />
          </Link>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-slate-900 text-slate-400 py-8">
        <div className="container mx-auto px-6 text-center">
          <p>&copy; 2026 CourseHub. Sistema de Inscripción de Cursos.</p>
        </div>
      </footer>
    </div>
  );
}
