[app]
title = SARFIT
package.name = sarfit
package.domain = com.mohammedaqeel
source.dir = .
source.include_exts = py,png,jpg,kv,atlas,ttf
version = 1.0
requirements = python3,kivy
orientation = portrait
fullscreen = 0

android.permissions = READ_EXTERNAL_STORAGE,READ_MEDIA_IMAGES

# Pin the Android API / build-tools / NDK so the CI build is reproducible
# and doesn't silently pull a brand-new, less-tested API level.
android.api = 34
android.minapi = 21
android.ndk = 25b
android.build_tools = 34.0.0
android.accept_sdk_license = True
android.archs = arm64-v8a

[buildozer]
log_level = 2
warn_on_root = 1
