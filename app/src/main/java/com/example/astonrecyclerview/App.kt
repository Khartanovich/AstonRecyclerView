package com.example.astonrecyclerview

import android.app.Application
import android.util.Log

class App: Application() {
    val repository = UserRepository()

    override fun onCreate() {
        super.onCreate()
    }
}