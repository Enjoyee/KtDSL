package com.glimmer.enjoy.home

import com.glimmer.mvvm.bean.SharedData
import com.glimmer.mvvm.bean.SharedType
import com.glimmer.mvvm.viewmodel.BaseVM

class HomeVM : BaseVM() {

    fun onToast() {
        sharedData.value = SharedData("测试", SharedType.Toast())
    }

}