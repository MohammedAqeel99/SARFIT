# SARFIT (Python / Kivy port)

Developed by Mohammed Aqeel. This is a full Python rewrite of the original
Android app (Java + Gradle) using [Kivy](https://kivy.org), so it can run as
an installable mobile app while being 100% Python.

## What's inside
- **Login / Signup** — accounts stored on-device in a local JSON file
  (`storage.py`), no internet needed.
- **Main screen** — the same 5-day split, streak tracking, warm-up shortcut.
- **Day detail screen** — Week 1-4 tabs, sets/reps rules, and an animated
  body-model diagram that pulses neon on the muscle group trained that day.
- **Warm-up** and **Profile** (avatar, streak, logout) screens.
- Dark background + neon accent theme (green/cyan/pink/gold per day),
  matching the original.

## File map (Java → Python)
| Original (Java)             | Python                              |
|------------------------------|--------------------------------------|
| `MainActivity.java`          | `screens/main_screen.py`             |
| `LoginActivity.java`         | `screens/login_screen.py`            |
| `SignupActivity.java`        | `screens/signup_screen.py`           |
| `DayDetailActivity.java`     | `screens/day_detail_screen.py`       |
| `WarmupActivity.java`        | `screens/warmup_screen.py`           |
| `VideoActivity.java`         | `screens/video_screen.py`            |
| `ProfileActivity.java`       | `screens/profile_screen.py`          |
| `BodyModelView.java`         | `body_model.py`                      |
| `WorkoutData.java`           | `workout_data.py`                    |
| `Exercise/MuscleSection/WeekPlan/WorkoutDay.java` | `models.py`     |
| `VideoLibrary.java`          | `video_library.py`                   |
| `StreakManager.java`         | `storage.py`                         |
| `colors.xml`                 | `colors.py`                          |
| `Fonts.java`                 | not needed — set a custom font via `kivy.config` / `LabelBase.register` if wanted |

## How to run on desktop (fastest way to try it)
```bash
pip install -r requirements.txt
python main.py
```

## How to package it as an installable Android app

### Option A — GitHub Actions (recommended, works from Termux/any phone)
This repo includes `.github/workflows/build.yml`, which builds the APK on
GitHub's free Ubuntu runners using `buildozer` — no build tools needed on
your own device:
1. Push this project to a GitHub repo (from Termux: `pkg install git`,
   then `git init && git add . && git commit -m "SARFIT" && git remote add origin <your-repo-url> && git push -u origin main`).
2. Go to the repo's **Actions** tab (or it'll run automatically on push) and
   wait for the "Build APK (buildozer)" workflow to finish (~10-15 min the
   first time, since it downloads the Android SDK/NDK).
3. Download the `SARFIT-debug-apk` artifact, transfer it to your phone, and
   install it (enable "install unknown apps" first).

### Option B — Buildozer directly on a desktop/laptop
Linux/WSL/macOS only; needs the Android SDK/NDK, which Buildozer downloads
automatically the first time:
```bash
pip install buildozer cython
buildozer -v android debug
```
The APK will be generated under `bin/`.

### Why not build directly in Termux?
`buildozer`/python-for-android needs a full Linux toolchain (specific
compilers, autotools, the Android NDK) that Termux's package set doesn't
cleanly support — it's a non-standard environment (no glibc by default,
different linker), so builds are fragile and often fail partway through.
It's technically possible via `proot-distro` (running a real Ubuntu chroot
inside Termux) but slow and easy to break on a phone. GitHub Actions (Option
A) gives you the same result reliably, using Termux only to push code and
pull down the finished APK.

## Notable differences from the Android original
- **Accounts/streak**: the original used Firebase Auth + Firestore (cloud).
  This port stores everything locally in a JSON file, closer to the app's
  original SharedPreferences design mentioned in its own README ("real
  accounts stored on-device, no internet needed"). If you want cloud sync
  back, swap `storage.py`'s functions for calls to a backend of your choice.
- **Watch proper form**: Android's WebView isn't available the same way in
  Kivy, so "Watch on YouTube" opens the same video in your system browser
  instead of an in-app player.
- **Body diagram**: redrawn using Kivy's `Tesselator`/`Mesh` instead of
  Android's `Canvas`/`Path`/`RadialGradient`, since those APIs don't exist in
  Kivy. The shapes and pulsing highlight logic are ported 1:1 from
  `BodyModelView.java`; the gradient look is simplified to a solid pulsing
  color.
- All workout data lives in `workout_data.py` — edit exercise names/weeks
  there if your plan changes, exactly like before.

## Requirements
- Python 3.9+
- Kivy 2.3+ (installed via `requirements.txt`)
- For Android packaging: Buildozer + Cython (Linux/macOS/WSL)
