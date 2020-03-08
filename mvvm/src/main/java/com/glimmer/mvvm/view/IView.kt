package com.glimmer.mvvm.view

interface IView {
    fun getLayoutId(): Int

    fun initBefore() {}

    fun initView() {}

    fun initData() {}
}