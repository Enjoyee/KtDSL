package com.glimmer.requestdsl.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glimmer.requestdsl.model.RequestViewModel
import kotlinx.coroutines.cancel

/**
 * @author Glimmer
 * 2020/02/01
 */
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}