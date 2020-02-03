package com.glimmer.requestdsl.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.glimmer.requestdsl.model.RequestViewModel
import kotlinx.coroutines.cancel

open class BaseVM : RequestViewModel() {
    val toastLiveData = MutableLiveData<String>()

    fun showLoading() {
        apiLoading.value = true
    }

    fun hideLoading() {
        apiLoading.value = false
    }

    fun showToast(message: String) {
        toastLiveData.value = message
    }

}