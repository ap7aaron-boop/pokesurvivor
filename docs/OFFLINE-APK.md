# PokéSurvivor Offline Android integration

The Android wrapper lives in `android-wrapper/`. It bundles the production web game, opens it on the dedicated appassets origin with `?offline=1`, and enables DOM storage so the offline profile can persist across launches.

## Storage and mode selection

- The wrapper loads `https://appassets.androidplatform.net/assets/www/index.html?offline=1`.
- `WebViewAssetLoader` serves the APK-bundled files on a stable HTTPS origin; the wrapper does not enable arbitrary file-URL access.
- The game detects `offline=1` and skips Firebase initialization, even though the appassets origin uses HTTPS.
- WebView DOM storage is enabled. The local profile is keyed by `__pokesurvivor_offline__`.
- Do not call `G.DB.playAsGuest()` for this profile. Guest sessions are intentionally in-memory only.

The game stores profile metadata under `ps.accounts`, the active session under `ps.session`, and the persistent save under `ps.save.__pokesurvivor_offline__`.

## Verify the APK on a device

1. Install and launch the APK with airplane mode enabled.
2. Complete the personality test and choose a partner.
3. Make a persistent progression change (for example, spend/earn coins or change a setting saved by the game).
4. Force-stop the app and launch it again.
5. Confirm the selected partner, collection, coins and upgrades remain.

Clearing app data or uninstalling the app can erase local progress. Test the release APK on a physical Android device before distributing it.
