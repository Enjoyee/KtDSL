package com.glimmer.requestdsl.model

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * @author Glimmer
 * 2020/02/03
 */
open class RequestViewModel : ViewModel() {
    open val apiException: MutableLiveData<Throwable> = MutableLiveData()
    open val apiLoading: MutableLiveData<Boolean> = MutableLiveData()

    /*=======================================================================*/
    private fun <Response> api(apiDSL: APIDsl<Response>.() -> Unit) {
        APIDsl<Response>().apply(apiDSL).launch(viewModelScope)
    }

    /*=======================================================================*/
    @JvmOverloads
    protected fun <Response> apiCallback(
        request: suspend () -> Response,
        onResponse: ((Response) -> Unit),
        onStart: (() -> Boolean)? = null,
        onError: ((Exception) -> Boolean)? = null,
        onFinally: (() -> Boolean)? = null
    ) {
        api<Response> {
            onRequest {
                request.invoke()
            }

            onResponse {
                onResponse.invoke(it)
            }

            onStart {
                val override = onStart?.invoke()
                if (override == null || !override) {
                    onApiStart()
                }
                false
            }

            onError {
                val override = onError?.invoke(it)
                if (override == null || !override) {
                    onApiError(it)
                }
                false
            }

            onFinally {
                val override = onFinally?.invoke()
                if (override == null || !override) {
                    onApiFinally()
                }
                false
            }
        }
    }

    /*=======================================================================*/
    protected fun <Response> apiDsl(apiDSL: APIDsl<Response>.() -> Unit) {
        api<Response> {
            onRequest {
                APIDsl<Response>().apply(apiDSL).request()
            }

            onResponse {
                APIDsl<Response>().apply(apiDSL).onResponse?.invoke(it)
            }

            onStart {
                val override = APIDsl<Response>().apply(apiDSL).onStart?.invoke()
                if (override == null || !override) {
                    onApiStart()
                }
                override
            }

            onError { error ->
                val override = APIDsl<Response>().apply(apiDSL).onError?.invoke(error)
                if (override == null || !override) {
                    onApiError(error)
                }
                override
            }

            onFinally {
                val override = APIDsl<Response>().apply(apiDSL).onFinally?.invoke()
                if (override == null || !override) {
                    onApiFinally()
                }
                override
            }
        }
    }

    /*=======================================================================*/
    protected fun <Response> apiLiveData(
        context: CoroutineContext = EmptyCoroutineContext,
        timeoutInMs: Long = 3000L,
        request: suspend () -> Response
    ): LiveData<Result<Response>> {
        return liveData(context, timeoutInMs) {
            emit(Result.Start())
            try {
                emit(withContext(Dispatchers.IO) {
                    Result.Response(request())
                })
            } catch (e: Exception) {
                e.printStackTrace()
                emit(Result.Error(e))
            } finally {
                emit(Result.Finally())
            }
        }
    }

    /*=======================================================================*/
    protected open fun onApiStart() {
        apiLoading.value = true
    }

    protected open fun onApiError(e: Exception?) {
        apiLoading.value = false
        apiException.value = e
    }

    protected open fun onApiFinally() {
        apiLoading.value = false
    }

    /*=================Result必须加泛型 不然response的泛型就会被擦除=================*/
    sealed class Result<T> {
        class Start<T> : Result<T>()
        data class Response<T>(val response: T) : Result<T>()
        data class Error<T>(val exception: Exception) : Result<T>()
        class Finally<T> : Result<T>()
    }
    /*=======================================================================*/
}

