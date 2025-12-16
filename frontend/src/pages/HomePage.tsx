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
  CheckCircle,
  Workflow,
  Database,
  Server,
  Globe,
  ArrowRight,
} from "lucide-react";
import { Link } from "react-router-dom";

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
            Plataforma integral para gestionar inscripciones, estudiantes y
            cursos de manera eficiente y escalable
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
              <Users className="w-12 h-12 text-blue-600 mb-4" />
              <CardTitle>Gestión de Estudiantes</CardTitle>
              <CardDescription>
                Registro, autenticación y perfiles completos de estudiantes con
                historial académico
              </CardDescription>
            </CardHeader>
          </Card>

          <Card className="hover:shadow-lg transition">
            <CardHeader>
              <BookOpen className="w-12 h-12 text-green-600 mb-4" />
              <CardTitle>Catálogo de Cursos</CardTitle>
              <CardDescription>
                Gestión completa de cursos, horarios, cupos y requisitos previos
              </CardDescription>
            </CardHeader>
          </Card>

          <Card className="hover:shadow-lg transition">
            <CardHeader>
              <CheckCircle className="w-12 h-12 text-purple-600 mb-4" />
              <CardTitle>Inscripciones Automáticas</CardTitle>
              <CardDescription>
                Proceso de inscripción inteligente con validación de requisitos
                y disponibilidad
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
                  <div className="flex items-start gap-3">
                    <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-bold shrink-0">
                      1
                    </div>
                    <div>
                      <p className="font-semibold">Autenticación</p>
                      <p className="text-sm text-slate-600">
                        El estudiante inicia sesión en el sistema
                      </p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3">
                    <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-bold shrink-0">
                      2
                    </div>
                    <div>
                      <p className="font-semibold">Selección de Curso</p>
                      <p className="text-sm text-slate-600">
                        Busca y selecciona cursos disponibles
                      </p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3">
                    <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-bold shrink-0">
                      3
                    </div>
                    <div>
                      <p className="font-semibold">Validación</p>
                      <p className="text-sm text-slate-600">
                        Sistema verifica requisitos y disponibilidad
                      </p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3">
                    <div className="w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center text-blue-600 font-bold shrink-0">
                      4
                    </div>
                    <div>
                      <p className="font-semibold">Confirmación</p>
                      <p className="text-sm text-slate-600">
                        Inscripción procesada y confirmada
                      </p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>

            <Card>
              <CardHeader>
                <Workflow className="w-10 h-10 text-green-600 mb-2" />
                <CardTitle>Flujo de Gestión de Cursos</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  <div className="flex items-start gap-3">
                    <div className="w-8 h-8 rounded-full bg-green-100 flex items-center justify-center text-green-600 font-bold shrink-0">
                      1
                    </div>
                    <div>
                      <p className="font-semibold">Creación de Curso</p>
                      <p className="text-sm text-slate-600">
                        Administrador crea nuevo curso con detalles
                      </p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3">
                    <div className="w-8 h-8 rounded-full bg-green-100 flex items-center justify-center text-green-600 font-bold shrink-0">
                      2
                    </div>
                    <div>
                      <p className="font-semibold">Configuración</p>
                      <p className="text-sm text-slate-600">
                        Define horarios, cupos y requisitos
                      </p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3">
                    <div className="w-8 h-8 rounded-full bg-green-100 flex items-center justify-center text-green-600 font-bold shrink-0">
                      3
                    </div>
                    <div>
                      <p className="font-semibold">Publicación</p>
                      <p className="text-sm text-slate-600">
                        Curso disponible para inscripciones
                      </p>
                    </div>
                  </div>
                  <div className="flex items-start gap-3">
                    <div className="w-8 h-8 rounded-full bg-green-100 flex items-center justify-center text-green-600 font-bold shrink-0">
                      4
                    </div>
                    <div>
                      <p className="font-semibold">Monitoreo</p>
                      <p className="text-sm text-slate-600">
                        Seguimiento de inscripciones y capacidad
                      </p>
                    </div>
                  </div>
                </div>
              </CardContent>
            </Card>
          </div>
        </div>
      </section>

      {/* Architecture Section */}
      <section id="architecture" className="container mx-auto px-6 py-16">
        <h3 className="text-3xl font-bold text-center text-slate-800 mb-12">
          Arquitectura del Sistema
        </h3>
        <Card className="max-w-4xl mx-auto">
          <CardContent className="p-8">
            <div className="space-y-6">
              {/* Frontend */}
              <div className="bg-blue-50 p-6 rounded-lg border-2 border-blue-200">
                <div className="flex items-center gap-3 mb-3">
                  <Globe className="w-8 h-8 text-blue-600" />
                  <h4 className="text-xl font-bold text-slate-800">
                    Capa de Presentación
                  </h4>
                </div>
                <p className="text-slate-600 mb-2">React + Vite + TypeScript</p>
                <div className="flex flex-wrap gap-2">
                  <span className="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm">
                    shadcn/ui
                  </span>
                  <span className="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm">
                    React Router
                  </span>
                  <span className="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-sm">
                    Tailwind CSS
                  </span>
                </div>
              </div>

              <div className="flex justify-center">
                <div className="w-1 h-8 bg-slate-300"></div>
              </div>

              {/* Backend */}
              <div className="bg-green-50 p-6 rounded-lg border-2 border-green-200">
                <div className="flex items-center gap-3 mb-3">
                  <Server className="w-8 h-8 text-green-600" />
                  <h4 className="text-xl font-bold text-slate-800">
                    Capa de Lógica de Negocio
                  </h4>
                </div>
                <p className="text-slate-600 mb-2">
                  API RESTful / Node.js + Express
                </p>
                <div className="flex flex-wrap gap-2">
                  <span className="px-3 py-1 bg-green-100 text-green-700 rounded-full text-sm">
                    Autenticación JWT
                  </span>
                  <span className="px-3 py-1 bg-green-100 text-green-700 rounded-full text-sm">
                    Validaciones
                  </span>
                  <span className="px-3 py-1 bg-green-100 text-green-700 rounded-full text-sm">
                    Controladores
                  </span>
                </div>
              </div>

              <div className="flex justify-center">
                <div className="w-1 h-8 bg-slate-300"></div>
              </div>

              {/* Database */}
              <div className="bg-purple-50 p-6 rounded-lg border-2 border-purple-200">
                <div className="flex items-center gap-3 mb-3">
                  <Database className="w-8 h-8 text-purple-600" />
                  <h4 className="text-xl font-bold text-slate-800">
                    Capa de Persistencia
                  </h4>
                </div>
                <p className="text-slate-600 mb-2">PostgreSQL / MongoDB</p>
                <div className="flex flex-wrap gap-2">
                  <span className="px-3 py-1 bg-purple-100 text-purple-700 rounded-full text-sm">
                    Usuarios
                  </span>
                  <span className="px-3 py-1 bg-purple-100 text-purple-700 rounded-full text-sm">
                    Cursos
                  </span>
                  <span className="px-3 py-1 bg-purple-100 text-purple-700 rounded-full text-sm">
                    Inscripciones
                  </span>
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
          <p>&copy; 2024 CourseHub. Sistema de Inscripción de Cursos.</p>
        </div>
      </footer>
    </div>
  );
}
