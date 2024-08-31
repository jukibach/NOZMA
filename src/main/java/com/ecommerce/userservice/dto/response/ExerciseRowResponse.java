package com.ecommerce.userservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ExerciseRowResponse {
    private long id;
    private String name;
    private String description;
    private String majorMuscle;
    private String mechanics;
    private String bodyRegion;
    private String laterality;
    private List<String> exerciseTypes;
    private List<String> equipments;
    private List<String> movementPatterns;
    private List<String> muscleGroup;
}
