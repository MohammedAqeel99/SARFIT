"""
Direct Python port of Exercise.java, MuscleSection.java, WeekPlan.java, WorkoutDay.java
"""


class Exercise:
    def __init__(self, name, is_compound=False, custom_text=None):
        self.name = name
        self.is_compound = is_compound
        self.custom_text = custom_text

    def get_sets_reps(self):
        if self.custom_text is not None:
            return self.custom_text
        if self.is_compound:
            return "4 sets x 6-10 reps  |  Rest 2-3 min"
        return "3 sets x 10-15 reps  |  Rest 60-90 sec"

    def get_equipment(self):
        n = self.name.lower()
        if "barbell" in n or "deadlift" in n or "rack pull" in n or "good morning" in n:
            return "Barbell"
        if "ez bar" in n:
            return "EZ Bar"
        if "smith" in n:
            return "Smith Machine"
        if any(k in n for k in ("cable", "pushdown", "pulldown", "crossover", "woodchop")):
            return "Cable Machine"
        if any(k in n for k in ("machine", "pec deck", "hack squat", "leg press", "leg extension", "leg curl")):
            return "Machine"
        if "kettlebell" in n:
            return "Kettlebell"
        if "band" in n:
            return "Resistance Band"
        if any(k in n for k in ("db ", "dumbbell", "arnold", "goblet")):
            return "Dumbbells"
        if any(k in n for k in ("dip", "pull-up", "pullup", "chin-up", "push-up", "pushup",
                                 "plank", "hollow", "sit-up", "situp", "hanging", "dragon flag", "nordic")):
            return "Bodyweight"
        if "landmine" in n:
            return "Landmine + Barbell"
        if "plate" in n:
            return "Weight Plate"
        if "rope" in n:
            return "Cable Rope Attachment"
        return "Gym Equipment"

    def get_description(self, muscle_name):
        if self.is_compound:
            focus = (
                f"This is a compound movement that recruits multiple muscle groups around the "
                f"{muscle_name.lower()}, building overall strength and mass. Use controlled form "
                f"and a full range of motion."
            )
        else:
            focus = (
                f"This is an isolation movement that targets the {muscle_name.lower()} directly. "
                f"Focus on the mind-muscle connection, a slow negative, and avoid using momentum."
            )
        return f"{focus} Equipment: {self.get_equipment()}."


class MuscleSection:
    def __init__(self, muscle_name, exercises):
        self.muscle_name = muscle_name
        self.exercises = exercises


class WeekPlan:
    def __init__(self, week_number, sections):
        self.week_number = week_number
        self.sections = sections


class WorkoutDay:
    def __init__(self, day_name, focus, is_rest, weeks, body_highlight):
        self.day_name = day_name
        self.focus = focus
        self.is_rest = is_rest
        self.weeks = weeks
        # "chest_delts", "back_rear_delts", "arms_legs_core", "cardio", "rest"
        self.body_highlight = body_highlight
