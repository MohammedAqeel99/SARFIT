# SARFIT
Developed by MOHAMMED AQEEL
Package: com.mohammedaqeel.sarfit

## What's inside
- **Login / Signup** — real accounts stored on-device (SharedPreferences), no internet needed.
- **Main screen** — your 5-day split: Friday Chest+Triceps, Saturday Back+Biceps, Sunday Rest,
  Monday Legs, Tuesday Shoulders+Abs.
- **Day detail screen** — Week 1-4 tabs (the exact rotating variations from your plan), sets/reps/rest
  rules, and an animated human-body model that pulses neon on the muscle group trained that day.
- Dark background + neon accent theme (green/cyan/pink/gold per day).

## How to open it

### Option A — AIDE (on your phone)
1. Unzip `SARFIT.zip` somewhere on your device (e.g. with a file manager / zip app).
2. In AIDE: **Open Folder / Open Project** → select the extracted `SARFIT` folder (the one
   containing `settings.gradle`).
3. AIDE should detect it as a Gradle Android project and sync automatically.
4. Once synced: **Run ▶** to install straight to your device, or use AIDE's **Build APK** option
   to generate the `.apk` file for sharing/installing elsewhere.

### Option B — Android Studio (recommended if available)
1. Unzip and choose **Open** → select the `SARFIT` folder.
2. Let Gradle sync (it will auto-download the wrapper jar the first time — needs internet once).
3. **Build > Build Bundle(s)/APK(s) > Build APK(s)**.

## Notes
- No external libraries beyond `androidx.appcompat` — keeps the build lightweight and easy to sync
  on mobile.
- All workout data lives in one file: `WorkoutData.java` — edit exercise names/weeks there if your
  plan changes.
- The body-model animation is drawn in code (`BodyModelView.java`), not an image asset, so it's
  lightweight and easy to recolor per day.
