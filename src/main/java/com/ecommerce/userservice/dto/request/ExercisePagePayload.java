package com.ecommerce.userservice.dto.request;

import java.util.List;
import java.util.Map;

public record ExercisePagePayload(
        Boolean wrapLine,
        Integer pageSize,
        Integer pageIndex,
        Map<String, String> sortPreferences,
        String searchName
) {
}
