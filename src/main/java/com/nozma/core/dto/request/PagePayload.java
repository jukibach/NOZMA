package com.nozma.core.dto.request;

import org.springframework.data.domain.Pageable;

public record PagePayload(
        Pageable pageable,
        String searchName
) {
    public PagePayload(Pageable pageable) {
        this(pageable, null);
    }
}
