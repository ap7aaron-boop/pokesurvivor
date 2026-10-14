# PokéSurvivor Offline — Android wrapper

This is a native Android WebView shell for the offline PokéSurvivor game. The app bundles the web game into the APK at build time, launches it with `?offline=1`, and enables DOM storage for persistent local saves. The Android app intentionally does not request the INTERNET permission.

## Build locally

Requirements: JDK 17, Android SDK Platform 35, Android Build Tools, and Gradle 8.13.

From the repository root:

```bash
cd android-wrapper
gradle assembleDebug
```

The build copies `index.html`, `js/`, `css/`, and `assets/` from the repository root into the APK. Debug output:

`app/build/outputs/apk/debug/app-debug.apk`

The debug variant has application ID `com.naarodev.pokesurvivor.debug`. The release variant uses `com.naarodev.pokesurvivor`.

## Create a release signing key

Create a private upload/release key on a trusted machine. Keep the keystore and passwords private; never commit them to Git.

```bash
keytool -genkeypair -v -keystore pokesurvivor-release.jks -alias pokesurvivor -keyalg RSA -keysize 4096 -validity 10000
```

Set these environment variables when building a signed release:

- `ANDROID_KEYSTORE_PATH`: absolute path to the `.jks` file
- `ANDROID_KEYSTORE_PASSWORD`: keystore password
- `ANDROID_KEY_ALIAS`: `pokesurvivor` (or the alias you chose)
- `ANDROID_KEY_PASSWORD`: key password

Then run:

```bash
gradle assembleRelease
```

If signing variables are missing, Gradle can still compile the release variant but it will be unsigned. For distribution, install only the signed release APK and securely back up the keystore: future updates must use the same signing identity.

## GitHub Actions release signing

Add the following repository Actions secrets:

- `ANDROID_KEYSTORE_BASE64`: Base64-encoded contents of the JKS file
- `ANDROID_KEYSTORE_PASSWORD`
- `ANDROID_KEY_ALIAS`
- `ANDROID_KEY_PASSWORD`

On Linux/macOS, create the base64 value with `base64 -w 0 pokesurvivor-release.jks` (on macOS, use `base64 pokesurvivor-release.jks | tr -d '\n'`). The workflow uses a signed release when all four secrets are configured; otherwise it builds a debug APK. The signing key is never stored in the repository.

## Save behavior and limitations

- Progress is stored in Android WebView local storage and committed at the game's save checkpoints and when the page is hidden/closed. Clearing app data or uninstalling the app may erase it. A force-killed unfinished run is not restored as an in-progress run.
- This is an offline wrapper, not a Google Play billing or online multiplayer package.
- The web game and its third-party assets retain their original licenses; review the repository's license and credits before redistribution.
- Verify the APK on a real device: finish the starter test, change progression, force-stop, relaunch, and confirm the save remains.
