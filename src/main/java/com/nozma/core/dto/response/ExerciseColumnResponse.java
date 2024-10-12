package com.nozma.core.dto.response;

public record ExerciseColumnResponse(
        String code,
        String name,
        String type,
        boolean visible
) {
    public ExerciseColumnResponse(
            String code,
            String name,
            String type
    ) {
        this(code, name, type, true);
    }
}
