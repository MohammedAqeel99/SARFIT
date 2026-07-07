package com.mohammedaqeel.sarfit;

public class Exercise {
    public String name;
    public boolean isCompound;
    public String customText;

    public Exercise(String name, boolean isCompound) {
        this.name = name;
        this.isCompound = isCompound;
        this.customText = null;
    }

    public Exercise(String name, String customText) {
        this.name = name;
        this.isCompound = false;
        this.customText = customText;
    }

    public String getSetsReps() {
        if (customText != null) {
            return customText;
        }
        if (isCompound) {
            return "4 sets x 6-10 reps  |  Rest 2-3 min";
        } else {
            return "3 sets x 10-15 reps  |  Rest 60-90 sec";
        }
    }

    public String getEquipment() {
        String n = name.toLowerCase();
        if (n.contains("barbell") || n.contains("deadlift") || n.contains("rack pull") || n.contains("good morning")) return "Barbell";
        if (n.contains("ez bar")) return "EZ Bar";
        if (n.contains("smith")) return "Smith Machine";
        if (n.contains("cable") || n.contains("pushdown") || n.contains("pulldown") || n.contains("crossover") || n.contains("woodchop")) return "Cable Machine";
        if (n.contains("machine") || n.contains("pec deck") || n.contains("hack squat") || n.contains("leg press") || n.contains("leg extension") || n.contains("leg curl")) return "Machine";
        if (n.contains("kettlebell")) return "Kettlebell";
        if (n.contains("band")) return "Resistance Band";
        if (n.contains("db ") || n.contains("dumbbell") || n.contains("arnold") || n.contains("goblet")) return "Dumbbells";
        if (n.contains("dip") || n.contains("pull-up") || n.contains("pullup") || n.contains("chin-up") || n.contains("push-up") || n.contains("pushup") || n.contains("plank") || n.contains("hollow") || n.contains("sit-up") || n.contains("situp") || n.contains("hanging") || n.contains("dragon flag") || n.contains("nordic")) return "Bodyweight";
        if (n.contains("landmine")) return "Landmine + Barbell";
        if (n.contains("plate")) return "Weight Plate";
        if (n.contains("rope")) return "Cable Rope Attachment";
        return "Gym Equipment";
    }

    public String getDescription(String muscleName) {
        String type = isCompound ? "compound" : "isolation";
        String focus = isCompound
                ? "This is a compound movement that recruits multiple muscle groups around the " + muscleName.toLowerCase() + ", building overall strength and mass. Use controlled form and a full range of motion."
                : "This is an isolation movement that targets the " + muscleName.toLowerCase() + " directly. Focus on the mind-muscle connection, a slow negative, and avoid using momentum.";
        return focus + " Equipment: " + getEquipment() + ".";
    }
}
