package com.example.flirtcompose

import android.app.Application
import com.example.flirtcompose.data.AppContainer
import com.example.flirtcompose.data.DefaultAppContainer

class RequestApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}