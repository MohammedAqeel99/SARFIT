package com.mohammedaqeel.sarfit;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/** Every muscle group's 4-week exercise progression, kept independent so any
 *  combination of groups can be assigned to any day (e.g. Chest + Triceps on Monday). */
public class MasterMuscleData {

    private static Exercise c(String name) { return new Exercise(name, true); }
    private static Exercise i(String name) { return new Exercise(name, false); }
    private static Exercise cardio(String name, String text) { return new Exercise(name, text); }

    public static final String[] ALL_MUSCLES = {
            "Chest", "Back", "Shoulders", "Biceps", "Triceps", "Forearms", "Legs", "Core", "Cardio"
    };

    public static Map<String, WeekPlan[]> getPools() {
        Map<String, WeekPlan[]> pools = new LinkedHashMap<>();

        pools.put("Chest", new WeekPlan[]{
                new WeekPlan(1, Arrays.asList(new MuscleSection("Chest", Arrays.asList(c("Bench Press"), c("Incline DB Press"), i("Cable Fly"), c("Dips"))))),
                new WeekPlan(2, Arrays.asList(new MuscleSection("Chest", Arrays.asList(c("Smith Machine Bench"), c("Incline Machine Press"), i("Low-to-High Fly"), c("Decline Pushups"))))),
                new WeekPlan(3, Arrays.asList(new MuscleSection("Chest", Arrays.asList(c("Floor Press"), c("Cable Incline Press"), i("Svend Press"), c("Weighted Dips"))))),
                new WeekPlan(4, Arrays.asList(new MuscleSection("Chest", Arrays.asList(c("Spoto Press"), c("Neutral Grip DB Press"), i("High-to-Low Fly"), c("Ring Dips")))))
        });

        pools.put("Back", new WeekPlan[]{
                new WeekPlan(1, Arrays.asList(new MuscleSection("Back", Arrays.asList(c("Deadlift"), c("Lat Pulldown"), c("Seated Row"), c("1-Arm DB Row"))))),
                new WeekPlan(2, Arrays.asList(new MuscleSection("Back", Arrays.asList(c("Barbell Row"), c("Close Grip Pulldown"), c("Machine Row"), i("Pullover"))))),
                new WeekPlan(3, Arrays.asList(new MuscleSection("Back", Arrays.asList(c("Snatch Grip Deadlift"), c("V-Bar Pulldown"), i("Single Arm Cable Row"), c("Landmine Row"))))),
                new WeekPlan(4, Arrays.asList(new MuscleSection("Back", Arrays.asList(c("Trap Bar Deadlift"), c("Neutral Grip Pulldown"), c("Chest Supported DB Row"), i("Renegade Row")))))
        });

        pools.put("Shoulders", new WeekPlan[]{
                new WeekPlan(1, Arrays.asList(new MuscleSection("Shoulders", Arrays.asList(c("Overhead Press"), i("Lateral Raise"), i("Rear Delt Fly"), i("Front Raise"))))),
                new WeekPlan(2, Arrays.asList(new MuscleSection("Shoulders", Arrays.asList(c("Seated DB Press"), i("Cable Lateral Raise"), i("Face Pull"), i("Plate Front Raise"))))),
                new WeekPlan(3, Arrays.asList(new MuscleSection("Shoulders", Arrays.asList(c("Push Press"), i("Machine Lateral Raise"), i("Reverse Pec Deck"), i("Cable Front Raise"))))),
                new WeekPlan(4, Arrays.asList(new MuscleSection("Shoulders", Arrays.asList(c("Arnold Press"), i("21s Lateral Raise"), i("Band Pull-Apart"), i("Incline Front Raise")))))
        });

        pools.put("Biceps", new WeekPlan[]{
                new WeekPlan(1, Arrays.asList(new MuscleSection("Biceps", Arrays.asList(i("Barbell Curl"), i("Incline DB Curl"), i("Hammer Curl"))))),
                new WeekPlan(2, Arrays.asList(new MuscleSection("Biceps", Arrays.asList(i("Concentration Curl"), i("Cable Curl"), i("Cross Body Hammer Curl"))))),
                new WeekPlan(3, Arrays.asList(new MuscleSection("Biceps", Arrays.asList(i("Drag Curl"), i("Zottman Curl"), i("Cable Rope Curl"))))),
                new WeekPlan(4, Arrays.asList(new MuscleSection("Biceps", Arrays.asList(c("Chin-Up (Curl Focus)"), i("Bayesian Curl"), i("Rope Hammer Curl 21s")))))
        });

        pools.put("Triceps", new WeekPlan[]{
                new WeekPlan(1, Arrays.asList(new MuscleSection("Triceps", Arrays.asList(i("Pushdown"), i("Overhead DB Extension"), i("Skull Crushers"))))),
                new WeekPlan(2, Arrays.asList(new MuscleSection("Triceps", Arrays.asList(i("V-Bar Pushdown"), i("EZ Bar Skull Crushers"), i("Bench Dips"))))),
                new WeekPlan(3, Arrays.asList(new MuscleSection("Triceps", Arrays.asList(c("JM Press"), i("Kickbacks"), i("Tate Press"))))),
                new WeekPlan(4, Arrays.asList(new MuscleSection("Triceps", Arrays.asList(c("Close Grip Board Press"), i("Overhead Rope Extension"), c("Machine Dip")))))
        });

        pools.put("Forearms", new WeekPlan[]{
                new WeekPlan(1, Arrays.asList(new MuscleSection("Forearms", Arrays.asList(i("Wrist Curl"), i("Reverse Wrist Curl"), cardio("Farmer's Carry", "3 sets x 40m"))))),
                new WeekPlan(2, Arrays.asList(new MuscleSection("Forearms", Arrays.asList(i("Reverse Barbell Curl"), i("Behind-Back Wrist Curl"), i("Plate Pinch Hold"))))),
                new WeekPlan(3, Arrays.asList(new MuscleSection("Forearms", Arrays.asList(i("Hammer Curl (Forearm Focus)"), i("Wrist Roller"), cardio("Dead Hang", "3 sets x max time"))))),
                new WeekPlan(4, Arrays.asList(new MuscleSection("Forearms", Arrays.asList(i("Zottman Wrist Curl"), i("Fat Grip Farmer's Carry"), i("Finger Curl")))))
        });

        pools.put("Legs", new WeekPlan[]{
                new WeekPlan(1, Arrays.asList(new MuscleSection("Legs", Arrays.asList(c("Squat"), c("Romanian Deadlift"), c("Leg Press"))))),
                new WeekPlan(2, Arrays.asList(new MuscleSection("Legs", Arrays.asList(c("Hack Squat"), c("Bulgarian Split Squat"), i("Leg Curl"))))),
                new WeekPlan(3, Arrays.asList(new MuscleSection("Legs", Arrays.asList(c("Zercher Squat"), c("Step-Ups"), i("Sissy Squat"))))),
                new WeekPlan(4, Arrays.asList(new MuscleSection("Legs", Arrays.asList(c("Belt Squat"), c("Reverse Lunge"), i("Glute Ham Raise")))))
        });

        pools.put("Core", new WeekPlan[]{
                new WeekPlan(1, Arrays.asList(new MuscleSection("Core", Arrays.asList(i("Hanging Leg Raise"), i("Cable Crunch"))))),
                new WeekPlan(2, Arrays.asList(new MuscleSection("Core", Arrays.asList(i("Russian Twist"), i("Ab Wheel Rollout"))))),
                new WeekPlan(3, Arrays.asList(new MuscleSection("Core", Arrays.asList(cardio("Weighted Plank", "3 sets x 45-60 sec hold"), i("Woodchopper"))))),
                new WeekPlan(4, Arrays.asList(new MuscleSection("Core", Arrays.asList(i("Hanging Windshield Wipers"), i("Cable Woodchop")))))
        });

        pools.put("Cardio", new WeekPlan[]{
                new WeekPlan(1, Arrays.asList(new MuscleSection("Cardio", Arrays.asList(cardio("Steady-State Cycling/Jog", "30 min, moderate intensity"))))),
                new WeekPlan(2, Arrays.asList(new MuscleSection("Cardio", Arrays.asList(cardio("HIIT Intervals", "20 min: 30s sprint / 90s walk x 10"))))),
                new WeekPlan(3, Arrays.asList(new MuscleSection("Cardio", Arrays.asList(cardio("Incline Treadmill Walk", "35 min, incline 8-12%"))))),
                new WeekPlan(4, Arrays.asList(new MuscleSection("Cardio", Arrays.asList(cardio("Active Recovery: Light Cycling", "25 min, easy pace")))))
        });

        return pools;
    }
}
