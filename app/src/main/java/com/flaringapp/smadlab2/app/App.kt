package com.flaringapp.smadlab2.app

import android.app.Application
import com.flaringapp.smadlab2.app.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(appModules)
        }
    }
}