package com.glimmer.mvvm.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.glimmer.mvvm.Hammer
import com.glimmer.mvvm.common.ActivityManager
import com.glimmer.uutil.KLog

object ActivityLifecycle : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        if (Hammer.mConfig.mShowViewLifecycleLog.invoke()) {
            KLog.v("Activity界面：%s onCreate", activity?.javaClass?.canonicalName)
        }
        activity?.let { ActivityManager.add(it) }
        registerFragmentCallback(activity)
    }

    private fun registerFragmentCallback(activity: Activity?) {
        if (activity !is FragmentActivity) return
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycle, true)
    }

    override fun onActivityStarted(activity: Activity?) {
        if (Hammer.mConfig.mShowViewLifecycleLog.invoke()) {
            KLog.v("Activity界面：%s onStart", activity?.javaClass?.canonicalName)
        }
    }

    override fun onActivityResumed(activity: Activity?) {
        if (Hammer.mConfig.mShowViewLifecycleLog.invoke()) {
            KLog.v("Activity界面：%s onResume", activity?.javaClass?.canonicalName)
        }
    }

    override fun onActivityPaused(activity: Activity?) {
        if (Hammer.mConfig.mShowViewLifecycleLog.invoke()) {
            KLog.v("Activity界面：%s onPause", activity?.javaClass?.canonicalName)
        }
    }

    override fun onActivityStopped(activity: Activity?) {
        if (Hammer.mConfig.mShowViewLifecycleLog.invoke()) {
            KLog.v("Activity界面：%s onStop", activity?.javaClass?.canonicalName)
        }
    }

    override fun onActivityDestroyed(activity: Activity?) {
        if (Hammer.mConfig.mShowViewLifecycleLog.invoke()) {
            KLog.v("Activity界面：%s onDestroy", activity?.javaClass?.canonicalName)
        }
        activity?.let { ActivityManager.remove(it) }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        if (Hammer.mConfig.mShowViewLifecycleLog.invoke()) {
            KLog.v("Activity界面：%s onSaveInstanceState", activity?.javaClass?.canonicalName)
        }
    }

}