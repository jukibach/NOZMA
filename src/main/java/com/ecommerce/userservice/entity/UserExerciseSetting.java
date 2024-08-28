package com.ecommerce.userservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@Table(name = "t_user_exercise_settings", schema = "s_workout")
@Entity(name = "UserExerciseSettings")
@Getter
@Setter
public class UserExerciseSetting extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private long accountId;
    
    private String code;
    
    @Column(columnDefinition = "jsonb")
    private String settings;
    
    private String description;
    
}
