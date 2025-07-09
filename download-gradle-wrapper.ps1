# Download Gradle Wrapper JAR
$wrapperUrl = "https://github.com/gradle/gradle/raw/v8.4.0/gradle/wrapper/gradle-wrapper.jar"
$wrapperPath = "gradle\wrapper\gradle-wrapper.jar"

# Create directory if it doesn't exist
if (!(Test-Path "gradle\wrapper")) {
    New-Item -ItemType Directory -Path "gradle\wrapper" -Force
}

# Download the wrapper JAR
Write-Host "Downloading Gradle wrapper JAR..."
Invoke-WebRequest -Uri $wrapperUrl -OutFile $wrapperPath

Write-Host "Gradle wrapper downloaded successfully!"
Write-Host "You can now run: .\gradlew.bat build" 