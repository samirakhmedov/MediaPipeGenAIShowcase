package com.genaillm

import cocoapods.MediaPipeTasksGenAI.MPPLLMInference
import cocoapods.MediaPipeTasksGenAI.MPPLLMInferenceOptions
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle

class AppleLlmInference: ILlmInference {
    @OptIn(ExperimentalForeignApi::class)
    lateinit var inference: MPPLLMInference

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun initModel(path: String): String? {
        val modelPath = NSBundle.mainBundle.pathForResource(path, ofType = "bin")

        if (modelPath == null) throw Exception()

        val options = MPPLLMInferenceOptions()

        options.setModelPath(modelPath)
        options.setMaxTokens(1000)
        options.setTopk(40)
        options.setTemperature(0.8F)
        options.setRandomSeed(101)

        inference = MPPLLMInference(options, null)

        return null
    }

    @OptIn(ExperimentalForeignApi::class)
    override suspend fun generateResponse(inputText: String): String {
        return inference.generateResponseWithInputText(inputText, null) ?: throw Exception()
    }
}

actual fun createInference(): ILlmInference = AppleLlmInference()

actual val modelPath: String = "gemma2-2b-it-gpu-int8"