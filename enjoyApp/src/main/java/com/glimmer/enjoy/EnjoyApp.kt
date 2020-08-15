package com.glimmer.enjoy

import android.app.Application
import com.glimmer.mvvm.Hammer
import com.glimmer.mvvm.view.IApplication
import com.glimmer.uutil.KLog

class EnjoyApp : Application(), IApplication {

    companion object {
        lateinit var INSTANCE: EnjoyApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Hammer.init {
            showLog { true }
            showViewLifecycleLog { false }
            logTag { "Enjoy" }
            baseUrl { "http://www.baidu.com" }
        }
        KLog.d("Application===>$this")
    }

}