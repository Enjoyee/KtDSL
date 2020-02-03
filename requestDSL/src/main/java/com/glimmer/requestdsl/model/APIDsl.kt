package com.glimmer.requestdsl.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class APIDsl<Response> {
    internal lateinit var request: suspend () -> Response
    internal var onStart: (() -> Unit)? = null
    internal var onResponse: ((Response) -> Unit)? = null
    internal var onError: ((Exception) -> Unit)? = null
    internal var onFinally: (() -> Unit)? = null

    /*=======================================================================*/
    fun onStart(onStart: (() -> Unit)?) {
        this.onStart = onStart
    }

    fun onRequest(request: suspend () -> Response) {
        this.request = request
    }

    fun onResponse(onResponse: ((Response) -> Unit)?) {
        this.onResponse = onResponse
    }

    fun onError(onError: ((Exception) -> Unit)?) {
        this.onError = onError
    }

    fun onFinally(onFinally: (() -> Unit)?) {
        this.onFinally = onFinally
    }

    /*=======================================================================*/
    internal fun launch(viewModelScope: CoroutineScope) {
        viewModelScope.launch(context = Dispatchers.Main) {
            onStart?.invoke()
            try {
                val response = withContext(Dispatchers.IO) { request() }
                onResponse?.invoke(response)
            } catch (e: Exception) {
                e.printStackTrace()
                onError?.invoke(e)
            } finally {
                onFinally?.invoke()
            }
        }
    }

}