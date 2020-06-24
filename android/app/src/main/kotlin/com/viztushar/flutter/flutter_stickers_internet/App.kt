package com.viztushar.flutter.flutter_stickers_internet

import android.app.Activity
import android.app.Application
import androidx.annotation.CallSuper
import com.fxn.stash.Stash
import io.flutter.view.FlutterMain

class App : Application() {
    var currentActivity: Activity? = null

    @CallSuper
    override fun onCreate() {
        super.onCreate()
        Stash.init(this)
        FlutterMain.startInitialization(this)
    }

}