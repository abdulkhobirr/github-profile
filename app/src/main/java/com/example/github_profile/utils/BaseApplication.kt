package com.example.github_profile.utils

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication: Application() {
    companion object {
        var context: Context? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()

        context = this
    }
}