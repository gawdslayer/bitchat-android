# Bitchat Android Build Script
# This script will help you build the project with Android SDK 36

Write-Host "🚀 Building Bitchat Android Project..." -ForegroundColor Green

# Check if we're in the right directory
if (!(Test-Path "app\build.gradle")) {
    Write-Host "❌ Error: Please run this script from the bitchat-android directory" -ForegroundColor Red
    exit 1
}

# Check if Android SDK is available
if (!(Test-Path "$env:ANDROID_HOME")) {
    Write-Host "⚠️  Warning: ANDROID_HOME not set. Make sure Android SDK is installed." -ForegroundColor Yellow
}

# Try to use Gradle wrapper if available
if (Test-Path "gradlew.bat") {
    Write-Host "📦 Using Gradle wrapper..." -ForegroundColor Blue
    try {
        & .\gradlew.bat clean
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Clean successful" -ForegroundColor Green
        } else {
            Write-Host "❌ Clean failed" -ForegroundColor Red
            exit 1
        }
        
        & .\gradlew.bat build
        if ($LASTEXITCODE -eq 0) {
            Write-Host "✅ Build successful!" -ForegroundColor Green
            Write-Host "📱 APK location: app\build\outputs\apk\debug\app-debug.apk" -ForegroundColor Cyan
        } else {
            Write-Host "❌ Build failed" -ForegroundColor Red
            exit 1
        }
    } catch {
        Write-Host "❌ Error running Gradle wrapper: $_" -ForegroundColor Red
        exit 1
    }
} else {
    Write-Host "📦 Gradle wrapper not found. Please run the download script first:" -ForegroundColor Yellow
    Write-Host "   .\download-gradle-wrapper.ps1" -ForegroundColor Cyan
    exit 1
}

Write-Host "🎉 Build completed successfully!" -ForegroundColor Green 