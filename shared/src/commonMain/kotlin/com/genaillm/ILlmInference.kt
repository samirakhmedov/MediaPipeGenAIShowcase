package com.genaillm

import kotlinx.coroutines.flow.Flow

interface ILlmInference {

    /**
     * To load the model into the current context.
     * @return 1. null if successful, 2. an error message if failed.
     */
    suspend fun initModel(path: String): String?

    /**
     * To generate response for a given inputText in a synchronous way.
     */
    suspend fun generateResponse(inputText: String): String
}