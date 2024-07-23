package com.ecommerce.userservice.dto.request;

import java.util.List;
import java.util.Map;

public record PagePayload(
        List<String> visibleColumns,
        Boolean wrapLine,
        Integer pageSize,
        Integer pageIndex,
        Map<String, String> sortPreferences,
        String searchName
) {
}
