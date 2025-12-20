package com.techrent.app

import android.app.Application
import com.techrent.app.di.AppContainer

class App : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        container.seedIfNeeded()
    }
}
