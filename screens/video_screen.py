import webbrowser
from urllib.parse import quote

from kivy.uix.boxlayout import BoxLayout
from kivy.uix.button import Button
from kivy.uix.label import Label
from kivy.uix.screenmanager import Screen

import colors
import video_library
from ui_common import BackgroundBox, TapLabel


class VideoScreen(Screen):
    """
    Port of VideoActivity.java. Android's WebView isn't available the same
    way in a desktop/Kivy build, so this opens the same YouTube URL the
    original app would have loaded into its embedded WebView, in the
    system's default browser instead.
    """

    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.exercise_name = ""
        self.video_url = ""
        self._build()

    def _build(self):
        root = BackgroundBox(orientation="vertical", padding=24, spacing=16)

        top_row = BoxLayout(size_hint=(1, None), height=32)
        close_btn = TapLabel(text="\u2715 Close", color=colors.TEXT_SECONDARY, size_hint=(None, 1), width=80)
        close_btn.bind(on_release=lambda *_: setattr(self.manager, "current", "day_detail"))
        top_row.add_widget(close_btn)
        root.add_widget(top_row)

        self.title_label = Label(
            text="", font_size=22, bold=True, color=colors.TEXT_PRIMARY,
            size_hint=(1, None), height=40, halign="left",
        )
        self.title_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        root.add_widget(self.title_label)

        self.subtitle_label = Label(
            text="", font_size=13, color=colors.TEXT_SECONDARY,
            size_hint=(1, None), height=40, halign="left", valign="top",
        )
        self.subtitle_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        root.add_widget(self.subtitle_label)

        watch_btn = Button(
            text="\u25B6  Watch on YouTube",
            size_hint=(1, None), height=52,
            background_normal="", background_color=colors.NEON_CYAN,
            color=(0, 0, 0, 1), bold=True,
        )
        watch_btn.bind(on_release=lambda *_: webbrowser.open(self.video_url))
        root.add_widget(watch_btn)

        root.add_widget(BoxLayout())  # spacer
        self.add_widget(root)

    def load_exercise(self, exercise_name):
        self.exercise_name = exercise_name
        self.title_label.text = exercise_name

        video_id = video_library.id_for(exercise_name)
        if video_id:
            self.video_url = f"https://www.youtube.com/watch?v={video_id}"
            self.subtitle_label.text = "Curated demo video"
        else:
            query = quote(f"{exercise_name} proper form exercise")
            self.video_url = f"https://www.youtube.com/results?search_query={query}"
            self.subtitle_label.text = "No curated video yet \u2014 opens a YouTube search"
