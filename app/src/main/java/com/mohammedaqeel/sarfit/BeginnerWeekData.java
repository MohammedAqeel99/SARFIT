package com.mohammedaqeel.sarfit;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/** A simple 7-day general introduction program for brand-new users.
 *  Lighter, simpler movements than the main advanced split. */
public class BeginnerWeekData {

    private static Exercise c(String name) { return new Exercise(name, true); }
    private static Exercise i(String name) { return new Exercise(name, false); }

    public static Map<Integer, MuscleSection> getDays() {
        Map<Integer, MuscleSection> days = new LinkedHashMap<>();

        days.put(1, new MuscleSection("Day 1 - Full Body Intro", Arrays.asList(
                c("Bodyweight Squat"),
                c("Incline Push-Up"),
                i("Plank")
        )));

        days.put(2, new MuscleSection("Day 2 - Chest + Shoulders", Arrays.asList(
                c("Push-Up"),
                i("Dumbbell Shoulder Press (light)"),
                i("Lateral Raise (light)")
        )));

        days.put(3, new MuscleSection("Day 3 - Back + Core", Arrays.asList(
                c("Assisted Pull-Up or Band Row"),
                i("Superman Hold"),
                i("Dead Bug")
        )));

        days.put(4, new MuscleSection("Day 4 - Light Cardio & Mobility", Arrays.asList(
                i("Brisk Walk or Cycle (20 min)"),
                i("Full Body Stretch (10 min)")
        )));

        days.put(5, new MuscleSection("Day 5 - Legs", Arrays.asList(
                c("Bodyweight Lunge"),
                c("Glute Bridge"),
                i("Calf Raise")
        )));

        days.put(6, new MuscleSection("Day 6 - Arms", Arrays.asList(
                i("Dumbbell Curl (light)"),
                i("Tricep Dip (bench)"),
                i("Wrist Curl")
        )));

        days.put(7, new MuscleSection("Day 7 - Full Body Recap", Arrays.asList(
                c("Bodyweight Squat"),
                c("Push-Up"),
                i("Plank")
        )));

        return days;
    }
}
