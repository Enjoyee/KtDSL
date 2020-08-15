package com.glimmer.enjoy.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glimmer.mvvm.viewmodel.BaseVM

class HomeVM : BaseVM() {
    private var index = 0
    private val _testData = MutableLiveData<String>().apply { value = "防重点击跳转" }
    val testData1: LiveData<String> = MutableLiveData<String>().apply { value = "跳转列表" }
    val testData: LiveData<String> = _testData

    fun change() {
        index++
        _testData.value = "防重点击跳转，次数：$index"
        viewModelScope
    }

}