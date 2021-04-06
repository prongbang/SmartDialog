# Smart Dialog for Android

## Preview

![Image](/screenshot/screenshot.png)

## Installation

- Add the following repositories to your project/build.gradle file.

```
repositories {
   maven { url 'https://jitpack.io' }
}
```

- Add the following dependency to your project/app/build.gradle file.

```
android {
    packagingOptions {
        exclude 'META-INF/*.kotlin_module'
    }
}
dependencies {
    implementation 'com.github.prongbang:smartdialog:1.0.1'
}
```

## Used

```kotlin
SmartDialogFragment.Builder(supportFragmentManager)
        .setIcon(SmartIcon.SUCCESS)
        .setTitle("SUCCESS")
        .setMessage("Camera granted, You can access camera to take a picture")
        .setPositiveButton("CLOSE") {
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT)
                    .show()
        }
        .build()
        .show()
```
