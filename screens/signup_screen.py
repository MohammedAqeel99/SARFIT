from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.screenmanager import Screen
from kivy.uix.textinput import TextInput

import colors
import storage
from ui_common import BackgroundBox, TapLabel


class SignupScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self._build()

    def _build(self):
        root = BackgroundBox(orientation="vertical", padding=32, spacing=14)

        root.add_widget(Label(
            text="Create Account",
            font_size=28,
            bold=True,
            color=colors.NEON_GREEN,
            size_hint=(1, None),
            height=50,
        ))
        root.add_widget(BoxLayout(size_hint=(1, None), height=10))

        self.username_input = TextInput(
            hint_text="Username",
            multiline=False,
            size_hint=(1, None),
            height=48,
            background_color=colors.BG_SURFACE,
            foreground_color=colors.TEXT_PRIMARY,
            cursor_color=colors.NEON_GREEN,
            padding=[12, 12, 12, 12],
        )
        root.add_widget(self.username_input)

        self.password_input = TextInput(
            hint_text="Password (min 6 characters)",
            password=True,
            multiline=False,
            size_hint=(1, None),
            height=48,
            background_color=colors.BG_SURFACE,
            foreground_color=colors.TEXT_PRIMARY,
            cursor_color=colors.NEON_GREEN,
            padding=[12, 12, 12, 12],
        )
        root.add_widget(self.password_input)

        self.error_label = Label(
            text="",
            color=colors.ERROR_RED,
            font_size=13,
            size_hint=(1, None),
            height=24,
        )
        root.add_widget(self.error_label)

        signup_btn = Button(
            text="Sign Up",
            size_hint=(1, None),
            height=50,
            background_normal="",
            background_color=colors.NEON_GREEN,
            color=(0, 0, 0, 1),
            bold=True,
        )
        signup_btn.bind(on_release=self._attempt_signup)
        root.add_widget(signup_btn)

        back_link = TapLabel(
            text="Back to Login",
            color=colors.NEON_CYAN,
            font_size=13,
            size_hint=(1, None),
            height=30,
        )
        back_link.bind(on_release=lambda *_: setattr(self.manager, "current", "login"))
        root.add_widget(back_link)

        root.add_widget(BoxLayout())
        self.add_widget(root)

    def _attempt_signup(self, *args):
        username = self.username_input.text.strip()
        password = self.password_input.text
        self.error_label.text = "Creating account..."
        success, message = storage.sign_up(username, password)
        if success:
            self.error_label.text = ""
            self.manager.current = "login"  # back to Login to sign in, matches SignupActivity.finish()
        else:
            self.error_label.text = message
