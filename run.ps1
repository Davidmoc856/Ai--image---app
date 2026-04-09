# Build and Run AI Image App
# This script builds the application and starts the server

Set-Location $PSScriptRoot

Write-Host "Building AI Image Application..." -ForegroundColor Cyan
& .\gradlew.bat build --warning-mode summary

if ($LASTEXITCODE -ne 0) {
    Write-Host "Build failed!" -ForegroundColor Red
    Read-Host "Press Enter to exit"
    exit 1
}

Write-Host ""
Write-Host "Build completed successfully!" -ForegroundColor Green
Write-Host ""
Write-Host "Starting the application..." -ForegroundColor Cyan
Write-Host ""
Write-Host "The app will be available at: http://localhost:8080" -ForegroundColor Yellow
Write-Host "Press Ctrl+C to stop the server." -ForegroundColor Yellow
Write-Host ""

& java -jar build\libs\ai-image-app-0.0.1-SNAPSHOT.jar

Read-Host "Press Enter to exit"
