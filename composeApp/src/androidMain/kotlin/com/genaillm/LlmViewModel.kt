package com.genaillm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LlmViewModel(private val inference: ILlmInference) : ViewModel() {
    private val _state = MutableStateFlow<LlmState>(LlmState.Initial())
    val state: StateFlow<LlmState> = _state.asStateFlow()

    fun infer(input: String) {
        if (_state.value is LlmState.Loading || _state.value is LlmState.Error || _state.value is LlmState.Initial) return

        viewModelScope.launch {
            _state.value = LlmState.Loading()

            try {
                val answer = inference.generateResponse(input)
                _state.value = LlmState.Answer(answer)
            } catch (_: Exception) {
                _state.value = LlmState.Answer("Oops! Something went wrong!")
            }
        }
    }

    fun initialize() {
        if (_state.value is LlmState.Loading) return

        viewModelScope.launch {
            _state.value = LlmState.Loading()

            try {
                inference.initModel(modelPath)
                _state.value = LlmState.Ready()
            } catch (_: Exception) {
                _state.value = LlmState.Error()
            }
        }
    }
}