package dev.divyanshgemini.playfeaturedelivery

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.android.play.core.splitcompat.SplitCompat

class AppController: Application() {
    companion object {
        const val TAG = "AppController"
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        Log.d(TAG, "attachBaseContext")

        SplitCompat.install(this)
    }
}