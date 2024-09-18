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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "t_exercises_muscles_group", schema = "s_workout")
@Entity(name = "ExerciseMuscleGroups")
@EntityListeners(AuditingEntityListener.class)
public class ExerciseMuscleGroup extends BaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long exerciseId;
    private int muscleGroupId;
}
