# AI Image App - Spring Boot Application

A Spring Boot application that generates images using Hugging Face API with a Stable Diffusion model.

## Prerequisites

- Java 17 or higher
- Windows OS

## How to Run

### Option 1: Using the Batch Script (Easiest)
Simply double-click `run.bat` in the project root directory. This will:
1. Build the application
2. Start the server on http://localhost:8080

### Option 2: Using PowerShell Script
Open PowerShell and run:
```powershell
.\run.ps1
```

### Option 3: Using Gradle Directly
From the project root directory:
```
gradlew.bat build
gradlew.bat bootRun
```

Or build the JAR and run it:
```
gradlew.bat build
java -jar build\libs\ai-image-app-0.0.1-SNAPSHOT.jar
```

### Option 4: Using the IDE
1. Open the project in IntelliJ IDEA or any JetBrains IDE
2. Click on the green "Run" button next to the main function
3. Or use: Shift + F10 (Run) or Shift + F9 (Debug)

## Application Details

- **Server Port:** 8080
- **Base URL:** http://localhost:8080
- **Frontend:** http://localhost:8080/index.html
- **API Endpoint:** POST http://localhost:8080/api/image/generate

## API Usage

Send a POST request to `/api/image/generate` with a JSON body:

```json
{
  "prompt": "a beautiful sunset over the ocean"
}
```

Response:
```json
{
  "image": "data:image/png;base64,..."
}
```

## Configuration

Edit `src/main/resources/application.properties` to customize:
- Server port
- Application name
- Other Spring Boot settings

## Build Output

The built application generates two JAR files in `build/libs/`:
- `ai-image-app-0.0.1-SNAPSHOT.jar` - Executable JAR with embedded Tomcat
- `ai-image-app-0.0.1-SNAPSHOT-plain.jar` - Plain JAR with dependencies

## Troubleshooting

If `run.bat` doesn't work:
1. Ensure Java 17+ is installed: `java -version`
2. Try using `run.ps1` instead
3. Use the IDE's built-in run functionality
4. Check that the build succeeded: `gradlew.bat build`

## Fixed Issues

- Updated Java compatibility to 17 (max supported by Gradle 8.5)
- Removed deprecated Gradle task configurations
- Added proper task definitions for Gradle 8.5 compatibility
