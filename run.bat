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
echo Setting up environment variables...

REM Check if environment variables are set
if "%HF_TOKEN%"=="" (
    echo Warning: HF_TOKEN environment variable not set!
    echo Please set your Hugging Face API token:
    echo Example: set HF_TOKEN=your_token_here
    echo.
)

if "%HF_MODEL_URL%"=="" (
    echo Warning: HF_MODEL_URL environment variable not set!
    echo Please set your Hugging Face model URL:
    echo Example: set HF_MODEL_URL=https://api-inference.huggingface.co/models/your-model
    echo.
)

if "%GEMINI_API_KEY%"=="" (
    echo Warning: GEMINI_API_KEY environment variable not set!
    echo Please set your Gemini API key:
    echo Example: set GEMINI_API_KEY=your_api_key_here
    echo.
)

echo.
echo Starting the application...
echo.
echo The app will be available at: http://localhost:9090
echo Press Ctrl+C to stop the server.
echo.

java -jar build\libs\ai-image-app-0.0.1-SNAPSHOT.jar

pause
