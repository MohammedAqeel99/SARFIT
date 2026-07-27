"""
Curated, verified YouTube video IDs for common lifts. Anything not listed here
falls back to a YouTube search opened in the browser.
Port of VideoLibrary.java.
"""

VIDEOS = {
    "Bench Press": "vthMCtgVtFw",
    "Squat": "nEQQle9-0NA",
    "Deadlift": "WFUOtnI1jwk",
}


def id_for(exercise_name):
    return VIDEOS.get(exercise_name)
