
package com.ep18.couriersync.backend.common.dto;

import java.util.List;

public final class PagingDTOs {
    private PagingDTOs() {}

    public record PageInfo(int page, int size, long totalElements, int totalPages) {}

    /** En APIs (GraphQL/REST), puedes devolver Page<T> como este envoltorio genérico. */
    public record PageResponse<T>(List<T> content, PageInfo pageInfo) {}
}