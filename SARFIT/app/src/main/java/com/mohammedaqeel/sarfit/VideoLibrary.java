package com.mohammedaqeel.sarfit;

import java.util.HashMap;
import java.util.Map;

/** Curated, verified YouTube video IDs for common lifts. Anything not listed here
 *  falls back to an in-app YouTube search (still inside the app's WebView). */
public class VideoLibrary {

    private static final Map<String, String> VIDEOS = new HashMap<>();
    static {
        VIDEOS.put("Bench Press", "vthMCtgVtFw");
        VIDEOS.put("Squat", "nEQQle9-0NA");
        VIDEOS.put("Deadlift", "WFUOtnI1jwk");
    }

    public static String idFor(String exerciseName) {
        return VIDEOS.get(exerciseName);
    }
}
