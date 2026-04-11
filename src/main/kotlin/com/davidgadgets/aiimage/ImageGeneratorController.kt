package com.davidgadgets.aiimage

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.util.*

@RestController
@RequestMapping("/api/image")
@CrossOrigin(origins = ["*"])
class ImageGeneratorController {

    @Value("\${HF_TOKEN}")
    private lateinit var hfToken: String

    @Value("\${HF_MODEL_URL}")
    private lateinit var modelUrl: String

    @Value("\${GEMINI_API_KEY}") // We will add this to your Render Environment Variables
    private lateinit var geminiApiKey: String

    private val restTemplate = RestTemplate()

    @PostMapping("/generate")
    fun generate(@RequestBody request: Map<String, String>): ResponseEntity<Map<String, Any>> {
        val rawPrompt = request["prompt"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "Prompt is required"))

        // --- THE BRAIN UPGRADE: PROMPT EXPANSION ---
        val smartPrompt = try {
            expandPromptWithGemini(rawPrompt)
        } catch (_: Exception) {
            rawPrompt // Fallback to raw prompt if Gemini fails
        }

        val imageList = mutableListOf<String>()

        try {
            val headers = HttpHeaders()
            headers.set("Authorization", "Bearer $hfToken")
            headers.contentType = MediaType.APPLICATION_JSON
            headers.set("Accept", "image/png")

            for (i in 1..4) {
                // We add the smart prompt here
                val requestBody = mapOf(
                    "inputs" to "$smartPrompt (variation $i)",
                    "parameters" to mapOf("num_inference_steps" to 4)
                )

                val requestEntity = HttpEntity(requestBody, headers)
                val responseEntity = restTemplate.exchange(modelUrl, HttpMethod.POST, requestEntity, ByteArray::class.java)

                if (responseEntity.statusCode.is2xxSuccessful) {
                    val responseBytes = responseEntity.body
                    if (responseBytes != null && responseBytes.isNotEmpty()) {
                        val base64Image = Base64.getEncoder().encodeToString(responseBytes)
                        imageList.add("data:image/png;base64,$base64Image")
                    }
                }
            }

            return if (imageList.isNotEmpty()) {
                ResponseEntity.ok(mapOf("images" to imageList))
            } else {
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "Failed to generate images"))
            }

        } catch (e: HttpClientErrorException) {
            return ResponseEntity.status(e.statusCode).body(mapOf("error" to "AI is busy. Try again soon!"))
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "Connection error!"))
        }
    }

    // This is the hidden "Ghostwriter" function
    @Suppress("UNCHECKED_CAST")
    private fun expandPromptWithGemini(rawPrompt: String): String {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=$geminiApiKey"

        val systemInstruction = "You are a professional image prompt engineer. Expand the following short text into a high-quality, cinematic image prompt. Include cultural details, lighting, and textures. If the text mentions Nigerian context (like Bowen University or Pidgin), make sure the prompt reflects that accurately. Return ONLY the expanded prompt text. Original text: "

        val requestBody = mapOf(
            "contents" to listOf(
                mapOf("parts" to listOf(mapOf("text" to "$systemInstruction $rawPrompt")))
            )
        )

        val response = restTemplate.postForEntity(url, requestBody, Map::class.java)
        val candidates = response.body?.get("candidates") as? List<Map<String, Any>>
        val content = candidates?.get(0)?.get("content") as? Map<String, Any>
        val parts = content?.get("parts") as? List<Map<String, Any>>

        return parts?.get(0)?.get("text") as? String ?: rawPrompt
    }
}