package com.app.enrollment.system.enrollment.server.domain.repository;

import java.util.List;

/**
 * Resultado paginado agnóstico de la infraestructura (sin dependencia de Spring Data).
 *
 * @author Alonso
 */
public record PageResult<T>(List<T> content, int page, int size, long totalElements,
                            int totalPages) {

}
