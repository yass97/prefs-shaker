# Prefs Shaker
Debugging tool to easily check data in SharedPreferences.

|Light Mode|Dark Mode|
|----------|---------|
|<img src="https://github.com/user-attachments/assets/b16f4d76-682b-4ad7-b214-12cab038ad70" width=200 />|<img src="https://github.com/user-attachments/assets/23f6bc25-4f08-4b83-a245-2b119771c487" width=200 />|

## Features
- Shake the device and check the data in SharedPreferences
- Only works in debug builds

## Installation

### settings.gradle.kts
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

### build.gradle.kts
```kotlin
dependencies {
    implementation("com.github.yass97:prefs-shaker:<version>")
}
```

**Note:**
`Prefs Shaker` uses [Material 3](https://developer.android.com/jetpack/androidx/releases/compose-material3) for its UI.
If your project is using only Material 2 (`androidx.compose.material`), please add the following dependency to your `build.gradle.kts`:

```kotlin
dependencies {
    implementation("androidx.compose.material3:material3:<version>")
}
```

## Usage

### 1. MyApplication.kt
Add PrefsShaker to the MyApplication class and call the init method.

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // File name of shared preferences
            PrefsShaker.init(this, "fileName")
        }
    }
}
```

### 2. AndroidManifest.xml
Set MyApplication class in AndroidManifest.xml.

```xml
<application android:name=".MyApplication">
  ...
</application>
```

### 3. Build in debug mode
Shake the device and the Prefs Shaker will display.</br>
If SharedPreferences is not generated, or if the file is misnamed, Prefs Shaker will not display after shaking.
