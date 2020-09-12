package com.glimmer.enjoy

import android.app.Application
import com.glimmer.mvvm.Hammer
import com.glimmer.mvvm.view.IApplication
import timber.log.Timber

class EnjoyApp : Application(), IApplication {

    companion object {
        lateinit var INSTANCE: EnjoyApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Hammer.init {
            showLog { true }
            showViewLifecycleLog { true }
            logTag { "Enjoy" }
            baseUrl { "http://www.baidu.com" }
        }
        Timber.plant(Timber.DebugTree())

        Timber.tag("测试")
        Timber.d("cececec，%d,%d", 1, 2)
    }

}