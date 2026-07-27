"""
SARFIT - Kivy port of the original Android app.
Developed by Mohammed Aqeel. Ported to Python/Kivy.

Run with:
    pip install -r requirements.txt
    python main.py

Package for Android with buildozer (see buildozer.spec / README.md).
"""

from kivy.config import Config

Config.set("graphics", "width", "390")
Config.set("graphics", "height", "760")

from kivy.app import App
from kivy.core.window import Window
from kivy.uix.screenmanager import NoTransition, ScreenManager

import colors
from screens.day_detail_screen import DayDetailScreen
from screens.login_screen import LoginScreen
from screens.main_screen import MainScreen
from screens.profile_screen import ProfileScreen
from screens.signup_screen import SignupScreen
from screens.video_screen import VideoScreen
from screens.warmup_screen import WarmupScreen


class SarfitApp(App):
    def build(self):
        self.title = "SARFIT"
        self.username = None
        Window.clearcolor = colors.BG_DARK

        sm = ScreenManager(transition=NoTransition())
        sm.add_widget(LoginScreen(name="login"))
        sm.add_widget(SignupScreen(name="signup"))
        sm.add_widget(MainScreen(name="main"))
        sm.add_widget(DayDetailScreen(name="day_detail"))
        sm.add_widget(WarmupScreen(name="warmup"))
        sm.add_widget(VideoScreen(name="video"))
        sm.add_widget(ProfileScreen(name="profile"))
        sm.current = "login"
        return sm


if __name__ == "__main__":
    SarfitApp().run()
