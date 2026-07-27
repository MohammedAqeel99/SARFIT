from kivy.uix.boxlayout import BoxLayout
from kivy.uix.label import Label
from kivy.uix.scrollview import ScrollView
from kivy.uix.screenmanager import Screen

import colors
from ui_common import BackgroundBox, Card, TapLabel

WARMUP_ITEMS = [
    ("Arm Circles", "20 forward + 20 backward"),
    ("Shoulder Rolls", "15-20 reps"),
    ("Band External Rotations", "2 sets x 15 reps"),
    ("Face Pulls (light)", "2 sets x 15 reps"),
    ("Scapular Push-Ups", "10-15 reps"),
]


class WarmupScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self._build()

    def _build(self):
        root = BackgroundBox(orientation="vertical", padding=24, spacing=8)

        root.add_widget(Label(
            text="Shoulder Warm-Up", font_size=26, bold=True, color=colors.NEON_GREEN,
            size_hint=(1, None), height=36, halign="left",
        ))
        subtitle = Label(
            text="Do this before any Chest, Back, or Shoulder day",
            font_size=13, color=colors.TEXT_SECONDARY, size_hint=(1, None), height=24, halign="left",
        )
        subtitle.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        root.add_widget(subtitle)

        scroll = ScrollView()
        items = BoxLayout(orientation="vertical", spacing=12, size_hint_y=None, padding=[0, 12, 0, 12])
        items.bind(minimum_height=items.setter("height"))
        for name, detail in WARMUP_ITEMS:
            card = Card(orientation="vertical", padding=16, spacing=2, size_hint=(1, None), height=64)
            name_label = Label(text=name, font_size=16, bold=True, color=colors.TEXT_PRIMARY,
                                size_hint=(1, None), height=22, halign="left")
            name_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
            detail_label = Label(text=detail, font_size=13, color=colors.TEXT_SECONDARY,
                                  size_hint=(1, None), height=18, halign="left")
            detail_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
            card.add_widget(name_label)
            card.add_widget(detail_label)
            items.add_widget(card)
        scroll.add_widget(items)
        root.add_widget(scroll)

        back_btn = TapLabel(
            text="Back", bold=True, color=colors.NEON_CYAN,
            size_hint=(1, None), height=48,
        )
        back_btn.bind(on_release=lambda *_: setattr(self.manager, "current", "main"))
        root.add_widget(back_btn)

        self.add_widget(root)
