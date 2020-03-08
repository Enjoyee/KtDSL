package com.glimmer.mvvm.viewmodel

import androidx.lifecycle.MutableLiveData
import com.glimmer.mvvm.bean.SharedData

open class BaseVM : RequestViewModel() {
    val sharedData by lazy { MutableLiveData<SharedData>() }
}