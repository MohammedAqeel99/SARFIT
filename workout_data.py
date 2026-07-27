"""
Direct Python port of WorkoutData.java.
Edit exercise names/weeks here if the plan changes.
"""

from models import Exercise, MuscleSection, WeekPlan, WorkoutDay


def c(name):
    return Exercise(name, is_compound=True)


def i(name):
    return Exercise(name, is_compound=False)


def cardio(name, text):
    return Exercise(name, custom_text=text)


def get_schedule():
    days = []

    # ---------------- Sunday: Cardio ----------------
    cardio_plan = [
        WeekPlan(1, [
            MuscleSection("Cardio", [
                cardio("Steady-State Cycling/Jog", "30 min, moderate intensity (Zone 2)"),
                cardio("Core Finisher: Plank Circuit", "3 rounds x 45 sec plank + 15 sec rest"),
            ]),
        ]),
        WeekPlan(2, [
            MuscleSection("Cardio", [
                cardio("HIIT Intervals", "20 min: 30 sec sprint / 90 sec walk x 10"),
                cardio("Mobility & Stretching", "15 min full-body dynamic stretch"),
            ]),
        ]),
        WeekPlan(3, [
            MuscleSection("Cardio", [
                cardio("Incline Treadmill Walk", "35 min, incline 8-12%, brisk pace"),
                cardio("Core Finisher: Circuit", "3 rounds x 20 leg raises + 20 Russian twists"),
            ]),
        ]),
        WeekPlan(4, [
            MuscleSection("Cardio", [
                cardio("Active Recovery: Light Cycling", "25 min, easy pace"),
                cardio("Full Body Stretch & Foam Roll", "15-20 min recovery session"),
            ]),
        ]),
    ]
    days.append(WorkoutDay("Sunday", "Cardio", False, cardio_plan, "cardio"))

    # ---------------- Monday: Chest + Side Delts + Front Delts ----------------
    mon_push = [
        WeekPlan(1, [
            MuscleSection("Chest", [c("Bench Press"), c("Incline DB Press"), i("Cable Fly"), c("Dips")]),
            MuscleSection("Side Delts", [i("Cable Lateral Raise"), i("DB Lateral Raise")]),
            MuscleSection("Front Delts", [c("Overhead Press"), i("Front Raise")]),
        ]),
        WeekPlan(2, [
            MuscleSection("Chest", [c("Smith Machine Bench"), c("Incline Machine Press"), i("Low-to-High Fly"), c("Decline Pushups")]),
            MuscleSection("Side Delts", [i("Leaning Cable Lateral Raise"), i("Egyptian Lateral Raise")]),
            MuscleSection("Front Delts", [c("Seated DB Press"), i("Barbell Front Raise")]),
        ]),
        WeekPlan(3, [
            MuscleSection("Chest", [c("Floor Press"), c("Cable Incline Press"), i("Svend Press"), c("Weighted Dips")]),
            MuscleSection("Side Delts", [i("Behind-Back Cable Raise"), i("Bottoms-Up Lateral Raise")]),
            MuscleSection("Front Delts", [c("Push Press"), i("Plate Raise")]),
        ]),
        WeekPlan(4, [
            MuscleSection("Chest", [c("Spoto Press"), c("Neutral Grip DB Press"), i("High-to-Low Fly"), c("Ring Dips")]),
            MuscleSection("Side Delts", [i("21s Lateral Raise"), i("Cable Lean-Away Raise")]),
            MuscleSection("Front Delts", [c("Viking Press"), i("Band Front Raise")]),
        ]),
    ]
    days.append(WorkoutDay("Monday", "Chest + Side Delts + Front Delts", False, mon_push, "chest_delts"))

    # ---------------- Tuesday: Back + Rear Delts ----------------
    tue_pull = [
        WeekPlan(1, [
            MuscleSection("Back", [c("Deadlift"), c("Lat Pulldown"), c("Seated Row"), c("1-Arm DB Row")]),
            MuscleSection("Rear Delts", [i("Rear Delt Fly"), i("Face Pull")]),
        ]),
        WeekPlan(2, [
            MuscleSection("Back", [c("Barbell Row"), c("Close Grip Pulldown"), c("Machine Row"), i("Pullover")]),
            MuscleSection("Rear Delts", [i("Cable Rear Delt Fly"), i("Face Pull Rope")]),
        ]),
        WeekPlan(3, [
            MuscleSection("Back", [c("Snatch Grip Deadlift"), c("V-Bar Pulldown"), i("Single Arm Cable Row"), c("Landmine Row")]),
            MuscleSection("Rear Delts", [i("Bent-Over Rear Delt Raise"), i("Cross Cable Rear Delt Fly")]),
        ]),
        WeekPlan(4, [
            MuscleSection("Back", [c("Trap Bar Deadlift"), c("Neutral Grip Pulldown"), c("Chest Supported DB Row"), i("Renegade Row")]),
            MuscleSection("Rear Delts", [i("Prone Rear Delt Raise"), i("Cable Reverse Fly")]),
        ]),
    ]
    days.append(WorkoutDay("Tuesday", "Back + Rear Delts", False, tue_pull, "back_rear_delts"))

    # ---------------- Wednesday: Biceps + Triceps + Legs + Core ----------------
    wed_arms_legs = [
        WeekPlan(1, [
            MuscleSection("Biceps", [i("Barbell Curl"), i("Incline DB Curl"), i("Hammer Curl")]),
            MuscleSection("Triceps", [i("Pushdown"), i("Overhead DB Extension"), i("Skull Crushers")]),
            MuscleSection("Legs", [c("Squat"), c("Romanian Deadlift"), c("Leg Press")]),
            MuscleSection("Core", [i("Hanging Leg Raise"), i("Cable Crunch")]),
        ]),
        WeekPlan(2, [
            MuscleSection("Biceps", [i("Concentration Curl"), i("Cable Curl"), i("Cross Body Hammer Curl")]),
            MuscleSection("Triceps", [i("V-Bar Pushdown"), i("EZ Bar Skull Crushers"), i("Bench Dips")]),
            MuscleSection("Legs", [c("Hack Squat"), c("Bulgarian Split Squat"), i("Leg Curl")]),
            MuscleSection("Core", [i("Russian Twist"), i("Ab Wheel Rollout")]),
        ]),
        WeekPlan(3, [
            MuscleSection("Biceps", [i("Drag Curl"), i("Zottman Curl"), i("Cable Rope Curl")]),
            MuscleSection("Triceps", [c("JM Press"), i("Kickbacks"), i("Tate Press")]),
            MuscleSection("Legs", [c("Zercher Squat"), c("Step-Ups"), i("Sissy Squat")]),
            MuscleSection("Core", [cardio("Weighted Plank", "3 sets x 45-60 sec hold"), i("Woodchopper")]),
        ]),
        WeekPlan(4, [
            MuscleSection("Biceps", [c("Chin-Up (Curl Focus)"), i("Bayesian Curl"), i("Rope Hammer Curl 21s")]),
            MuscleSection("Triceps", [c("Close Grip Board Press"), i("Overhead Rope Extension"), c("Machine Dip")]),
            MuscleSection("Legs", [c("Belt Squat"), c("Reverse Lunge"), i("Glute Ham Raise")]),
            MuscleSection("Core", [i("Hanging Windshield Wipers"), i("Cable Woodchop")]),
        ]),
    ]
    days.append(WorkoutDay("Wednesday", "Biceps + Triceps + Legs + Core", False, wed_arms_legs, "arms_legs_core"))

    # ---------------- Thursday: Chest + Side Delts + Front Delts ----------------
    thu_push = [
        WeekPlan(1, [
            MuscleSection("Chest", [c("Incline Barbell Press"), c("Flat DB Press"), i("Pec Deck"), c("Machine Press")]),
            MuscleSection("Side Delts", [i("Machine Lateral Raise"), i("Cable Y-Raise")]),
            MuscleSection("Front Delts", [c("Arnold Press"), i("Plate Front Raise")]),
        ]),
        WeekPlan(2, [
            MuscleSection("Chest", [c("DB Bench Press"), c("Incline Smith Press"), i("DB Fly"), c("Chest Press Machine")]),
            MuscleSection("Side Delts", [i("Lu Raises"), i("Partial Lateral Raise")]),
            MuscleSection("Front Delts", [c("Landmine Press"), i("Cable Front Raise")]),
        ]),
        WeekPlan(3, [
            MuscleSection("Chest", [c("Guillotine Press"), c("Reverse Grip Bench"), i("Cable Crossover"), i("Machine Fly")]),
            MuscleSection("Side Delts", [i("Single-Arm Cable Lateral"), i("Kettlebell Lateral Raise")]),
            MuscleSection("Front Delts", [c("Z Press"), i("DB Front Raise")]),
        ]),
        WeekPlan(4, [
            MuscleSection("Chest", [c("Board Press"), c("Cybex Press"), i("Standing Cable Fly"), c("Chest Dips")]),
            MuscleSection("Side Delts", [i("Step-Up Lateral Raise"), i("Double Cable Lateral")]),
            MuscleSection("Front Delts", [c("Bradford Press"), i("Incline Front Raise")]),
        ]),
    ]
    days.append(WorkoutDay("Thursday", "Chest + Side Delts + Front Delts", False, thu_push, "chest_delts"))

    # ---------------- Friday: Back + Rear Delts ----------------
    fri_pull = [
        WeekPlan(1, [
            MuscleSection("Back", [c("Rack Pull"), c("Pull-ups"), c("T-Bar Row"), i("Straight Arm Pulldown")]),
            MuscleSection("Rear Delts", [i("Reverse Pec Deck"), i("Band Pull-Apart")]),
        ]),
        WeekPlan(2, [
            MuscleSection("Back", [c("Meadows Row"), c("Wide Grip Pulldown"), c("Chest Supported Row"), c("Seal Row")]),
            MuscleSection("Rear Delts", [i("Incline Rear Delt Raise"), i("Rope Face Pull")]),
        ]),
        WeekPlan(3, [
            MuscleSection("Back", [c("Deficit Deadlift"), c("Kneeling Pulldown"), c("Yates Row"), i("Dumbbell Pullover")]),
            MuscleSection("Rear Delts", [i("Machine Rear Delt Fly"), i("Scapular Pull-ups")]),
        ]),
        WeekPlan(4, [
            MuscleSection("Back", [c("Pendlay Row"), c("Cross-Body Lat Pulldown"), c("Inverted Row"), c("Good Morning")]),
            MuscleSection("Rear Delts", [i("High Pulley Rear Delt Fly"), i("Band Reverse Fly")]),
        ]),
    ]
    days.append(WorkoutDay("Friday", "Back + Rear Delts", False, fri_pull, "back_rear_delts"))

    # ---------------- Saturday: Biceps + Triceps + Legs + Core ----------------
    sat_arms_legs = [
        WeekPlan(1, [
            MuscleSection("Biceps", [i("EZ Bar Curl"), i("Preacher Curl"), i("Rope Hammer Curl")]),
            MuscleSection("Triceps", [i("Rope Pushdown"), c("Close Grip Bench Press"), i("Single Arm Cable Extension")]),
            MuscleSection("Legs", [c("Front Squat"), c("Walking Lunges"), i("Leg Extension")]),
            MuscleSection("Core", [i("Decline Situps"), cardio("Plank", "3 sets x 45-60 sec hold")]),
        ]),
        WeekPlan(2, [
            MuscleSection("Biceps", [i("Standing DB Curl"), i("Spider Curl"), i("Reverse Curl")]),
            MuscleSection("Triceps", [i("Reverse Grip Pushdown"), i("Rope Overhead Extension"), i("Diamond Pushups")]),
            MuscleSection("Legs", [c("Smith Machine Squat"), c("Goblet Squat"), c("Stiff Leg Deadlift")]),
            MuscleSection("Core", [i("Toe Touches"), i("Bicycle Crunch")]),
        ]),
        WeekPlan(3, [
            MuscleSection("Biceps", [i("21s Barbell Curl"), i("Wide Grip Curl"), i("Waiter Curl")]),
            MuscleSection("Triceps", [i("Floor Press Extension"), i("French Press"), c("Dip Machine")]),
            MuscleSection("Legs", [c("Pendulum Squat"), c("Curtsy Lunge"), i("Nordic Curl")]),
            MuscleSection("Core", [cardio("Dragon Flag", "3 sets x 6-10 reps"), cardio("Side Plank", "3 sets x 30-45 sec each side")]),
        ]),
        WeekPlan(4, [
            MuscleSection("Biceps", [i("Cheat Curl"), i("Cross-Body Curl"), i("Fat Grip Curl")]),
            MuscleSection("Triceps", [i("Skull Crusher to Press"), i("Single Arm Overhead Extension"), c("Weighted Dips")]),
            MuscleSection("Legs", [c("Safety Bar Squat"), c("Deficit Reverse Lunge"), i("Copenhagen Plank")]),
            MuscleSection("Core", [i("Pallof Press"), cardio("Hollow Body Hold", "3 sets x 30-45 sec")]),
        ]),
    ]
    days.append(WorkoutDay("Saturday", "Biceps + Triceps + Legs + Core", False, sat_arms_legs, "arms_legs_core"))

    return days
