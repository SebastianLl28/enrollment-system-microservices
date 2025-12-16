// import { ToastProvider as SonnerToastProvider } from "sonner";
import { Toaster } from "sonner";

interface ToastProviderProps {
  children: React.ReactNode;
}

const ToastProvider = ({ children }: ToastProviderProps) => {
  return (
    <>
      <Toaster
        position="bottom-right"
        duration={3000}
        swipeDirections={["bottom", "right"]}
      />
      {children}
    </>
  );
};

export default ToastProvider;
