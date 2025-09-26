package com.ep18.couriersync.backend.common.pagination;

import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageInfo;
import com.ep18.couriersync.backend.common.dto.PagingDTOs.PageResponse;
import org.springframework.data.domain.Page;

import java.util.function.Function;

public final class PageMapper {
    private PageMapper() {}

    /** Mapea Page<E> a PageResponse<D> usando un mapper de elemento. */
    public static <E, D> PageResponse<D> map(Page<E> page, Function<E, D> mapper) {
        return new PageResponse<>(
                page.getContent().stream().map(mapper).toList(),
                new PageInfo(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages())
        );
    }
}
