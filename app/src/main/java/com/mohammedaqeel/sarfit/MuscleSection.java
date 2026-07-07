package com.mohammedaqeel.sarfit;

import java.util.List;

public class MuscleSection {
    public String muscleName;
    public List<Exercise> exercises;

    public MuscleSection(String muscleName, List<Exercise> exercises) {
        this.muscleName = muscleName;
        this.exercises = exercises;
    }
}
