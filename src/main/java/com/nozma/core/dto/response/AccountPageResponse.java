package com.nozma.core.dto.response;

import java.util.List;

public record AccountPageResponse(
        Integer pageSize,
        Integer pageIndex,
        Integer totalRecords,
        List<AccountDetailResponse> response
) {
}
