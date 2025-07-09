# 🔧 Fix Gradle Permission Issues in Android Studio

## 🚨 Error: "Could not create parent directory for lock file"

This error occurs when Android Studio doesn't have permission to write to the Gradle cache directory.

## 🛠️ Solutions (Try in Order)

### **Solution 1: Change Gradle Home Directory (Easiest)**

1. **Open Android Studio**
2. **Go to File → Settings** (or press **Ctrl+Alt+S**)
3. **Navigate to**: `Build, Execution, Deployment → Gradle`
4. **Change "Gradle user home"** to:
   ```
   C:\temp\gradle-home
   ```
5. **Click Apply and OK**
6. **Restart Android Studio**
7. **Try building again**

### **Solution 2: Run Permission Fix Script**

1. **Right-click PowerShell** and select **"Run as Administrator"**
2. **Navigate to project directory**:
   ```powershell
   cd "E:\PROJECTS\bitchat-android"
   ```
3. **Run the fix script**:
   ```powershell
   .\fix-gradle-permissions.ps1
   ```
4. **Restart Android Studio**
5. **Try building again**

### **Solution 3: Manual Permission Fix**

1. **Open File Explorer**
2. **Navigate to**: `C:\Users\mrgsk\.gradle`
3. **Right-click the `.gradle` folder**
4. **Select Properties**
5. **Click Security tab**
6. **Click Edit**
7. **Select your username**
8. **Check "Full control"**
9. **Click Apply and OK**
10. **Restart Android Studio**

### **Solution 4: Delete and Recreate Gradle Cache**

1. **Close Android Studio**
2. **Delete the Gradle cache**:
   ```powershell
   Remove-Item -Recurse -Force "C:\Users\mrgsk\.gradle"
   ```
3. **Restart Android Studio**
4. **Let it recreate the cache**

### **Solution 5: Use Different Gradle Version**

1. **Open `gradle/wrapper/gradle-wrapper.properties`**
2. **Change the distribution URL**:
   ```properties
   distributionUrl=https\://services.gradle.org/distributions/gradle-8.3-bin.zip
   ```
3. **Sync project in Android Studio**

## 🔍 **Alternative: Use Command Line**

If Android Studio still has issues, try building from command line:

1. **Open PowerShell** (as Administrator)
2. **Navigate to project**:
   ```powershell
   cd "E:\PROJECTS\bitchat-android"
   ```
3. **Download Gradle wrapper**:
   ```powershell
   .\download-gradle-wrapper.ps1
   ```
4. **Build project**:
   ```powershell
   .\gradlew.bat clean
   .\gradlew.bat build
   ```

## 🎯 **Success Indicators**

After fixing, you should see:
- ✅ Android Studio can sync project
- ✅ Gradle builds without permission errors
- ✅ APK is generated successfully

## 🐛 **If Still Having Issues**

1. **Check Windows Defender**: Make sure it's not blocking Gradle
2. **Check antivirus**: Temporarily disable to test
3. **Run as Administrator**: Try running Android Studio as Administrator
4. **Use different directory**: Move project to `C:\temp\bitchat-android`

## 📞 **Getting Help**

If none of these solutions work:
1. Check Android Studio logs: `Help → Show Log in Explorer`
2. Check Gradle logs: `View → Tool Windows → Build`
3. Try creating a new project to test if it's a project-specific issue

---

**Remember**: The permission issue is common on Windows and usually resolves with one of these solutions. 