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
Write-Host "Setting up environment variables..." -ForegroundColor Cyan

# Check if environment variables are set
$hfToken = $env:HF_TOKEN
$hfModelUrl = $env:HF_MODEL_URL
$geminiApiKey = $env:GEMINI_API_KEY

if (-not $hfToken) {
    Write-Host "Warning: HF_TOKEN environment variable not set!" -ForegroundColor Yellow
    Write-Host "Please set your Hugging Face API token:" -ForegroundColor Yellow
    Write-Host "Example: `$env:HF_TOKEN = 'your_token_here'" -ForegroundColor Yellow
}

if (-not $hfModelUrl) {
    Write-Host "Warning: HF_MODEL_URL environment variable not set!" -ForegroundColor Yellow
    Write-Host "Please set your Hugging Face model URL:" -ForegroundColor Yellow
    Write-Host "Example: `$env:HF_MODEL_URL = 'https://api-inference.huggingface.co/models/your-model'" -ForegroundColor Yellow
}

if (-not $geminiApiKey) {
    Write-Host "Warning: GEMINI_API_KEY environment variable not set!" -ForegroundColor Yellow
    Write-Host "Please set your Gemini API key:" -ForegroundColor Yellow
    Write-Host "Example: `$env:GEMINI_API_KEY = 'your_api_key_here'" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Starting the application..." -ForegroundColor Cyan
Write-Host ""
Write-Host "The app will be available at: http://localhost:9090" -ForegroundColor Yellow
Write-Host "Press Ctrl+C to stop the server." -ForegroundColor Yellow
Write-Host ""

& java -jar build\libs\ai-image-app-0.0.1-SNAPSHOT.jar

Read-Host "Press Enter to exit"
