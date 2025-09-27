package com.ep18.couriersync.backend.common.dto;

import java.util.List;

public final class PagingDTOs {
    private PagingDTOs() {}

    public record PageInfo(int page, int size, long totalElements, int totalPages) {}

    public record PageResponse<T>(List<T> content, PageInfo pageInfo) {}
}