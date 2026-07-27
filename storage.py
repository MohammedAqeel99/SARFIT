"""
Local, on-device account + streak storage.

The original Android app used Firebase Auth/Firestore (a cloud backend).
Since this Kivy port has no server, accounts and streak/total data are
stored on-device instead, in the same spirit as the app's original
SharedPreferences design mentioned in its README ("real accounts stored
on-device, no internet needed"). Everything below is a drop-in, offline
replacement for StreakManager.java + the Firebase calls in
LoginActivity/SignupActivity/ProfileActivity.

Data lives in a JSON file under the app's user_data_dir
(~/.sarfit/sarfit_accounts.json on desktop).
"""

import hashlib
import json
import os
from datetime import date, timedelta

from kivy.app import App


def _store_path():
    app = App.get_running_app()
    base = app.user_data_dir if app else "."
    os.makedirs(base, exist_ok=True)
    return os.path.join(base, "sarfit_accounts.json")


def _load():
    path = _store_path()
    if not os.path.exists(path):
        return {"users": {}}
    try:
        with open(path, "r") as f:
            return json.load(f)
    except (json.JSONDecodeError, OSError):
        return {"users": {}}


def _save(data):
    with open(_store_path(), "w") as f:
        json.dump(data, f, indent=2)


def _hash_password(password):
    return hashlib.sha256(password.encode("utf-8")).hexdigest()


def _norm(username):
    return username.strip().lower()


def sign_up(username, password):
    """Returns (success: bool, message: str)."""
    if not username.strip() or not password:
        return False, "Please fill in both fields."
    if len(password) < 6:
        return False, "Password must be at least 6 characters."

    data = _load()
    key = _norm(username)
    if key in data["users"]:
        return False, "Sign up failed: username may already be taken."

    data["users"][key] = {
        "username": username.strip(),
        "password_hash": _hash_password(password),
        "streak": 0,
        "total": 0,
        "last_date": None,
        "profile_picture_path": None,
    }
    _save(data)
    return True, ""


def sign_in(username, password):
    """Returns (success: bool, display_name_or_message: str)."""
    if not username.strip() or not password:
        return False, "Please enter username and password."

    data = _load()
    key = _norm(username)
    user = data["users"].get(key)
    if not user or user["password_hash"] != _hash_password(password):
        return False, "Login failed: incorrect username/password, or no account exists."
    return True, user["username"]


def record_visit_and_fetch(username):
    """Updates the daily streak (once per day) and returns (streak, total)."""
    data = _load()
    key = _norm(username)
    user = data["users"].get(key)
    if not user:
        return 0, 0

    today = date.today().isoformat()
    last_date = user.get("last_date")

    if today == last_date:
        return user["streak"], user["total"]

    if last_date:
        yesterday = (date.today() - timedelta(days=1)).isoformat()
        user["streak"] = user["streak"] + 1 if yesterday == last_date else 1
    else:
        user["streak"] = 1

    user["total"] = user.get("total", 0) + 1
    user["last_date"] = today
    _save(data)
    return user["streak"], user["total"]


def fetch_only(username):
    """Fetches streak/total without modifying them (used by the Profile screen)."""
    data = _load()
    user = data["users"].get(_norm(username))
    if not user:
        return 0, 0
    return user.get("streak", 0), user.get("total", 0)


def set_profile_picture_path(username, path):
    data = _load()
    key = _norm(username)
    if key in data["users"]:
        data["users"][key]["profile_picture_path"] = path
        _save(data)


def get_profile_picture_path(username):
    data = _load()
    user = data["users"].get(_norm(username))
    return user.get("profile_picture_path") if user else None
