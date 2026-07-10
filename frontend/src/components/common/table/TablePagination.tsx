import { Button } from "@/components/ui/button";
import { ChevronLeft, ChevronRight } from "lucide-react";

interface TablePaginationProps {
  page: number; // 0-based
  totalPages: number;
  totalElements: number;
  onPageChange: (page: number) => void;
}

export function TablePagination({
  page,
  totalPages,
  totalElements,
  onPageChange,
}: TablePaginationProps) {
  if (totalElements === 0) return null;

  return (
    <div className="flex items-center justify-between px-4 pt-2">
      <p className="text-sm text-gray-600">
        Página {page + 1} de {Math.max(totalPages, 1)} · {totalElements}{" "}
        {totalElements === 1 ? "registro" : "registros"}
      </p>
      <div className="flex items-center gap-2">
        <Button
          variant="outline"
          size="sm"
          onClick={() => onPageChange(page - 1)}
          disabled={page <= 0}
        >
          <ChevronLeft className="h-4 w-4" />
          Anterior
        </Button>
        <Button
          variant="outline"
          size="sm"
          onClick={() => onPageChange(page + 1)}
          disabled={page + 1 >= totalPages}
        >
          Siguiente
          <ChevronRight className="h-4 w-4" />
        </Button>
      </div>
    </div>
  );
}
