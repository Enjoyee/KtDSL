package com.glimmer.requestdsl.model

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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
        onStart: (() -> Unit)? = null,
        onError: ((Exception) -> Unit)? = null,
        onFinally: (() -> Unit)? = null
    ) {
        api<Response> {
            onRequest {
                request.invoke()
            }

            onResponse {
                onResponse.invoke(it)
            }

            onStart {
                apiLoading.value = true
                onStart?.invoke()
            }

            onError { error ->
                apiLoading.value = false
                apiException.value = error
                onError?.invoke(error)
            }

            onFinally {
                apiLoading.value = false
                onFinally?.invoke()
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
                apiLoading.value = true
            }

            onError { error ->
                apiLoading.value = false
                apiException.value = error
            }

            onFinally {
                apiLoading.value = false
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
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

