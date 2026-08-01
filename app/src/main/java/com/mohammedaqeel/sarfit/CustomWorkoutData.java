package com.mohammedaqeel.sarfit;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Simple exercise pools per muscle group, used by the Custom Day Builder
 *  so the user can combine any muscle groups freely (e.g. Chest + Triceps). */
public class CustomWorkoutData {

    private static Exercise c(String name) { return new Exercise(name, true); }
    private static Exercise i(String name) { return new Exercise(name, false); }

    public static Map<String, List<Exercise>> getMusclePool() {
        Map<String, List<Exercise>> pool = new LinkedHashMap<>();
        pool.put("Chest", Arrays.asList(c("Bench Press"), c("Incline DB Press"), i("Cable Fly")));
        pool.put("Back", Arrays.asList(c("Deadlift"), c("Lat Pulldown"), c("Seated Row")));
        pool.put("Shoulders", Arrays.asList(c("Overhead Press"), i("Lateral Raise"), i("Rear Delt Fly")));
        pool.put("Biceps", Arrays.asList(i("Barbell Curl"), i("Hammer Curl"), i("Incline DB Curl")));
        pool.put("Triceps", Arrays.asList(i("Pushdown"), i("Overhead DB Extension"), c("Close Grip Bench Press")));
        pool.put("Legs", Arrays.asList(c("Squat"), c("Romanian Deadlift"), c("Leg Press")));
        pool.put("Core", Arrays.asList(i("Hanging Leg Raise"), i("Cable Crunch"), i("Plank")));
        pool.put("Cardio", Arrays.asList(i("Steady-State Cardio (20-30 min)"), i("HIIT Intervals (15-20 min)")));
        return pool;
    }
}
