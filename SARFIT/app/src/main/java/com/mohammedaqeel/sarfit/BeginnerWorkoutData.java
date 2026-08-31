package com.mohammedaqeel.sarfit;

import java.util.ArrayList;
import java.util.List;

public class BeginnerWorkoutData {

    public static List<Exercise> getGeneralBeginnerRoutine() {
        List<Exercise> routine = new ArrayList<>();

        // Chest (2 Variations)
        routine.add(new Exercise("Push-ups", "3 Sets x 12 Reps", "chest_pushup"));
        routine.add(new Exercise("Incline Dumbbell Press", "3 Sets x 10 Reps", "chest_incline_press"));

        // Back (2 Variations)
        routine.add(new Exercise("Lat Pulldown", "3 Sets x 12 Reps", "back_lat_pulldown"));
        routine.add(new Exercise("Seated Cable Row", "3 Sets x 10 Reps", "back_seated_row"));

        // Legs (2 Variations)
        routine.add(new Exercise("Bodyweight Squats", "3 Sets x 15 Reps", "legs_bodyweight_squat"));
        routine.add(new Exercise("Lying Leg Curls", "3 Sets x 12 Reps", "legs_lying_curl"));

        // Shoulders (2 Variations)
        routine.add(new Exercise("Dumbbell Overhead Press", "3 Sets x 10 Reps", "shoulder_db_press"));
        routine.add(new Exercise("Lateral Raises", "3 Sets x 12 Reps", "shoulder_lateral_raise"));

        // Arms (2 Variations)
        routine.add(new Exercise("Dumbbell Bicep Curls", "3 Sets x 12 Reps", "bicep_db_curl"));
        routine.add(new Exercise("Tricep Rope Pushdowns", "3 Sets x 12 Reps", "tricep_rope_pushdown"));

        return routine;
    }
}
