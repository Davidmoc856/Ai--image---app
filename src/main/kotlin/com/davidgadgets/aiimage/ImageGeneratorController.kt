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

    @Value("\${HF_TOKEN:}")
    private lateinit var hfToken: String

    @Value("\${HF_MODEL_URL:}")
    private lateinit var modelUrl : String

    private val restTemplate = RestTemplate()

    @PostMapping("/generate")
    fun generate(@RequestBody request: Map<String, String>): ResponseEntity<Map<String, Any>> {
        val prompt = request["prompt"] ?: return ResponseEntity.badRequest().body(mapOf("error" to "Prompt is required"))

        // We will store all 4 images here
        val imageList = mutableListOf<String>()

        try {
            val headers = HttpHeaders()
            headers.set("Authorization", "Bearer $hfToken")
            headers.contentType = MediaType.APPLICATION_JSON
            headers.set("Accept", "image/png")

            // Generate 4 variations
            for (_i in 1..4) {
                // We add a tiny random seed to the prompt so each image is different
                val uniquePrompt = "$prompt (variation ${System.currentTimeMillis() % 1000})"

                val requestBody = mapOf(
                    "inputs" to uniquePrompt,
                    "parameters" to mapOf("num_inference_steps" to 4) // FLUX Schnell is fast!
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
            val friendlyMessage = when (e.statusCode.value()) {
                402 -> "Server energy depleted! Please return in 24 hours. 🔋"
                429 -> "Slow down! The AI is overwhelmed. Try again in a minute."
                else -> "AI hiccup: ${e.message}"
            }
            return ResponseEntity.status(e.statusCode).body(mapOf("error" to friendlyMessage))
        } catch (_: Exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mapOf("error" to "Connection issue! Check your internet."))
        }
    }
}