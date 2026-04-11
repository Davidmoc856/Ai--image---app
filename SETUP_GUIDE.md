# AI Image App - Environment Setup Guide

## 🚀 Quick Start

### Step 1: Set Environment Variables

You need to set these environment variables before running the app:

#### Option A: PowerShell (Recommended)
```powershell
# Set your API keys (replace with your actual keys)
$env:HF_TOKEN = "your_huggingface_token_here"
$env:HF_MODEL_URL = "https://api-inference.huggingface.co/models/your-model-name"
$env:GEMINI_API_KEY = "your_gemini_api_key_here"

# Then run the app
.\run.ps1
```

#### Option B: Command Prompt
```cmd
# Set your API keys (replace with your actual keys)
set HF_TOKEN=your_huggingface_token_here
set HF_MODEL_URL=https://api-inference.huggingface.co/models/your-model-name
set GEMINI_API_KEY=your_gemini_api_key_here

# Then run the app
run.bat
```

#### Option C: Permanent Environment Variables (Windows)
1. Search for "Environment Variables" in Windows search
2. Click "Edit the system environment variables"
3. Click "Environment Variables" button
4. Under "User variables", click "New" for each:
   - Variable name: `HF_TOKEN`, Value: your_token
   - Variable name: `HF_MODEL_URL`, Value: your_model_url
   - Variable name: `GEMINI_API_KEY`, Value: your_api_key

### Step 2: Run the Application

After setting the environment variables, run:
- `.\run.ps1` (PowerShell)
- `run.bat` (Command Prompt)

The app will be available at: **http://localhost:9090**

## 🔑 Getting API Keys

### Hugging Face Token
1. Go to [https://huggingface.co/settings/tokens](https://huggingface.co/settings/tokens)
2. Create a new token with "Read" permissions
3. Copy the token value

### Hugging Face Model URL
Use one of these popular image generation models:
- `https://api-inference.huggingface.co/models/stabilityai/stable-diffusion-2-1`
- `https://api-inference.huggingface.co/models/CompVis/stable-diffusion-v1-4`
- `https://api-inference.huggingface.co/models/runwayml/stable-diffusion-v1-5`

### Gemini API Key
1. Go to [https://makersuite.google.com/app/apikey](https://makersuite.google.com/app/apikey)
2. Create a new API key
3. Copy the key value

## 🐳 Docker Deployment

If you want to deploy with Docker:

```bash
# Set environment variables
export HF_TOKEN="your_token"
export HF_MODEL_URL="your_model_url"
export GEMINI_API_KEY="your_api_key"

# Build and run
docker build -t ai-image-app .
docker run -p 9090:9090 \
  -e HF_TOKEN="$HF_TOKEN" \
  -e HF_MODEL_URL="$HF_MODEL_URL" \
  -e GEMINI_API_KEY="$GEMINI_API_KEY" \
  ai-image-app
```

## 🧪 Testing the App

Once running, test the API:

```bash
curl -X POST http://localhost:9090/api/image/generate \
  -H "Content-Type: application/json" \
  -d '{"prompt": "A beautiful sunset over mountains"}'
```

## ❗ Troubleshooting

### "Environment variable not set" warnings
- Make sure you've set all three required environment variables
- Restart your terminal/command prompt after setting variables

### "Connection refused" or API errors
- Check that your API keys are valid and have proper permissions
- Verify the model URL is correct and accessible

### Port already in use
- Change the port in `application.properties` if 9090 is taken
- Or stop other applications using port 9090

### Build fails
- Make sure you have Java 17+ installed
- Check that all dependencies can be downloaded

---

**Need help?** Check the `FIXES_APPLIED.md` and `DEPLOYMENT_READY.md` files for detailed documentation.
