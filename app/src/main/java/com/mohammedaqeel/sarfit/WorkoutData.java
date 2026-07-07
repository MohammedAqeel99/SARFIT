package com.mohammedaqeel.sarfit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorkoutData {

    private static Exercise c(String name) { return new Exercise(name, true); }
    private static Exercise i(String name) { return new Exercise(name, false); }
    private static Exercise cardio(String name, String text) { return new Exercise(name, text); }

    public static List<WorkoutDay> getSchedule() {
        List<WorkoutDay> days = new ArrayList<>();

        List<WeekPlan> cardioPlan = new ArrayList<>();
        cardioPlan.add(new WeekPlan(1, Arrays.asList(
                new MuscleSection("Cardio", Arrays.asList(
                        cardio("Steady-State Cycling/Jog", "30 min, moderate intensity (Zone 2)"),
                        cardio("Core Finisher: Plank Circuit", "3 rounds x 45 sec plank + 15 sec rest")
                ))
        )));
        cardioPlan.add(new WeekPlan(2, Arrays.asList(
                new MuscleSection("Cardio", Arrays.asList(
                        cardio("HIIT Intervals", "20 min: 30 sec sprint / 90 sec walk x 10"),
                        cardio("Mobility & Stretching", "15 min full-body dynamic stretch")
                ))
        )));
        cardioPlan.add(new WeekPlan(3, Arrays.asList(
                new MuscleSection("Cardio", Arrays.asList(
                        cardio("Incline Treadmill Walk", "35 min, incline 8-12%, brisk pace"),
                        cardio("Core Finisher: Circuit", "3 rounds x 20 leg raises + 20 Russian twists")
                ))
        )));
        cardioPlan.add(new WeekPlan(4, Arrays.asList(
                new MuscleSection("Cardio", Arrays.asList(
                        cardio("Active Recovery: Light Cycling", "25 min, easy pace"),
                        cardio("Full Body Stretch & Foam Roll", "15-20 min recovery session")
                ))
        )));
        days.add(new WorkoutDay("Sunday", "Cardio", false, cardioPlan, "cardio"));

        List<WeekPlan> monPush = new ArrayList<>();
        monPush.add(new WeekPlan(1, Arrays.asList(
                new MuscleSection("Chest", Arrays.asList(c("Bench Press"), c("Incline DB Press"), i("Cable Fly"), c("Dips"))),
                new MuscleSection("Side Delts", Arrays.asList(i("Cable Lateral Raise"), i("DB Lateral Raise"))),
                new MuscleSection("Front Delts", Arrays.asList(c("Overhead Press"), i("Front Raise")))
        )));
        monPush.add(new WeekPlan(2, Arrays.asList(
                new MuscleSection("Chest", Arrays.asList(c("Smith Machine Bench"), c("Incline Machine Press"), i("Low-to-High Fly"), c("Decline Pushups"))),
                new MuscleSection("Side Delts", Arrays.asList(i("Leaning Cable Lateral Raise"), i("Egyptian Lateral Raise"))),
                new MuscleSection("Front Delts", Arrays.asList(c("Seated DB Press"), i("Barbell Front Raise")))
        )));
        monPush.add(new WeekPlan(3, Arrays.asList(
                new MuscleSection("Chest", Arrays.asList(c("Floor Press"), c("Cable Incline Press"), i("Svend Press"), c("Weighted Dips"))),
                new MuscleSection("Side Delts", Arrays.asList(i("Behind-Back Cable Raise"), i("Bottoms-Up Lateral Raise"))),
                new MuscleSection("Front Delts", Arrays.asList(c("Push Press"), i("Plate Raise")))
        )));
        monPush.add(new WeekPlan(4, Arrays.asList(
                new MuscleSection("Chest", Arrays.asList(c("Spoto Press"), c("Neutral Grip DB Press"), i("High-to-Low Fly"), c("Ring Dips"))),
                new MuscleSection("Side Delts", Arrays.asList(i("21s Lateral Raise"), i("Cable Lean-Away Raise"))),
                new MuscleSection("Front Delts", Arrays.asList(c("Viking Press"), i("Band Front Raise")))
        )));
        days.add(new WorkoutDay("Monday", "Chest + Side Delts + Front Delts", false, monPush, "chest_delts"));

        List<WeekPlan> tuePull = new ArrayList<>();
        tuePull.add(new WeekPlan(1, Arrays.asList(
                new MuscleSection("Back", Arrays.asList(c("Deadlift"), c("Lat Pulldown"), c("Seated Row"), c("1-Arm DB Row"))),
                new MuscleSection("Rear Delts", Arrays.asList(i("Rear Delt Fly"), i("Face Pull")))
        )));
        tuePull.add(new WeekPlan(2, Arrays.asList(
                new MuscleSection("Back", Arrays.asList(c("Barbell Row"), c("Close Grip Pulldown"), c("Machine Row"), i("Pullover"))),
                new MuscleSection("Rear Delts", Arrays.asList(i("Cable Rear Delt Fly"), i("Face Pull Rope")))
        )));
        tuePull.add(new WeekPlan(3, Arrays.asList(
                new MuscleSection("Back", Arrays.asList(c("Snatch Grip Deadlift"), c("V-Bar Pulldown"), i("Single Arm Cable Row"), c("Landmine Row"))),
                new MuscleSection("Rear Delts", Arrays.asList(i("Bent-Over Rear Delt Raise"), i("Cross Cable Rear Delt Fly")))
        )));
        tuePull.add(new WeekPlan(4, Arrays.asList(
                new MuscleSection("Back", Arrays.asList(c("Trap Bar Deadlift"), c("Neutral Grip Pulldown"), c("Chest Supported DB Row"), i("Renegade Row"))),
                new MuscleSection("Rear Delts", Arrays.asList(i("Prone Rear Delt Raise"), i("Cable Reverse Fly")))
        )));
        days.add(new WorkoutDay("Tuesday", "Back + Rear Delts", false, tuePull, "back_rear_delts"));

        List<WeekPlan> wedArmsLegs = new ArrayList<>();
        wedArmsLegs.add(new WeekPlan(1, Arrays.asList(
                new MuscleSection("Biceps", Arrays.asList(i("Barbell Curl"), i("Incline DB Curl"), i("Hammer Curl"))),
                new MuscleSection("Triceps", Arrays.asList(i("Pushdown"), i("Overhead DB Extension"), i("Skull Crushers"))),
                new MuscleSection("Legs", Arrays.asList(c("Squat"), c("Romanian Deadlift"), c("Leg Press"))),
                new MuscleSection("Core", Arrays.asList(i("Hanging Leg Raise"), i("Cable Crunch")))
        )));
        wedArmsLegs.add(new WeekPlan(2, Arrays.asList(
                new MuscleSection("Biceps", Arrays.asList(i("Concentration Curl"), i("Cable Curl"), i("Cross Body Hammer Curl"))),
                new MuscleSection("Triceps", Arrays.asList(i("V-Bar Pushdown"), i("EZ Bar Skull Crushers"), i("Bench Dips"))),
                new MuscleSection("Legs", Arrays.asList(c("Hack Squat"), c("Bulgarian Split Squat"), i("Leg Curl"))),
                new MuscleSection("Core", Arrays.asList(i("Russian Twist"), i("Ab Wheel Rollout")))
        )));
        wedArmsLegs.add(new WeekPlan(3, Arrays.asList(
                new MuscleSection("Biceps", Arrays.asList(i("Drag Curl"), i("Zottman Curl"), i("Cable Rope Curl"))),
                new MuscleSection("Triceps", Arrays.asList(c("JM Press"), i("Kickbacks"), i("Tate Press"))),
                new MuscleSection("Legs", Arrays.asList(c("Zercher Squat"), c("Step-Ups"), i("Sissy Squat"))),
                new MuscleSection("Core", Arrays.asList(cardio("Weighted Plank", "3 sets x 45-60 sec hold"), i("Woodchopper")))
        )));
        wedArmsLegs.add(new WeekPlan(4, Arrays.asList(
                new MuscleSection("Biceps", Arrays.asList(c("Chin-Up (Curl Focus)"), i("Bayesian Curl"), i("Rope Hammer Curl 21s"))),
                new MuscleSection("Triceps", Arrays.asList(c("Close Grip Board Press"), i("Overhead Rope Extension"), c("Machine Dip"))),
                new MuscleSection("Legs", Arrays.asList(c("Belt Squat"), c("Reverse Lunge"), i("Glute Ham Raise"))),
                new MuscleSection("Core", Arrays.asList(i("Hanging Windshield Wipers"), i("Cable Woodchop")))
        )));
        days.add(new WorkoutDay("Wednesday", "Biceps + Triceps + Legs + Core", false, wedArmsLegs, "arms_legs_core"));

        List<WeekPlan> thuPush = new ArrayList<>();
        thuPush.add(new WeekPlan(1, Arrays.asList(
                new MuscleSection("Chest", Arrays.asList(c("Incline Barbell Press"), c("Flat DB Press"), i("Pec Deck"), c("Machine Press"))),
                new MuscleSection("Side Delts", Arrays.asList(i("Machine Lateral Raise"), i("Cable Y-Raise"))),
                new MuscleSection("Front Delts", Arrays.asList(c("Arnold Press"), i("Plate Front Raise")))
        )));
        thuPush.add(new WeekPlan(2, Arrays.asList(
                new MuscleSection("Chest", Arrays.asList(c("DB Bench Press"), c("Incline Smith Press"), i("DB Fly"), c("Chest Press Machine"))),
                new MuscleSection("Side Delts", Arrays.asList(i("Lu Raises"), i("Partial Lateral Raise"))),
                new MuscleSection("Front Delts", Arrays.asList(c("Landmine Press"), i("Cable Front Raise")))
        )));
        thuPush.add(new WeekPlan(3, Arrays.asList(
                new MuscleSection("Chest", Arrays.asList(c("Guillotine Press"), c("Reverse Grip Bench"), i("Cable Crossover"), i("Machine Fly"))),
                new MuscleSection("Side Delts", Arrays.asList(i("Single-Arm Cable Lateral"), i("Kettlebell Lateral Raise"))),
                new MuscleSection("Front Delts", Arrays.asList(c("Z Press"), i("DB Front Raise")))
        )));
        thuPush.add(new WeekPlan(4, Arrays.asList(
                new MuscleSection("Chest", Arrays.asList(c("Board Press"), c("Cybex Press"), i("Standing Cable Fly"), c("Chest Dips"))),
                new MuscleSection("Side Delts", Arrays.asList(i("Step-Up Lateral Raise"), i("Double Cable Lateral"))),
                new MuscleSection("Front Delts", Arrays.asList(c("Bradford Press"), i("Incline Front Raise")))
        )));
        days.add(new WorkoutDay("Thursday", "Chest + Side Delts + Front Delts", false, thuPush, "chest_delts"));

        List<WeekPlan> friPull = new ArrayList<>();
        friPull.add(new WeekPlan(1, Arrays.asList(
                new MuscleSection("Back", Arrays.asList(c("Rack Pull"), c("Pull-ups"), c("T-Bar Row"), i("Straight Arm Pulldown"))),
                new MuscleSection("Rear Delts", Arrays.asList(i("Reverse Pec Deck"), i("Band Pull-Apart")))
        )));
        friPull.add(new WeekPlan(2, Arrays.asList(
                new MuscleSection("Back", Arrays.asList(c("Meadows Row"), c("Wide Grip Pulldown"), c("Chest Supported Row"), c("Seal Row"))),
                new MuscleSection("Rear Delts", Arrays.asList(i("Incline Rear Delt Raise"), i("Rope Face Pull")))
        )));
        friPull.add(new WeekPlan(3, Arrays.asList(
                new MuscleSection("Back", Arrays.asList(c("Deficit Deadlift"), c("Kneeling Pulldown"), c("Yates Row"), i("Dumbbell Pullover"))),
                new MuscleSection("Rear Delts", Arrays.asList(i("Machine Rear Delt Fly"), i("Scapular Pull-ups")))
        )));
        friPull.add(new WeekPlan(4, Arrays.asList(
                new MuscleSection("Back", Arrays.asList(c("Pendlay Row"), c("Cross-Body Lat Pulldown"), c("Inverted Row"), c("Good Morning"))),
                new MuscleSection("Rear Delts", Arrays.asList(i("High Pulley Rear Delt Fly"), i("Band Reverse Fly")))
        )));
        days.add(new WorkoutDay("Friday", "Back + Rear Delts", false, friPull, "back_rear_delts"));

        List<WeekPlan> satArmsLegs = new ArrayList<>();
        satArmsLegs.add(new WeekPlan(1, Arrays.asList(
                new MuscleSection("Biceps", Arrays.asList(i("EZ Bar Curl"), i("Preacher Curl"), i("Rope Hammer Curl"))),
                new MuscleSection("Triceps", Arrays.asList(i("Rope Pushdown"), c("Close Grip Bench Press"), i("Single Arm Cable Extension"))),
                new MuscleSection("Legs", Arrays.asList(c("Front Squat"), c("Walking Lunges"), i("Leg Extension"))),
                new MuscleSection("Core", Arrays.asList(i("Decline Situps"), cardio("Plank", "3 sets x 45-60 sec hold")))
        )));
        satArmsLegs.add(new WeekPlan(2, Arrays.asList(
                new MuscleSection("Biceps", Arrays.asList(i("Standing DB Curl"), i("Spider Curl"), i("Reverse Curl"))),
                new MuscleSection("Triceps", Arrays.asList(i("Reverse Grip Pushdown"), i("Rope Overhead Extension"), i("Diamond Pushups"))),
                new MuscleSection("Legs", Arrays.asList(c("Smith Machine Squat"), c("Goblet Squat"), c("Stiff Leg Deadlift"))),
                new MuscleSection("Core", Arrays.asList(i("Toe Touches"), i("Bicycle Crunch")))
        )));
        satArmsLegs.add(new WeekPlan(3, Arrays.asList(
                new MuscleSection("Biceps", Arrays.asList(i("21s Barbell Curl"), i("Wide Grip Curl"), i("Waiter Curl"))),
                new MuscleSection("Triceps", Arrays.asList(i("Floor Press Extension"), i("French Press"), c("Dip Machine"))),
                new MuscleSection("Legs", Arrays.asList(c("Pendulum Squat"), c("Curtsy Lunge"), i("Nordic Curl"))),
                new MuscleSection("Core", Arrays.asList(cardio("Dragon Flag", "3 sets x 6-10 reps"), cardio("Side Plank", "3 sets x 30-45 sec each side")))
        )));
        satArmsLegs.add(new WeekPlan(4, Arrays.asList(
                new MuscleSection("Biceps", Arrays.asList(i("Cheat Curl"), i("Cross-Body Curl"), i("Fat Grip Curl"))),
                new MuscleSection("Triceps", Arrays.asList(i("Skull Crusher to Press"), i("Single Arm Overhead Extension"), c("Weighted Dips"))),
                new MuscleSection("Legs", Arrays.asList(c("Safety Bar Squat"), c("Deficit Reverse Lunge"), i("Copenhagen Plank"))),
                new MuscleSection("Core", Arrays.asList(i("Pallof Press"), cardio("Hollow Body Hold", "3 sets x 30-45 sec")))
        )));
        days.add(new WorkoutDay("Saturday", "Biceps + Triceps + Legs + Core", false, satArmsLegs, "arms_legs_core"));

        return days;
    }
}
