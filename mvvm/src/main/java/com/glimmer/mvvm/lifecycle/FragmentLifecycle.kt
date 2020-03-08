package com.glimmer.mvvm.lifecycle

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.glimmer.mvvm.delegate.FragmentDelegate
import com.glimmer.mvvm.delegate.FragmentDelegateImpl
import com.glimmer.mvvm.delegate.MvvmFragmentDelegateImpl
import com.glimmer.mvvm.view.IFragment
import com.glimmer.mvvm.view.IMvvmFragment
import com.glimmer.uutil.L

object FragmentLifecycle : FragmentManager.FragmentLifecycleCallbacks() {
    private val cacheDelegate by lazy { HashMap<String, FragmentDelegate>() }
    private lateinit var fragmentDelegate: FragmentDelegate

    override fun onFragmentAttached(fm: FragmentManager, f: Fragment, context: Context) {
        L.v("Fragment界面：%s onAttach", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onAttached(context) }
    }

    override fun onFragmentCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        L.v("Fragment界面：%s onCreate", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onCreated(savedInstanceState) }
    }

    override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
        L.v("Fragment界面：%s onViewCreate", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onViewCreated(v, savedInstanceState) }
    }

    override fun onFragmentActivityCreated(fm: FragmentManager, f: Fragment, savedInstanceState: Bundle?) {
        L.v("Fragment界面：%s onActivityCreated", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onActivityCreate(savedInstanceState) }
    }

    override fun onFragmentStarted(fm: FragmentManager, f: Fragment) {
        L.v("Fragment界面：%s onStart", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onStarted() }
    }

    override fun onFragmentResumed(fm: FragmentManager, f: Fragment) {
        L.v("Fragment界面：%s onResume", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onResumed() }
    }

    override fun onFragmentPaused(fm: FragmentManager, f: Fragment) {
        L.v("Fragment界面：%s onPause", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onPaused() }
    }

    override fun onFragmentStopped(fm: FragmentManager, f: Fragment) {
        L.v("Fragment界面：%s onStop", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onStopped() }
    }

    override fun onFragmentSaveInstanceState(fm: FragmentManager, f: Fragment, outState: Bundle) {
        L.v("Fragment界面：%s onSaveInstanceState", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onSaveInstanceState(outState) }
    }

    override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
        L.v("Fragment界面：%s onDestroyView", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onViewDestroyed() }
    }

    override fun onFragmentDestroyed(fm: FragmentManager, f: Fragment) {
        L.v("Fragment界面：%s onDestroy", f.javaClass.canonicalName)
        forwardDelegate(fm, f) { fragmentDelegate.onDestroyed() }
    }

    override fun onFragmentDetached(fm: FragmentManager, f: Fragment) {
        L.v("Fragment界面：%s onDetach", f.javaClass.canonicalName)
        forwardDelegate(fm, f) {
            fragmentDelegate.onDetached()
            cacheDelegate.clear()
        }
    }

    private fun forwardDelegate(fm: FragmentManager, f: Fragment, block: () -> Unit) {
        if (f !is IFragment) return
        if (!this::fragmentDelegate.isInitialized || !fragmentDelegate.isAdded()) {
            val key = f.javaClass.name
            fragmentDelegate = cacheDelegate[key] ?: getDelegate(fm, f, key)
        }
        block()
    }

    private fun getDelegate(fm: FragmentManager, f: Fragment, key: String): FragmentDelegate {
        return newDelegate(fm, f).also { cacheDelegate[key] = it }
    }

    private fun newDelegate(fm: FragmentManager, f: Fragment): FragmentDelegate {
        if (f is IMvvmFragment) {
            return MvvmFragmentDelegateImpl(fm, f)
        }
        return FragmentDelegateImpl(fm, f)
    }
}