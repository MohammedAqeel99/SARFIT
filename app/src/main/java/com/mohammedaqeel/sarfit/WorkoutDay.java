package com.mohammedaqeel.sarfit;

import java.util.List;

public class WorkoutDay {
    public String dayName;
    public String focus;
    public boolean isRest;
    public List<WeekPlan> weeks;
    public String bodyHighlight; // "chest_triceps","back_biceps","legs","shoulders_abs","rest"

    public WorkoutDay(String dayName, String focus, boolean isRest, List<WeekPlan> weeks, String bodyHighlight) {
        this.dayName = dayName;
        this.focus = focus;
        this.isRest = isRest;
        this.weeks = weeks;
        this.bodyHighlight = bodyHighlight;
    }
}
