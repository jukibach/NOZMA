package com.nozma.core.entity.exercises;

import com.nozma.core.entity.BaseDomain;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldNameConstants
@Table(name = "t_display_exercise_settings", schema = "s_workout")
@Entity(name = "DisplayExerciseSettings")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class DisplayExerciseSetting extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long accountId;
    private String code;
    private boolean name;
    private boolean bodyRegion;
    private boolean laterality;
    private boolean majorMuscle;
    private boolean mechanics;
    private boolean equipments;
    private boolean exerciseTypes;
    private boolean muscleGroup;
    private boolean movementPatterns;
    private boolean description;
    
}
