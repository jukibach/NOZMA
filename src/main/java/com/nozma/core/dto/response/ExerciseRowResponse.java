package com.nozma.core.dto.response;

import java.util.List;

public record ExerciseRowResponse(long id,
                                  String name,
                                  String description,
                                  String majorMuscle,
                                  String mechanics,
                                  String bodyRegion,
                                  String laterality,
                                  List<String> exerciseTypes,
                                  List<String> equipments,
                                  List<String> movementPatterns,
                                  List<String> muscleGroup) {
}
