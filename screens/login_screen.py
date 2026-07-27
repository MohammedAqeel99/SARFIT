from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.screenmanager import Screen
from kivy.uix.textinput import TextInput

import colors
import storage
from ui_common import BackgroundBox, TapLabel, pulse_label


class LoginScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self._build()

    def _build(self):
        root = BackgroundBox(orientation="vertical", padding=32, spacing=14)

        title = Label(
            text="SARFIT",
            font_size=42,
            bold=True,
            color=colors.NEON_GREEN,
            size_hint=(1, None),
            height=70,
        )
        pulse_label(title)
        root.add_widget(title)

        root.add_widget(Label(
            text="Train Different.",
            font_size=14,
            color=colors.TEXT_SECONDARY,
            size_hint=(1, None),
            height=24,
        ))

        root.add_widget(BoxLayout(size_hint=(1, None), height=20))

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

        pass_row = BoxLayout(size_hint=(1, None), height=48, spacing=6)
        self.password_input = TextInput(
            hint_text="Password",
            password=True,
            multiline=False,
            background_color=colors.BG_SURFACE,
            foreground_color=colors.TEXT_PRIMARY,
            cursor_color=colors.NEON_GREEN,
            padding=[12, 12, 12, 12],
        )
        self._pw_visible = False
        toggle = TapLabel(text="\U0001F441", size_hint=(None, 1), width=44, color=colors.TEXT_SECONDARY)
        toggle.bind(on_release=self._toggle_password)
        pass_row.add_widget(self.password_input)
        pass_row.add_widget(toggle)
        root.add_widget(pass_row)

        self.error_label = Label(
            text="",
            color=colors.ERROR_RED,
            font_size=13,
            size_hint=(1, None),
            height=24,
        )
        root.add_widget(self.error_label)

        login_btn = Button(
            text="Log In",
            size_hint=(1, None),
            height=50,
            background_normal="",
            background_color=colors.NEON_GREEN,
            color=(0, 0, 0, 1),
            bold=True,
        )
        login_btn.bind(on_release=self._attempt_login)
        root.add_widget(login_btn)

        go_signup = TapLabel(
            text="Don't have an account? Sign up",
            color=colors.NEON_CYAN,
            font_size=13,
            size_hint=(1, None),
            height=30,
        )
        go_signup.bind(on_release=self._go_signup)
        root.add_widget(go_signup)

        root.add_widget(BoxLayout())  # spacer
        self.add_widget(root)

    def _toggle_password(self, *args):
        self._pw_visible = not self._pw_visible
        self.password_input.password = not self._pw_visible

    def _attempt_login(self, *args):
        username = self.username_input.text.strip()
        password = self.password_input.text
        success, result = storage.sign_in(username, password)
        if success:
            self.error_label.text = ""
            app = App.get_running_app()
            app.username = result
            self.manager.current = "main"
        else:
            self.error_label.text = result

    def _go_signup(self, *args):
        self.manager.current = "signup"

    def on_pre_enter(self, *args):
        # Auto-login stand-in: if the app already has a signed-in username
        # (e.g. returning to Login via back-navigation), skip straight to Main.
        app = App.get_running_app()
        if getattr(app, "username", None):
            self.manager.current = "main"
