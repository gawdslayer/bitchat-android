# ☕ Java Setup Guide for Android Development

## 🚨 Error: "JAVA_HOME is not set and no 'java' command could be found"

This error means Java isn't installed or properly configured on your system.

## 🛠️ Quick Fix Steps

### **Step 1: Check Current Java Installation**

Run the Java check script:
```powershell
cd "E:\PROJECTS\bitchat-android"
.\check-java.ps1
```

### **Step 2: Install Java (If Not Found)**

#### **Option A: Download from Eclipse Adoptium (Recommended)**

1. **Go to**: https://adoptium.net/
2. **Download**: Java 17 or 21 (LTS version)
3. **Run the installer**
4. **Make sure to check "Add to PATH"** during installation

#### **Option B: Download from Oracle**

1. **Go to**: https://www.oracle.com/java/technologies/downloads/
2. **Download**: Java 17 or 21 for Windows
3. **Run the installer**
4. **Note**: Oracle requires account creation

### **Step 3: Set JAVA_HOME Environment Variable**

#### **Method 1: Using PowerShell (Temporary)**
```powershell
# Set for current session
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:PATH = "$env:JAVA_HOME\bin;$env:PATH"

# Verify
java -version
echo $env:JAVA_HOME
```

#### **Method 2: Using System Environment Variables (Permanent)**

1. **Press Win + R**, type `sysdm.cpl`, press Enter
2. **Click "Environment Variables"**
3. **Under "System Variables"**, click "New"
4. **Variable name**: `JAVA_HOME`
5. **Variable value**: `C:\Program Files\Java\jdk-17` (adjust path as needed)
6. **Click OK**
7. **Find "Path" in System Variables**, click "Edit"
8. **Click "New"** and add: `%JAVA_HOME%\bin`
9. **Click OK** on all dialogs
10. **Restart your terminal/Android Studio**

### **Step 4: Verify Installation**

```powershell
# Check Java version
java -version

# Check JAVA_HOME
echo $env:JAVA_HOME

# Check if javac is available
javac -version
```

## 📋 **Java Version Requirements**

For Android SDK 36, you need:
- ✅ **Java 17** (recommended)
- ✅ **Java 21** (also supported)
- ❌ **Java 8** (too old)
- ❌ **Java 11** (too old)

## 🔍 **Common Java Installation Locations**

The script checks these locations:
```
C:\Program Files\Java\jdk-17
C:\Program Files\Java\jdk-17.0.9
C:\Program Files\Java\jdk-21
C:\Program Files\Java\jdk-21.0.1
C:\Program Files\Eclipse Adoptium\jdk-17
C:\Program Files\Eclipse Adoptium\jdk-21
C:\Program Files\OpenJDK\jdk-17
C:\Program Files\OpenJDK\jdk-21
```

## 🎯 **Expected Output After Fix**

When Java is properly configured, you should see:
```
✅ Java is installed:
openjdk version "17.0.9" 2023-10-17
✅ Found Java at: C:\Program Files\Java\jdk-17
   Setting JAVA_HOME to: C:\Program Files\Java\jdk-17
   Java version: openjdk version "17.0.9" 2023-10-17
✅ Java is now configured!
📋 Current JAVA_HOME: C:\Program Files\Java\jdk-17
```

## 🐛 **Troubleshooting**

### **Issue: "Java is not recognized as an internal or external command"**
**Solution**: Java is not in PATH
1. Check if Java is installed
2. Add Java bin directory to PATH
3. Restart terminal

### **Issue: "JAVA_HOME is set but java command doesn't work"**
**Solution**: PATH is not updated
1. Add `%JAVA_HOME%\bin` to PATH
2. Restart terminal

### **Issue: "Wrong Java version"**
**Solution**: Multiple Java versions installed
1. Check all Java installations
2. Set JAVA_HOME to correct version
3. Update PATH accordingly

### **Issue: "Permission denied"**
**Solution**: Run as Administrator
1. Right-click PowerShell
2. Select "Run as Administrator"
3. Try commands again

## 🔄 **After Java is Fixed**

Once Java is working:

1. **Restart Android Studio**
2. **Try building the project**:
   ```powershell
   .\gradlew.bat clean
   .\gradlew.bat build
   ```
3. **Or use the build script**:
   ```powershell
   .\build-project.ps1
   ```

## 📞 **Getting Help**

If you're still having issues:

1. **Run the check script**: `.\check-java.ps1`
2. **Check Android Studio logs**: `Help → Show Log in Explorer`
3. **Verify Java installation**: Look in `C:\Program Files\Java\`
4. **Try different Java version**: Java 17 or 21

---

**Remember**: Java is required for Android development. Once properly installed and configured, you should be able to build the Bitchat Android project successfully! 🚀 