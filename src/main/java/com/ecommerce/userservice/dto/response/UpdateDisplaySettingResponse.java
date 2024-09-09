package com.ecommerce.userservice.dto.response;

public record UpdateDisplaySettingResponse(
        long accountId,
        boolean name,
        boolean bodyRegion,
        boolean laterality,
        boolean majorMuscle,
        boolean mechanics,
        boolean equipments,
        boolean exerciseTypes,
        boolean muscleGroup,
        boolean movementPatterns,
        boolean description) {
}
