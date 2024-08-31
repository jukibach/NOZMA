package com.ecommerce.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ExerciseRow {
    private long id;
    private String name;
    private String description;
    private String majorMuscle;
    private String mechanics;
    private String bodyRegion;
    private String laterality;
    private boolean isWeight;
    private boolean isCardio;
    private boolean isPlyo;
}
