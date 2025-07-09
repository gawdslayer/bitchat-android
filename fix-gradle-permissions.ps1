# Fix Gradle Permissions Script
# Run this as Administrator

Write-Host "🔧 Fixing Gradle Permissions..." -ForegroundColor Green

# Check if running as Administrator
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole] "Administrator")

if (-not $isAdmin) {
    Write-Host "❌ This script needs to be run as Administrator" -ForegroundColor Red
    Write-Host "Right-click PowerShell and select 'Run as Administrator'" -ForegroundColor Yellow
    exit 1
}

# Get user profile path
$userProfile = $env:USERPROFILE
$gradleHome = "$userProfile\.gradle"

Write-Host "📁 Gradle home: $gradleHome" -ForegroundColor Blue

# Create Gradle directory if it doesn't exist
if (!(Test-Path $gradleHome)) {
    Write-Host "📂 Creating Gradle directory..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $gradleHome -Force
}

# Take ownership of the Gradle directory
Write-Host "🔐 Taking ownership of Gradle directory..." -ForegroundColor Yellow
$currentUser = [System.Security.Principal.WindowsIdentity]::GetCurrent().Name
takeown /f "$gradleHome" /r /d y

# Grant full permissions to current user
Write-Host "🔑 Granting full permissions..." -ForegroundColor Yellow
icacls "$gradleHome" /grant "${currentUser}:(OI)(CI)F" /t

# Create wrapper directory
$wrapperDir = "$gradleHome\wrapper\dists"
if (!(Test-Path $wrapperDir)) {
    Write-Host "📂 Creating wrapper directory..." -ForegroundColor Yellow
    New-Item -ItemType Directory -Path $wrapperDir -Force
}

# Grant permissions to wrapper directory
icacls "$wrapperDir" /grant "${currentUser}:(OI)(CI)F" /t

Write-Host "✅ Permissions fixed!" -ForegroundColor Green
Write-Host "🔄 Please restart Android Studio and try building again" -ForegroundColor Cyan 