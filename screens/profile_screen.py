import os
import shutil

from kivy.app import App
from kivy.graphics import Color, Ellipse
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.filechooser import FileChooserListView
from kivy.uix.floatlayout import FloatLayout
from kivy.uix.image import Image
from kivy.uix.label import Label
from kivy.uix.popup import Popup
from kivy.uix.screenmanager import Screen

import colors
import storage
from ui_common import BackgroundBox, TapLabel, pulse_label


class ProfileScreen(Screen):
    def __init__(self, **kwargs):
        super().__init__(**kwargs)
        self.username = "Athlete"
        self._build()

    def _build(self):
        root = BackgroundBox(orientation="vertical", padding=28, spacing=14)

        back_row = BoxLayout(size_hint=(1, None), height=32)
        back_btn = TapLabel(text="\u2190 Back", color=colors.TEXT_SECONDARY, size_hint=(None, 1), width=70)
        back_btn.bind(on_release=lambda *_: setattr(self.manager, "current", "main"))
        back_row.add_widget(back_btn)
        root.add_widget(back_row)

        self.avatar_container = FloatLayout(size_hint=(None, None), size=(120, 120), pos_hint={"center_x": 0.5})
        with self.avatar_container.canvas.before:
            Color(*colors.BG_SURFACE)
            self._avatar_bg = Ellipse(size=self.avatar_container.size, pos=self.avatar_container.pos)
        self.avatar_container.bind(size=self._sync_avatar_bg, pos=self._sync_avatar_bg)

        self.avatar_letter = Label(text="A", font_size=48, bold=True, color=colors.NEON_GREEN)
        self.avatar_image = Image(size=self.avatar_container.size, allow_stretch=True, opacity=0)
        self.avatar_container.add_widget(self.avatar_letter)
        self.avatar_container.add_widget(self.avatar_image)
        pulse_label(self.avatar_letter, scale_min=0.85, scale_max=1.0)

        wrap_row = BoxLayout(size_hint=(1, None), height=120)
        wrap_row.add_widget(BoxLayout())
        wrap_row.add_widget(self.avatar_container)
        wrap_row.add_widget(BoxLayout())
        root.add_widget(wrap_row)
        self.avatar_container.bind(on_touch_down=self._maybe_pick_image)

        self.name_label = Label(text=self.username, font_size=22, bold=True, color=colors.TEXT_PRIMARY,
                                 size_hint=(1, None), height=32)
        root.add_widget(self.name_label)

        stats_row = BoxLayout(size_hint=(1, None), height=70, spacing=16)
        self.streak_label = self._stat_block(stats_row, "0", "Day Streak")
        self.total_label = self._stat_block(stats_row, "0", "Total Workouts")
        root.add_widget(stats_row)

        root.add_widget(BoxLayout())  # spacer

        logout_btn = TapLabel(
            text="Log Out", bold=True, color=colors.ERROR_RED,
            size_hint=(1, None), height=48,
        )
        logout_btn.bind(on_release=self._logout)
        root.add_widget(logout_btn)

        self.add_widget(root)

    def _stat_block(self, parent, number, caption):
        block = BoxLayout(orientation="vertical")
        num_label = Label(text=number, font_size=24, bold=True, color=colors.NEON_GREEN)
        cap_label = Label(text=caption, font_size=11, color=colors.TEXT_SECONDARY)
        block.add_widget(num_label)
        block.add_widget(cap_label)
        parent.add_widget(block)
        return num_label

    def _sync_avatar_bg(self, *args):
        self._avatar_bg.size = self.avatar_container.size
        self._avatar_bg.pos = self.avatar_container.pos
        self.avatar_image.size = self.avatar_container.size
        self.avatar_image.pos = self.avatar_container.pos

    def _maybe_pick_image(self, widget, touch):
        if not widget.collide_point(*touch.pos):
            return False
        self._open_file_chooser()
        return True

    def _open_file_chooser(self):
        chooser = FileChooserListView(filters=["*.png", "*.jpg", "*.jpeg"])
        popup = Popup(title="Choose a profile picture", content=chooser, size_hint=(0.9, 0.9))

        def on_submit(instance, selection, touch=None):
            if selection:
                self._set_profile_picture(selection[0])
            popup.dismiss()

        chooser.bind(on_submit=on_submit)
        popup.open()

    def _set_profile_picture(self, source_path):
        app = App.get_running_app()
        dest_dir = os.path.join(app.user_data_dir, "profile_pictures")
        os.makedirs(dest_dir, exist_ok=True)
        dest_path = os.path.join(dest_dir, f"{self.username}{os.path.splitext(source_path)[1]}")
        try:
            shutil.copyfile(source_path, dest_path)
        except OSError:
            return
        storage.set_profile_picture_path(self.username, dest_path)
        self._show_picture(dest_path)

    def _show_picture(self, path):
        self.avatar_image.source = path
        self.avatar_image.reload()
        self.avatar_image.opacity = 1
        self.avatar_letter.opacity = 0

    def _logout(self, *args):
        app = App.get_running_app()
        app.username = None
        self.manager.current = "login"

    def on_pre_enter(self, *args):
        app = App.get_running_app()
        self.username = getattr(app, "username", None) or "Athlete"
        self.name_label.text = self.username
        self.avatar_letter.text = self.username[0].upper()

        streak, total = storage.fetch_only(self.username)
        self.streak_label.text = str(streak)
        self.total_label.text = str(total)

        pic_path = storage.get_profile_picture_path(self.username)
        if pic_path and os.path.exists(pic_path):
            self._show_picture(pic_path)
        else:
            self.avatar_image.opacity = 0
            self.avatar_letter.opacity = 1
