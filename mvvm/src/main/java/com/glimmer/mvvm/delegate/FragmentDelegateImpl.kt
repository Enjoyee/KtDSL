package com.glimmer.mvvm.delegate

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.glimmer.mvvm.view.IFragment

open class FragmentDelegateImpl(private val fragmentManager: FragmentManager, private val fragment: Fragment) : FragmentDelegate {
    private val iFragment = fragment as IFragment

    override fun onAttached(context: Context) {
    }

    override fun onCreated(savedInstanceState: Bundle?) {
        val clazz = fragment.javaClass.superclass
        clazz?.let {
            val field = it.getDeclaredField("mContentLayoutId")
            field.isAccessible = true
            field.set(fragment, iFragment.getLayoutId())
        } ?: throw ClassNotFoundException("fragment init layout error")
    }

    override fun onViewCreated(v: View, savedInstanceState: Bundle?) {
        iFragment.initBefore()
        iFragment.initView()
        iFragment.initData()
    }

    override fun isAdded(): Boolean = fragment.isAdded

    override fun onActivityCreate(savedInstanceState: Bundle?) {
    }

    override fun onStarted() {
    }

    override fun onResumed() {
    }

    override fun onPaused() {
    }

    override fun onStopped() {
    }

    override fun onSaveInstanceState(outState: Bundle) {
    }

    override fun onViewDestroyed() {
    }

    override fun onDestroyed() {
    }

    override fun onDetached() {
    }
}