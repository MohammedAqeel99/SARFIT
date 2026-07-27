8from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.label import Label
from kivy.uix.scrollview import ScrollView
from kivy.uix.screenmanager import Screen

import colors
import storage
from workout_data import get_schedule
from ui_common import BackgroundBox, TapCard, TapLabel


class MainScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.days = get_schedule()
        self._build()

    def _build(self):
        root = BackgroundBox(orientation="vertical", padding=20, spacing=10)

        top_row = BoxLayout(size_hint=(1, None), height=40, spacing=10)
        self.welcome_label = Label(
            text="Welcome back, Athlete!",
            font_size=18,
            bold=True,
            color=colors.TEXT_PRIMARY,
            halign="left",
            valign="middle",
        )
        self.welcome_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        top_row.add_widget(self.welcome_label)

        you_btn = TapLabel(text="Me", color=colors.NEON_CYAN, bold=True, size_hint=(None, 1), width=50)
        you_btn.bind(on_release=self._open_profile)
        top_row.add_widget(you_btn)
        root.add_widget(top_row)

        warmup_btn = TapLabel(
            text="Shoulder Warm-Up  \u2192",
            color=colors.NEON_GREEN,
            font_size=14,
            size_hint=(1, None),
            height=32,
            halign="left",
        )
        warmup_btn.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        warmup_btn.bind(on_release=self._open_warmup)
        root.add_widget(warmup_btn)

        scroll = ScrollView(size_hint=(1, 1))
        self.day_list = BoxLayout(orientation="vertical", spacing=10, size_hint_y=None, padding=[0, 6, 0, 6])
        self.day_list.bind(minimum_height=self.day_list.setter("height"))
        for idx, day in enumerate(self.days):
            self.day_list.add_widget(self._build_day_card(day, idx))
        scroll.add_widget(self.day_list)
        root.add_widget(scroll)

        self.add_widget(root)

    def _build_day_card(self, day, index):
        card = TapCard(orientation="vertical", padding=16, spacing=4, size_hint=(1, None), height=78)
        card.add_widget(Label(
            text=day.day_name, font_size=18, bold=True, color=colors.TEXT_PRIMARY,
            halign="left", size_hint=(1, None), height=24,
        ))
        accent = colors.hex_to_rgba("#777777") if day.is_rest else colors.accent_for(day.body_highlight)
        card.add_widget(Label(
            text=day.focus, font_size=13, color=accent,
            halign="left", size_hint=(1, None), height=20,
        ))
        for child in card.children:
            child.bind(size=lambda w, *_: setattr(w, "text_size", w.size))

        if not day.is_rest:
            card.bind(on_release=lambda *_: self._open_day(index))
        return card

    def _open_day(self, index):
        detail = self.manager.get_screen("day_detail")
        detail.load_day(index)
        self.manager.current = "day_detail"

    def _open_profile(self, *args):
        self.manager.current = "profile"

    def _open_warmup(self, *args):
        self.manager.current = "warmup"

    def on_pre_enter(self, *args):
        app = App.get_running_app()
        username = getattr(app, "username", None) or "Athlete"
        self.welcome_label.text = f"Welcome back, {username}!"
        if username != "Athlete":
            storage.record_visit_and_fetch(username)
