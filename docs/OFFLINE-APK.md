# Android offline wrapper integration

The game source supports a persistent, no-login offline profile when the bundled page is opened with the query parameter `offline=1`.

## Required WebView setup

- Load the bundled page as `file:///android_asset/index.html?offline=1` (or the equivalent path used by your wrapper).
- Enable DOM storage before loading the page: `webView.getSettings().setDomStorageEnabled(true)`.
- Keep the app's WebView data when updating the APK. Clearing app storage or uninstalling the app can erase local progress.
- Do not call `G.DB.playAsGuest()` for the offline profile. Guest sessions are intentionally in-memory only.
- Keep the normal website URL free of `offline=1`; the parameter is intended for the offline Android build only.

The game stores local profile metadata under `ps.accounts`, the active session under `ps.session`, and the persistent save under `ps.save.__pokesurvivor_offline__`.

## Verification checklist

1. Install and launch the offline APK; the game should continue past login to the existing profile flow.
2. Complete the personality test / choose a partner, then enter the menu.
3. Earn coins or make another persistent progression change, exit the app, and relaunch.
4. Confirm the chosen partner, coins, collection and upgrades remain.
5. Repeat with airplane mode enabled.

This repository contains the web-game source. The separate Android wrapper project and signing/build configuration are not included here, so these instructions must also be applied to that wrapper before producing a new APK.
