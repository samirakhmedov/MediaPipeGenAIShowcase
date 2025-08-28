package com.genaillm

import android.app.Application
import com.genaillm.di.androidModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidPlatform.initialize(this)

        startKoin {
            androidContext(this@MainApplication)
            modules(androidModule)
        }
    }
}