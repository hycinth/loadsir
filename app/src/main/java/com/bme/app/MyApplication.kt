package com.bme.app

import android.app.Application
import com.hycinth.loadsir.callback.ProgressCallback
import com.hycinth.loadsir.core.LoadSir

class MyApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        LoadSir.beginBuilder().addCallback(ProgressCallback.Builder().build())
    }
}