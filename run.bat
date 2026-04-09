@echo off
REM Build and Run AI Image App
REM This script builds the application and starts the server

cd /d "%~dp0"

echo Building AI Image Application...
call gradlew.bat build --warning-mode summary

if %ERRORLEVEL% neq 0 (
    echo Build failed!
    pause
    exit /b 1
)

echo.
echo Build completed successfully!
echo.
echo Starting the application...
echo.
echo The app will be available at: http://localhost:8080
echo Press Ctrl+C to stop the server.
echo.

java -jar build\libs\ai-image-app-0.0.1-SNAPSHOT.jar

pause
