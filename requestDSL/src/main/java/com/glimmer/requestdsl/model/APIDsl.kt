package com.glimmer.requestdsl.model

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Glimmer
 * 2020/01/31
 */
class APIDsl<Response> {
    internal lateinit var request: suspend () -> Response
    internal var onStart: (() -> Boolean?)? = null
    internal var onResponse: ((Response) -> Unit)? = null
    internal var onError: ((Exception) -> Boolean?)? = null
    internal var onFinally: (() -> Boolean?)? = null

    /*=======================================================================*/
    fun onStart(onStart: (() -> Boolean?)?) {
        this.onStart = onStart
    }

    fun onRequest(request: suspend () -> Response) {
        this.request = request
    }

    fun onResponse(onResponse: ((Response) -> Unit)?) {
        this.onResponse = onResponse
    }

    fun onError(onError: ((Exception) -> Boolean?)?) {
        this.onError = onError
    }

    fun onFinally(onFinally: (() -> Boolean?)?) {
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