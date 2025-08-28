package com.genaillm.di

import com.genaillm.ILlmInference
import com.genaillm.LlmViewModel
import com.genaillm.createInference
import org.koin.dsl.module

val androidModule = module {
    single<ILlmInference> { createInference() }
    single { LlmViewModel(get()) }
}