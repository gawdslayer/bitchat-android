# 🔧 Troubleshooting Guide for Android SDK 36

This guide addresses common issues when building the Bitchat Android project with Android SDK 36 (Android 16.0 "Baklava").

## 🚨 Common Error: `org.gradle.api.artifacts.Dependency org.gradle.api.artifacts.dsl.DependencyHandler.module(java.lang.Object)`

### **Root Cause**
This error typically occurs when there's a version mismatch between:
- Android Gradle Plugin version
- Gradle version
- Kotlin version
- Compose compiler version

### **Solution Applied**
I've updated the project configuration to be compatible with Android SDK 36:

#### **Updated Versions:**
```gradle
// build.gradle (root)
plugins {
    id 'com.android.application' version '8.4.0'  // Updated from 8.1.0
    id 'org.jetbrains.kotlin.android' version '1.9.20'  // Updated from 1.8.20
    id 'com.google.dagger.hilt.android' version '2.48'  // Updated from 2.45
}

// app/build.gradle
android {
    compileSdk 36  // Updated from 34
    targetSdk 36   // Updated from 34
    
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17  // Updated from VERSION_1_8
        targetCompatibility JavaVersion.VERSION_17  // Updated from VERSION_1_8
    }
    
    kotlinOptions {
        jvmTarget = '17'  // Updated from '1.8'
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion '1.5.4'  // Updated from '1.4.7'
    }
}
```

## 🛠️ Step-by-Step Fix

### **Step 1: Download Gradle Wrapper**
```powershell
# Run this in PowerShell from the bitchat-android directory
.\download-gradle-wrapper.ps1
```

### **Step 2: Clean and Rebuild**
```powershell
# Clean the project
.\gradlew.bat clean

# Build the project
.\gradlew.bat build
```

### **Step 3: Alternative - Use Build Script**
```powershell
# Use the automated build script
.\build-project.ps1
```

## 🔍 Additional Troubleshooting

### **Issue: "Gradle wrapper not found"**
**Solution:**
1. Run the download script: `.\download-gradle-wrapper.ps1`
2. Verify `gradle\wrapper\gradle-wrapper.jar` exists
3. Try building again: `.\gradlew.bat build`

### **Issue: "Android SDK not found"**
**Solution:**
1. Set `ANDROID_HOME` environment variable:
   ```powershell
   $env:ANDROID_HOME = "C:\Users\YourUsername\AppData\Local\Android\Sdk"
   ```
2. Add to PATH:
   ```powershell
   $env:PATH += ";$env:ANDROID_HOME\platform-tools"
   ```

### **Issue: "Java version incompatible"**
**Solution:**
1. Ensure Java 17 is installed
2. Set `JAVA_HOME` environment variable:
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
   ```

### **Issue: "Compose compiler version mismatch"**
**Solution:**
The compose compiler version has been updated to `1.5.4` to match Kotlin 1.9.20.

### **Issue: "Dependency conflicts"**
**Solution:**
I've simplified the dependencies to avoid conflicts:
- Removed Room database (can be added back later)
- Removed WorkManager (can be added back later)
- Removed DataStore (can be added back later)
- Kept only essential dependencies

## 📱 Testing the Build

### **Successful Build Output**
```
✅ Clean successful
✅ Build successful!
📱 APK location: app\build\outputs\apk\debug\app-debug.apk
```

### **Install on Device**
1. Enable USB debugging on your Android device
2. Connect via USB
3. Install the APK:
   ```powershell
   adb install app\build\outputs\apk\debug\app-debug.apk
   ```

## 🔄 Adding Back Dependencies

Once the basic build works, you can gradually add back dependencies:

### **Room Database**
```gradle
// Add to app/build.gradle dependencies
implementation 'androidx.room:room-runtime:2.6.1'
implementation 'androidx.room:room-ktx:2.6.1'
kapt 'androidx.room:room-compiler:2.6.1'
```

### **WorkManager**
```gradle
// Add to app/build.gradle dependencies
implementation 'androidx.work:work-runtime-ktx:2.9.0'
implementation 'androidx.hilt:hilt-work:1.1.0'
```

### **DataStore**
```gradle
// Add to app/build.gradle dependencies
implementation 'androidx.datastore:datastore-preferences:1.0.0'
```

## 🐛 Debug Information

### **Check Gradle Version**
```powershell
.\gradlew.bat --version
```

### **Check Android SDK**
```powershell
sdkmanager --list
```

### **Check Java Version**
```powershell
java -version
```

### **Enable Gradle Debug**
```powershell
.\gradlew.bat build --debug
```

## 📞 Getting Help

If you're still experiencing issues:

1. **Check the logs**: Look for specific error messages
2. **Verify versions**: Ensure all tools are compatible with Android SDK 36
3. **Clean environment**: Delete `.gradle` and `build` folders, then rebuild
4. **Update tools**: Make sure Android Studio and SDK tools are up to date

## 🎯 Success Criteria

The build is successful when:
- ✅ `.\gradlew.bat build` completes without errors
- ✅ APK is generated in `app\build\outputs\apk\debug\`
- ✅ App installs and launches on device
- ✅ No dependency conflicts in build output

---

**Remember**: Android SDK 36 is very new, so some dependencies might not be fully compatible yet. The simplified configuration should work, and you can add features back gradually as compatibility improves. 