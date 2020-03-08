package com.glimmer.mvvm.view

import android.content.Context
import com.glimmer.uutil.toast

interface IMvvmView {
    fun showToast(message: String) {
        (this as? Context)?.apply { toast(message) }
    }

    fun showToast(resId: Int) {
        (this as? Context)?.apply { toast(resId) }
    }

    fun showApiToast(message: String) {
        showToast(message)
    }

    fun getLoadingDialog(): ILoadingDialog? = null

    fun getStateView(): IStateView? = null
}