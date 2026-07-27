"""
Theme colors, port of res/values/colors.xml.
Kivy colors are RGBA tuples in the 0-1 range.
"""


def hex_to_rgba(hex_color, alpha=1.0):
    hex_color = hex_color.lstrip("#")
    r = int(hex_color[0:2], 16) / 255.0
    g = int(hex_color[2:4], 16) / 255.0
    b = int(hex_color[4:6], 16) / 255.0
    return (r, g, b, alpha)


BG_DARK = hex_to_rgba("#0A0A0A")
BG_SURFACE = hex_to_rgba("#1A1A1E")
BG_CARD = hex_to_rgba("#151518")
NEON_GREEN = hex_to_rgba("#39FF14")
NEON_CYAN = hex_to_rgba("#00F0FF")
NEON_PINK = hex_to_rgba("#FF2E9F")
NEON_GOLD = hex_to_rgba("#FFD700")
TEXT_PRIMARY = hex_to_rgba("#FFFFFF")
TEXT_SECONDARY = hex_to_rgba("#9E9E9E")
REST_GREY = hex_to_rgba("#3A3A3A")
ERROR_RED = hex_to_rgba("#FF4C4C")

SKIN_BASE = hex_to_rgba("#3A3A42")
SKIN_LIGHT = hex_to_rgba("#55555E")


def accent_for(group):
    """Port of accentFor() used in MainActivity / DayDetailActivity."""
    return {
        "chest_delts": NEON_GREEN,
        "back_rear_delts": NEON_CYAN,
        "arms_legs_core": NEON_PINK,
        "cardio": NEON_GOLD,
    }.get(group, hex_to_rgba("#888888"))
