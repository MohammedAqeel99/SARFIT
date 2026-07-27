from kivy.uix.boxlayout import BoxLayout
from kivy.uix.label import Label
from kivy.uix.scrollview import ScrollView
from kivy.uix.screenmanager import Screen

import colors
from body_model import BodyModel
from workout_data import get_schedule
from ui_common import BackgroundBox, Card, TapBox, TapLabel


class DayDetailScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.days = get_schedule()
        self.day = None
        self.selected_week = 0
        self._build()

    def _build(self):
        self.root = BackgroundBox(orientation="vertical", padding=16, spacing=8)

        header_row = BoxLayout(size_hint=(1, None), height=32)
        back_btn = TapLabel(text="\u2190 Back", color=colors.TEXT_SECONDARY, size_hint=(None, 1), width=70)
        back_btn.bind(on_release=lambda *_: setattr(self.manager, "current", "main"))
        header_row.add_widget(back_btn)
        self.root.add_widget(header_row)

        self.title_label = Label(text="", font_size=22, bold=True, color=colors.TEXT_PRIMARY,
                                  size_hint=(1, None), height=32, halign="left")
        self.title_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        self.root.add_widget(self.title_label)

        self.focus_label = Label(text="", font_size=14, color=colors.TEXT_SECONDARY,
                                  size_hint=(1, None), height=22, halign="left")
        self.focus_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        self.root.add_widget(self.focus_label)

        self.sets_reps_label = Label(
            text="Compound lifts: 4 sets x 6-10 reps, rest 2-3 min  |  Isolation: 3 sets x 10-15 reps, rest 60-90 sec",
            font_size=11, color=colors.TEXT_SECONDARY, size_hint=(1, None), height=34, halign="left",
        )
        self.sets_reps_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        self.root.add_widget(self.sets_reps_label)

        body_row = BoxLayout(size_hint=(1, None), height=260)
        self.body_model = BodyModel(size_hint=(1, 1))
        body_row.add_widget(self.body_model)
        self.root.add_widget(body_row)

        flip_row = BoxLayout(size_hint=(1, None), height=30, spacing=8)
        self.view_label = Label(text="Front", color=colors.TEXT_SECONDARY, size_hint=(None, 1), width=60)
        flip_link = TapLabel(text="Flip View \u21bb", color=colors.NEON_CYAN, size_hint=(None, 1), width=110)
        flip_link.bind(on_release=self._flip_view)
        flip_row.add_widget(self.view_label)
        flip_row.add_widget(flip_link)
        self.root.add_widget(flip_row)

        self.week_tabs = BoxLayout(size_hint=(1, None), height=44, spacing=6)
        self.root.add_widget(self.week_tabs)

        scroll = ScrollView(size_hint=(1, 1))
        self.exercise_list = BoxLayout(orientation="vertical", spacing=8, size_hint_y=None, padding=[0, 8, 0, 8])
        self.exercise_list.bind(minimum_height=self.exercise_list.setter("height"))
        scroll.add_widget(self.exercise_list)
        self.root.add_widget(scroll)

        self.add_widget(self.root)

    def load_day(self, day_index):
        self.day = self.days[day_index]
        self.selected_week = 0
        self.title_label.text = self.day.day_name
        self.focus_label.text = self.day.focus

        if self.day.weeks and self.day.weeks[0].sections:
            self.body_model.set_muscle_group(self.day.weeks[0].sections[0].muscle_name)
        self.view_label.text = "Back" if self.body_model.is_showing_back() else "Front"

        self._build_week_tabs()
        self._render_week(0)

    def _flip_view(self, *args):
        self.body_model.toggle_view()
        self.view_label.text = "Back" if self.body_model.is_showing_back() else "Front"

    def _build_week_tabs(self):
        self.week_tabs.clear_widgets()
        accent = colors.accent_for(self.day.body_highlight)
        for w_idx, week in enumerate(self.day.weeks):
            selected = w_idx == self.selected_week
            tab = TapLabel(
                text=f"Week {week.week_number}",
                bold=selected,
                color=(0, 0, 0, 1) if selected else colors.TEXT_PRIMARY,
            )
            with tab.canvas.before:
                from kivy.graphics import Color as GColor, Rectangle
                GColor(*(accent if selected else colors.BG_SURFACE))
                rect = Rectangle(size=tab.size, pos=tab.pos)
            tab.bind(size=lambda w, *_a, r=rect: (setattr(r, "size", w.size), setattr(r, "pos", w.pos)))
            tab.bind(pos=lambda w, *_a, r=rect: setattr(r, "pos", w.pos))
            tab.bind(on_release=lambda *_a, idx=w_idx: self._select_week(idx))
            self.week_tabs.add_widget(tab)

    def _select_week(self, week_idx):
        self.selected_week = week_idx
        self._build_week_tabs()
        self._render_week(week_idx)

    def _render_week(self, week_idx):
        self.exercise_list.clear_widgets()
        plan = self.day.weeks[week_idx]
        accent = colors.accent_for(self.day.body_highlight)

        for section in plan.sections:
            header = Label(
                text=section.muscle_name, font_size=16, bold=True, color=accent,
                size_hint=(1, None), height=28, halign="left",
            )
            header.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
            self.exercise_list.add_widget(header)

            for ex in section.exercises:
                self.exercise_list.add_widget(self._build_exercise_row(ex, section.muscle_name, accent))

    def _build_exercise_row(self, ex, muscle_name, accent):
        row = Card(orientation="vertical", padding=14, spacing=6, size_hint=(1, None), height=64)

        header_row = TapBox(orientation="horizontal", size_hint=(1, None), height=44, padding=0)
        name_col = BoxLayout(orientation="vertical")
        name_label = Label(text=ex.name, font_size=15, color=colors.TEXT_PRIMARY, halign="left",
                            size_hint=(1, None), height=20)
        name_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        details_label = Label(text=ex.get_sets_reps(), font_size=11, color=colors.TEXT_SECONDARY, halign="left",
                               size_hint=(1, None), height=16)
        details_label.bind(size=lambda w, *_: setattr(w, "text_size", w.size))
        name_col.add_widget(name_label)
        name_col.add_widget(details_label)
        header_row.add_widget(name_col)

        arrow = Label(text="\u25BE", color=accent, size_hint=(None, 1), width=24)
        header_row.add_widget(arrow)
        row.add_widget(header_row)

        expanded_info = Label(
            text=f"Equipment: {ex.get_equipment()}\n{ex.get_description(muscle_name)}",
            font_size=11.5, color=colors.hex_to_rgba("#CFCFCF"), halign="left", valign="top",
            size_hint=(1, None), height=0, opacity=0,
        )
        expanded_info.bind(width=lambda w, *_: setattr(w, "text_size", (w.width, None)))
        expanded_info.bind(texture_size=lambda w, *_: setattr(w, "height", w.texture_size[1] if w.opacity else 0))
        row.add_widget(expanded_info)

        watch_btn = TapLabel(
            text="\u25B6  Watch proper form", bold=True, color=colors.NEON_CYAN,
            size_hint=(1, None), height=0, opacity=0,
        )

        def open_video(*_a):
            video_screen = self.manager.get_screen("video")
            video_screen.load_exercise(ex.name)
            self.manager.current = "video"

        watch_btn.bind(on_release=open_video)
        row.add_widget(watch_btn)

        state = {"expanded": False}

        def toggle_expand(*_a):
            state["expanded"] = not state["expanded"]
            expanding = state["expanded"]
            expanded_info.opacity = 1 if expanding else 0
            expanded_info.height = expanded_info.texture_size[1] if expanding else 0
            watch_btn.opacity = 1 if expanding else 0
            watch_btn.height = 30 if expanding else 0
            row.height = 64 + (expanded_info.height if expanding else 0) + (30 if expanding else 0) + (12 if expanding else 0)
            self.body_model.set_muscle_group(muscle_name)
            self.view_label.text = "Back" if self.body_model.is_showing_back() else "Front"

        header_row.bind(on_release=toggle_expand)
        return row
