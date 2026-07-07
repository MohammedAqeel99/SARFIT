package com.mohammedaqeel.sarfit;

import java.util.List;

public class WeekPlan {
    public int weekNumber;
    public List<MuscleSection> sections;

    public WeekPlan(int weekNumber, List<MuscleSection> sections) {
        this.weekNumber = weekNumber;
        this.sections = sections;
    }
}
