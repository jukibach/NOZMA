package com.ecommerce.userservice.projection;

public interface ExerciseView {
    Long getId();
    String getName();
    String getDescription();
    String getMajorMuscle();
    String getMechanics();
    String getBodyRegion();
    String getLaterality();
    boolean getIsWeight();
    boolean getIsCardio();
    boolean getIsPlyo();
}
