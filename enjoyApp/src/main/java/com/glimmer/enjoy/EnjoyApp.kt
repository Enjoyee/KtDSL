package com.glimmer.enjoy

import android.app.Application
import com.glimmer.mvvm.Hammer


class EnjoyApp : Application() {

    companion object {
        lateinit var INSTANCE: EnjoyApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Hammer.init {
            showLog { true }
            logTag { "Enjoy" }
            baseUrl { "http://www.baidu.com" }
        }
    }


}