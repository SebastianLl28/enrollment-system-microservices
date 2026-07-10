package com.app.enrollment.system.enrollment.server.application.dto.response;

import java.util.List;

/**
 * @author Alonso
 */
public record PageResponse<T>(List<T> content, int page, int size, long totalElements,
                              int totalPages) {

}
