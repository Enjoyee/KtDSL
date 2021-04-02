package com.glimmer.lib

import android.app.Activity
import android.app.Application
import android.os.Bundle

class App : Application() {

    interface FrontBackListener {
        fun callback(frontToBack: Boolean)
    }

    var frontBackListener: FrontBackListener? = null

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            var visibleCount = 0

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
                if (visibleCount == 0) {
                    frontBackListener?.callback(false)
                }
                visibleCount++
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
                visibleCount--
                if (visibleCount == 0) {
                    frontBackListener?.callback(true)
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }

        })
    }

}