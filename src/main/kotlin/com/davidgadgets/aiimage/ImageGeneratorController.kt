package com.davidgadgets.aiimage

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.client.RestTemplate
import java.util.*

@RestController
@RequestMapping("/api/image")
@CrossOrigin(origins = ["*"]) // Allows your frontend to talk to your backend
class ImageGeneratorController {

    // Get your FREE token at: huggingface.co/settings/tokens

    @Value("\${HF_TOKEN}")
    private lateinit var hfToken: String

    @Value("\${HF_MODEL_URL}")
    private lateinit var modelUrl : String



    private val restTemplate = RestTemplate()

    @PostMapping("/generate")
    fun generate(@RequestBody request: Map<String, String>): ResponseEntity<Map<String, String>> {
        val prompt = request["prompt"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "Prompt is required"))

        try {
            val headers = HttpHeaders()
            headers.set("Authorization", "Bearer $hfToken")
            headers.contentType = MediaType.APPLICATION_JSON

            headers.set("Accept", "image/png")

            val requestBody = mapOf("inputs" to prompt, "parameters" to mapOf("num_inference_steps" to 20, "guidance_scale" to 7.5))
            val requestEntity = HttpEntity(requestBody, headers)

            val responseEntity = restTemplate.exchange(modelUrl, HttpMethod.POST, requestEntity, ByteArray::class.java)

            if (responseEntity.statusCode.is2xxSuccessful) {
                val responseBytes = responseEntity.body
                if (responseBytes != null && responseBytes.isNotEmpty()) {
                    val base64Image = Base64.getEncoder().encodeToString(responseBytes)
                    return ResponseEntity.ok(mapOf("image" to "data:image/png;base64,$base64Image"))
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "Failed to generate image"))
                }
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "Error generating image: ${responseEntity.statusCode}"))
            }
        }  catch (e: HttpClientErrorException) {
        // This catches the 402, 429, and 404 errors specifically
        val friendlyMessage = when (e.statusCode.value()) {
            402 -> "Server energy depleted for this session! Please return in 24 hours or try again later. 🔋"
            429 -> "Slow down! The AI is a bit overwhelmed. Please wait a minute before generating again."
            404 -> "The AI model is currently taking a nap. Let's try again in a few minutes."
            else -> "Unexpected AI hiccup: ${e.message}"
        }
        return ResponseEntity.status(e.statusCode).body(mapOf("error" to friendlyMessage))

    } catch (e: Exception) {
        // This catches everything else (like internet connection issues)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(mapOf("error" to "Something went wrong on our end. Please check your connection!"))
    }
    }
}