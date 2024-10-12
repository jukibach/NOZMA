package com.nozma.core.dto.response;

import java.util.List;

public record AccountPageResponse(
        Integer pageSize,
        Integer pageIndex,
        long totalRecords,
        List<AccountColumnResponse> columns,
        List<AccountDetailResponse> response
) {
}
