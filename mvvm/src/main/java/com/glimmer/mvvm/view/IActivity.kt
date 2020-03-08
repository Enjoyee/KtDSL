package com.glimmer.mvvm.view

import android.os.Bundle

interface IActivity : IView {
    fun initWidows() {}

    fun initArgs(extras: Bundle?) {}
}