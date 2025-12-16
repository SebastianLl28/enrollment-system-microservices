import { createBrowserRouter } from "react-router-dom";
import { ROUTE_PATHS, AUTH_ROUTE_PATHS } from "@/app/route/path";
import HomePage from "@/pages/HomePage";
import DashboardPage from "@/pages/DashboardPage";
import NotFoundPage from "@/pages/NotFoundPage";
import PublicRoute from "@/features/auth/components/PublicRoute";
import LoginPage from "@/features/auth/pages/LoginPage";
import { OAuth2RedirectHandler } from "@/features/auth/pages/OAuth2RedirectHandler";
import RegisterPage from "@/features/auth/pages/RegisterPage";
import TwoFactorVerifyPage from "@/features/auth/pages/TwoFactorVerifyPage";
import ProtectedRoute from "@/features/auth/components/ProtectedRoute";
import FacultiesPage from "@/features/faculty/pages/FacultiesPage";
import CareerPage from "@/features/career/pages/CareerPage";
import CoursePage from "@/features/course/pages/CoursePage";
import StudentsPage from "@/features/students/pages/StudentsPage";
import ProfilePage from "@/features/profile/pages/ProfilePage";
import TermsPage from "@/features/term/pages/TermsPage";
import CourseOfferingPage from "@/features/course-offering/pages/CourseOfferingPage";
import EnrollmentsPage from "@/features/enrollment/pages/EnrollmentsPage";
import RolesPage from "@/features/rbac/pages/RolesPages";
import UsersRbacPage from "@/features/rbac/pages/UsersRbacPage";
import PermissionsPage from "@/features/rbac/pages/PermissionsPage";

export const router = createBrowserRouter([
  {
    element: <PublicRoute />,
    children: [
      {
        path: ROUTE_PATHS.root,
        element: <HomePage />,
      },
      {
        path: ROUTE_PATHS.login,
        element: <LoginPage />,
      },
      {
        path: AUTH_ROUTE_PATHS.oauth2Login,
        element: <OAuth2RedirectHandler />,
      },
      {
        path: ROUTE_PATHS.register,
        element: <RegisterPage />,
      },
      {
        path: ROUTE_PATHS.twoFactorVerify,
        element: <TwoFactorVerifyPage />,
      },
    ],
  },
  {
    element: <ProtectedRoute />,
    children: [
      {
        path: ROUTE_PATHS.dashboard,
        element: <DashboardPage />,
      },
      {
        path: ROUTE_PATHS.faculty,
        element: <FacultiesPage />,
      },
      {
        path: ROUTE_PATHS.career,
        element: <CareerPage />,
      },
      {
        path: ROUTE_PATHS.course,
        element: <CoursePage />,
      },
      {
        path: ROUTE_PATHS.students,
        element: <StudentsPage />,
      },
      {
        path: ROUTE_PATHS.profile,
        element: <ProfilePage />,
      },
      {
        path: ROUTE_PATHS.term,
        element: <TermsPage />,
      },
      {
        path: ROUTE_PATHS.courseOffering,
        element: <CourseOfferingPage />,
      },
      {
        path: ROUTE_PATHS.enrollment,
        element: <EnrollmentsPage />,
      },
      {
        path: ROUTE_PATHS.rbacRoles,
        element: <RolesPage />,
      },
      {
        path: ROUTE_PATHS.rbacUsers,
        element: <UsersRbacPage />,
      },
      {
        path: ROUTE_PATHS.rbacPermissions,
        element: <PermissionsPage />,
      },
    ],
  },
  {
    path: ROUTE_PATHS.notFound,
    element: <NotFoundPage />,
  },
]);
