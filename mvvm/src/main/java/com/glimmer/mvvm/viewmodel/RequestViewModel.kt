package com.glimmer.mvvm.viewmodel

import androidx.lifecycle.*
import com.glimmer.requestdsl.request.APIDsl
import com.glimmer.uutil.L
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

open class RequestViewModel : ViewModel() {
    val apiException: MutableLiveData<Throwable> = MutableLiveData()
    val apiLoading: MutableLiveData<Boolean> = MutableLiveData()

    /*=======================================================================*/
    private fun <Response> api(apiDSL: APIDsl<Response>.() -> Unit) {
        APIDsl<Response>().apply(apiDSL).launch(viewModelScope)
    }

    /*=======================================================================*/
    @JvmOverloads
    protected fun <Response> apiCallback(
        onStart: (() -> Unit)? = null,
        request: suspend () -> Response,
        onResponse: ((Response) -> Unit),
        onError: ((Exception) -> Unit)? = null,
        onFinally: (() -> Unit)? = null
    ) {
        api<Response> {

            onStart {
                apiLoading.value = true
                onStart?.invoke()
            }

            onRequest {
                request.invoke()
            }

            onResponse {
                onResponse.invoke(it)
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

            onStart {
                apiLoading.value = true
                APIDsl<Response>().apply(apiDSL).onStart?.invoke()
            }

            onRequest {
                APIDsl<Response>().apply(apiDSL).request()
            }

            onResponse {
                APIDsl<Response>().apply(apiDSL).onResponse?.invoke(it)
            }

            onError { error ->
                apiLoading.value = false
                apiException.value = error
                APIDsl<Response>().apply(apiDSL).onError?.invoke(error)
            }

            onFinally {
                apiLoading.value = false
                APIDsl<Response>().apply(apiDSL).onFinally?.invoke()
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
                L.e(e, "网络请求出错")
                emit(Result.Error<Response>(e))
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
}

