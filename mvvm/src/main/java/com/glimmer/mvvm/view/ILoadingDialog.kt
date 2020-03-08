package com.glimmer.mvvm.view

interface ILoadingDialog {
    fun showLoadingDialog()

    fun dismissLoadingDialog()

    fun isShowing(): Boolean
}