# Check Java Installation Script
Write-Host "🔍 Checking Java Installation..." -ForegroundColor Green

# Check if java command is available
try {
    $javaVersion = java -version 2>&1
    Write-Host "✅ Java is installed:" -ForegroundColor Green
    Write-Host $javaVersion[0] -ForegroundColor Cyan
} catch {
    Write-Host "❌ Java command not found in PATH" -ForegroundColor Red
}

# Check common Java installation locations
$javaLocations = @(
    "C:\Program Files\Java\jdk-17",
    "C:\Program Files\Java\jdk-17.0.9",
    "C:\Program Files\Java\jdk-21",
    "C:\Program Files\Java\jdk-21.0.1",
    "C:\Program Files\Eclipse Adoptium\jdk-17",
    "C:\Program Files\Eclipse Adoptium\jdk-21",
    "C:\Program Files\OpenJDK\jdk-17",
    "C:\Program Files\OpenJDK\jdk-21"
)

Write-Host "`n🔍 Checking common Java installation locations..." -ForegroundColor Yellow

$foundJava = $false
foreach ($location in $javaLocations) {
    if (Test-Path "$location\bin\java.exe") {
        Write-Host "✅ Found Java at: $location" -ForegroundColor Green
        Write-Host "   Setting JAVA_HOME to: $location" -ForegroundColor Cyan
        
        # Set JAVA_HOME for current session
        $env:JAVA_HOME = $location
        $env:PATH = "$location\bin;$env:PATH"
        
        # Test if it works
        try {
            $testVersion = & "$location\bin\java.exe" -version 2>&1
            Write-Host "   Java version: $($testVersion[0])" -ForegroundColor Green
            $foundJava = $true
            break
        } catch {
            Write-Host "   ❌ Error testing Java at this location" -ForegroundColor Red
        }
    }
}

if (-not $foundJava) {
    Write-Host "`n❌ No Java installation found in common locations" -ForegroundColor Red
    Write-Host "`n📥 You need to install Java 17 or later:" -ForegroundColor Yellow
    Write-Host "   1. Download from: https://adoptium.net/" -ForegroundColor Cyan
    Write-Host "   2. Install Java 17 or 21" -ForegroundColor Cyan
    Write-Host "   3. Run this script again" -ForegroundColor Cyan
} else {
    Write-Host "`n✅ Java is now configured!" -ForegroundColor Green
    Write-Host "🔄 Please restart your terminal/Android Studio" -ForegroundColor Yellow
}

# Check current JAVA_HOME
Write-Host "`n📋 Current JAVA_HOME: $env:JAVA_HOME" -ForegroundColor Blue 