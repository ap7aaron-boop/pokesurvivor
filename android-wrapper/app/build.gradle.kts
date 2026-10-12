import org.gradle.api.tasks.Sync

plugins {
    id("com.android.application")
}

val repoRoot = rootProject.projectDir.parentFile
val keystorePath = providers.environmentVariable("ANDROID_KEYSTORE_PATH").orNull
val keyAliasValue = providers.environmentVariable("ANDROID_KEY_ALIAS").orNull
val keyPasswordValue = providers.environmentVariable("ANDROID_KEY_PASSWORD").orNull
val storePasswordValue = providers.environmentVariable("ANDROID_KEYSTORE_PASSWORD").orNull
val releaseSigningReady = !keystorePath.isNullOrBlank() &&
    !keyAliasValue.isNullOrBlank() &&
    !keyPasswordValue.isNullOrBlank() &&
    !storePasswordValue.isNullOrBlank() &&
    file(keystorePath).exists()

android {
    namespace = "com.naarodev.pokesurvivor"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.naarodev.pokesurvivor"
        minSdk = 24
        targetSdk = 35
        versionCode = 4
        versionName = "1.0.3"
    }

    signingConfigs {
        if (releaseSigningReady) {
            create("release") {
                storeFile = file(keystorePath!!)
                storePassword = storePasswordValue
                keyAlias = keyAliasValue
                keyPassword = keyPasswordValue
            }
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
        release {
            isMinifyEnabled = false
            if (releaseSigningReady) {
                signingConfig = signingConfigs.getByName("release")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.webkit:webkit:1.12.1")
}

val syncGameAssets by tasks.registering(Sync::class) {
    from(repoRoot) {
        include("index.html")
        include("js/**")
        include("css/**")
        include("assets/**")
        include("favicon.ico")
        include("manifest.webmanifest")
        exclude("js/**/*.map")
    }
    into(layout.projectDirectory.dir("src/main/assets/www"))
}

tasks.named("preBuild").configure {
    dependsOn(syncGameAssets)
}
