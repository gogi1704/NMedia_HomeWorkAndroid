package com.example.nmedia.application

import android.app.Application
import com.example.nmedia.auth.AppAuth

class NMediaApplication: Application() {
    override fun onCreate() {
        AppAuth.initApp(this)
        super.onCreate()
    }
}