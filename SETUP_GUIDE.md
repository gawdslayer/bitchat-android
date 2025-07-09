# 🚀 Bitchat Android Setup Guide

This guide will walk you through setting up the Android development environment and building the Bitchat Android client.

## 📋 Prerequisites

### Required Software
- **Android Studio** (Arctic Fox or later) - [Download here](https://developer.android.com/studio)
- **Java Development Kit (JDK)** 8 or later
- **Android SDK** API 21+ (Android 5.0+)
- **Git** for version control

### Hardware Requirements
- **Android device** with Bluetooth LE support (for testing)
- **USB cable** to connect your device
- **Bluetooth LE capable device** (required for mesh networking)

## 🛠️ Development Environment Setup

### Step 1: Install Android Studio

1. Download Android Studio from the [official website](https://developer.android.com/studio)
2. Run the installer and follow the setup wizard
3. During installation, make sure to install:
   - Android SDK
   - Android SDK Platform-Tools
   - Android Virtual Device (AVD) Manager

### Step 2: Configure Android SDK

1. Open Android Studio
2. Go to **File → Settings → Appearance & Behavior → System Settings → Android SDK**
3. Install the following SDK components:
   - **Android SDK Platform 34** (or latest)
   - **Android SDK Build-Tools 34.0.0** (or latest)
   - **Android SDK Platform-Tools**
   - **Android Emulator**
   - **Android SDK Tools**

### Step 3: Set Up Your Device

1. **Enable Developer Options** on your Android device:
   - Go to **Settings → About Phone**
   - Tap **Build Number** 7 times
   - Go back to **Settings → Developer Options**
   - Enable **USB Debugging**

2. **Connect your device** via USB cable

3. **Install device drivers** if prompted

## 🏗️ Building the Project

### Step 1: Clone the Repository

```bash
git clone https://github.com/your-username/bitchat-android.git
cd bitchat-android
```

### Step 2: Open in Android Studio

1. Open Android Studio
2. Select **Open an existing Android Studio project**
3. Navigate to the `bitchat-android` folder and select it
4. Wait for the project to sync and Gradle to download dependencies

### Step 3: Configure Build Settings

1. Open `app/build.gradle`
2. Verify the following settings:
   ```gradle
   android {
       compileSdk 34
       defaultConfig {
           minSdk 21
           targetSdk 34
           // ... other settings
       }
   }
   ```

3. Sync the project: **File → Sync Project with Gradle Files**

### Step 4: Build and Run

1. **Select your device** from the device dropdown in the toolbar
2. Click the **Run** button (green play icon) or press **Shift + F10**
3. The app will be installed and launched on your device

## 🔧 Project Structure

```
bitchat-android/
├── app/
│   ├── src/main/
│   │   ├── java/com/bitchat/
│   │   │   ├── ui/              # Jetpack Compose UI components
│   │   │   ├── viewmodel/       # MVVM ViewModels
│   │   │   ├── service/         # BLE, encryption, message services
│   │   │   ├── protocol/        # Binary protocol implementation
│   │   │   ├── crypto/          # Encryption and key management
│   │   │   └── util/            # Utility classes
│   │   ├── res/                 # Resources (strings, layouts, etc.)
│   │   └── AndroidManifest.xml  # App permissions and components
│   └── build.gradle             # App-level dependencies
├── build.gradle                 # Project-level configuration
├── settings.gradle              # Project settings
└── README.md                    # Project documentation
```

## 🔐 Permissions Setup

The app requires several permissions for Bluetooth mesh networking:

### Bluetooth Permissions
- `BLUETOOTH` - Basic Bluetooth functionality
- `BLUETOOTH_ADMIN` - Bluetooth administration
- `BLUETOOTH_CONNECT` - Connect to Bluetooth devices
- `BLUETOOTH_SCAN` - Scan for Bluetooth devices
- `BLUETOOTH_ADVERTISE` - Advertise as Bluetooth device

### Location Permissions
- `ACCESS_FINE_LOCATION` - Required for BLE scanning on Android
- `ACCESS_COARSE_LOCATION` - Alternative location permission

### Other Permissions
- `INTERNET` - For potential future features
- `WAKE_LOCK` - For background operation
- `FOREGROUND_SERVICE` - For background BLE operations
- `POST_NOTIFICATIONS` - For message notifications

## 🧪 Testing

### Testing on Physical Device

1. **Enable Bluetooth** on your device
2. **Grant permissions** when prompted:
   - Bluetooth permissions
   - Location permissions
   - Notification permissions

3. **Test basic functionality**:
   - App launches without crashes
   - Bluetooth scanning works
   - UI responds to user input

### Testing with Multiple Devices

1. **Install the app** on multiple Android devices
2. **Enable Bluetooth** on all devices
3. **Grant permissions** on all devices
4. **Test mesh networking**:
   - Devices should discover each other
   - Messages should relay between devices
   - Encryption should work properly

### Testing with iOS Devices

1. **Build the iOS version** of Bitchat
2. **Install on iOS device(s)**
3. **Test cross-platform compatibility**:
   - Android and iOS devices should discover each other
   - Messages should relay between platforms
   - Encryption should work across platforms

## 🐛 Troubleshooting

### Common Issues

#### Build Errors
- **Gradle sync fails**: Check internet connection and try again
- **SDK not found**: Install required SDK components
- **Version conflicts**: Update dependency versions

#### Runtime Errors
- **Bluetooth not working**: Ensure Bluetooth is enabled and permissions granted
- **Location permission required**: Grant location permission for BLE scanning
- **App crashes**: Check logcat for error details

#### Device Issues
- **Device not detected**: Install proper USB drivers
- **USB debugging not working**: Enable developer options and USB debugging
- **Bluetooth LE not supported**: Use a device with Bluetooth LE support

### Debug Tools

1. **Android Studio Logcat**: View app logs and debug information
2. **Bluetooth debugging**: Use Android's built-in Bluetooth debugging tools
3. **Network debugging**: Monitor BLE traffic and packet exchange

## 📱 Deployment

### Debug Build
```bash
./gradlew assembleDebug
```

### Release Build
```bash
./gradlew assembleRelease
```

### APK Location
- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Release APK: `app/build/outputs/apk/release/app-release.apk`

## 🔄 Next Steps

1. **Implement missing features**:
   - LZ4 compression
   - Persistent storage
   - Background service optimization
   - UI improvements

2. **Add tests**:
   - Unit tests for protocol implementation
   - Integration tests for BLE functionality
   - UI tests for user interactions

3. **Optimize performance**:
   - Battery optimization
   - Memory usage optimization
   - Network efficiency improvements

4. **Security audit**:
   - Review encryption implementation
   - Test for vulnerabilities
   - Implement additional security features

## 📚 Resources

- [Android BLE Documentation](https://developer.android.com/guide/topics/connectivity/bluetooth/ble-overview)
- [Jetpack Compose Documentation](https://developer.android.com/jetpack/compose)
- [Bouncy Castle Documentation](https://www.bouncycastle.org/documentation.html)
- [Original iOS Bitchat Repository](https://github.com/jackjackbits/bitchat)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## 📄 License

This project is released into the public domain. See the [LICENSE](LICENSE) file for details. 