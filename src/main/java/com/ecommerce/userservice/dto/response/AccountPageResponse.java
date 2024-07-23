package com.ecommerce.userservice.dto.response;

import java.util.List;

public record AccountPageResponse(
        Integer pageSize,
        Integer pageIndex,
        Integer totalRecords,
        List<AccountResponse> response
) {
}
