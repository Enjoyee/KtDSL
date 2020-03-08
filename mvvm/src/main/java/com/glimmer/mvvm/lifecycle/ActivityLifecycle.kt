package com.glimmer.mvvm.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.glimmer.mvvm.commom.ActivityManager
import com.glimmer.mvvm.delegate.ActivityDelegate
import com.glimmer.mvvm.delegate.ActivityDelegateImpl
import com.glimmer.mvvm.delegate.MvvmActivityDelegateImpl
import com.glimmer.mvvm.view.IActivity
import com.glimmer.mvvm.view.IMvvmActivity
import com.glimmer.uutil.L

object ActivityLifecycle : Application.ActivityLifecycleCallbacks {
    private val cacheActivityDelegate by lazy { HashMap<String, ActivityDelegate>() }
    private lateinit var activityDelegate: ActivityDelegate

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        L.v("Activity界面：%s onCreate", activity?.javaClass?.canonicalName)
        activity?.let { ActivityManager.add(it) }
        forwardDelegate(activity) { activityDelegate.onCreate(savedInstanceState) }
        registerFragmentCallback(activity)
    }

    private fun registerFragmentCallback(activity: Activity?) {
        if (activity !is FragmentActivity) return
        activity.supportFragmentManager.registerFragmentLifecycleCallbacks(FragmentLifecycle, true)
    }

    override fun onActivityStarted(activity: Activity?) {
        L.v("Activity界面：%s onStart", activity?.javaClass?.canonicalName)
        forwardDelegate(activity) { activityDelegate.onStart() }
    }

    override fun onActivityResumed(activity: Activity?) {
        L.v("Activity界面：%s onResume", activity?.javaClass?.canonicalName)
        forwardDelegate(activity) { activityDelegate.onResume() }
    }

    override fun onActivityPaused(activity: Activity?) {
        L.v("Activity界面：%s onPause", activity?.javaClass?.canonicalName)
        forwardDelegate(activity) { activityDelegate.onPause() }
    }

    override fun onActivityStopped(activity: Activity?) {
        L.v("Activity界面：%s onStop", activity?.javaClass?.canonicalName)
        forwardDelegate(activity) { activityDelegate.onStop() }
    }

    override fun onActivityDestroyed(activity: Activity?) {
        L.v("Activity界面：%s onDestroy", activity?.javaClass?.canonicalName)
        activity?.let { ActivityManager.remove(it) }
        forwardDelegate(activity) {
            activityDelegate.onDestroy()
            cacheActivityDelegate.clear()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        L.v("Activity界面：%s onSaveInstanceState", activity?.javaClass?.canonicalName)
        forwardDelegate(activity) { activityDelegate.onSaveInstanceState(activity, outState) }
    }

    private fun forwardDelegate(activity: Activity?, block: () -> Unit) {
        if (activity !is IActivity) return
        val key = activity.javaClass.name
        activityDelegate = cacheActivityDelegate[key] ?: getDelegate(activity, key)
        block()
    }

    private fun getDelegate(activity: Activity, key: String): ActivityDelegate {
        return newDelegate(activity).also { cacheActivityDelegate[key] = it }
    }

    private fun newDelegate(activity: Activity): ActivityDelegate {
        if (activity is IMvvmActivity) {
            return MvvmActivityDelegateImpl(activity)
        }
        return ActivityDelegateImpl(activity)
    }
}