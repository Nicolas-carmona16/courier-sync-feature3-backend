package com.ep18.couriersync.backend.common.pagination;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageRequestUtil {
    private PageRequestUtil() {}

    public static final int DEFAULT_PAGE = 0;
    public static final int DEFAULT_SIZE = 10;
    public static final int MAX_SIZE = 100;

    public static Pageable of(Integer page, Integer size, Sort sort) {
        int p = (page == null || page < 0) ? DEFAULT_PAGE : page;
        int s = (size == null || size <= 0) ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
        return PageRequest.of(p, s, sort == null ? Sort.unsorted() : sort);
    }
}
