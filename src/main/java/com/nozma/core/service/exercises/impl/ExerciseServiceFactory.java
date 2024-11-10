package com.nozma.core.service.exercises.impl;

import com.nozma.core.enums.ApplicationRole;
import com.nozma.core.service.exercises.ExerciseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ExerciseServiceFactory {
    
    private final BeanFactory beanFactory;
    
    @Autowired
    public ExerciseServiceFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
    
    public ExerciseService getExerciseService(String userType) {
        String beanName = ApplicationRole.valueOf(userType).getRoleName() + ExerciseService.class.getSimpleName() + "Impl";
        return (ExerciseService) beanFactory.getBean(beanName);
    }
}
