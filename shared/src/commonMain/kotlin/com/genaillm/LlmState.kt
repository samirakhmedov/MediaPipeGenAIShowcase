package com.genaillm

sealed class LlmState() {
    class Initial() : LlmState()
    class Loading() : LlmState()
    class Ready() : LlmState()
    class Error() : LlmState()
    data class Answer(val answer: String) : LlmState()
}