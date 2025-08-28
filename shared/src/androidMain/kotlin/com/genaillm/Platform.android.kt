package com.genaillm

import android.content.Context
import com.google.mediapipe.tasks.genai.llminference.LlmInference
import com.google.mediapipe.tasks.genai.llminference.LlmInference.LlmInferenceOptions

class AndroidLlmInference(val context: Context): ILlmInference {
    lateinit var llmInference: LlmInference

    override suspend fun initModel(path: String): String? {
        // Set the configuration options for the LLM Inference task
        val taskOptions = LlmInferenceOptions.builder()
            .setModelPath(path)
            .setMaxTopK(64)
            .build()

        // Create an instance of the LLM Inference task
        llmInference = LlmInference.createFromOptions(context, taskOptions)

        return null
    }

    override suspend fun generateResponse(inputText: String): String {
        return llmInference.generateResponse(inputText)
    }
}

object AndroidPlatform {
    lateinit var applicationContext: Context
    
    fun initialize(context: Context) {
        applicationContext = context
    }
}

actual fun createInference(): ILlmInference = AndroidLlmInference(AndroidPlatform.applicationContext)
actual val modelPath: String = "/data/local/tmp/llm/gemma3-270m-it-q8.task"