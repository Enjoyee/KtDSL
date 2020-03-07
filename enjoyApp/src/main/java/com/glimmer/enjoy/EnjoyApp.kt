package com.glimmer.enjoy

import android.app.Application


class EnjoyApp : Application() {
    private lateinit var INSTANCE: EnjoyApp

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        initLog()
    }

    private fun initLog() {
    }

}