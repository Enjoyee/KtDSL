package com.glimmer.requestrx.request

import android.content.Context
import com.glimmer.requestrx.gson.CustomizeGsonConverterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * 请求
 */
object Request {
    private lateinit var mAppContext: Context
    private lateinit var mHeaders: HeaderInterceptor
    private lateinit var mOkHttpBuilder: OkHttpClient.Builder
    private lateinit var mRetrofitBuilder: Retrofit.Builder

    /*=======================================================================*/
    fun init(appContext: Context, baseUrl: String, requestBuilder: (() -> Builder)? = null) {
        mAppContext = appContext.applicationContext
        mHeaders = HeaderInterceptor()
        initRetrofit(requestBuilder, baseUrl)
    }

    /*=======================================================================*/
    fun <ApiService> createApiService(apiService: Class<ApiService>): ApiService {
        return mRetrofitBuilder.build().create(apiService)
    }

    fun putHead(key: String, value: String): HeaderInterceptor {
        mHeaders.put(key, value)
        return mHeaders
    }

    /*=======================================================================*/
    private fun getDefaultOkHttpBuilder(appContext: Context): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .cache(Cache(appContext.cacheDir, 10 * 1024 * 1024L))
            .addInterceptor(mHeaders)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
    }

    private fun initRetrofit(requestBuilder: (() -> Builder)? = null, baseUrl: String) {
        // OKHttp Builder
        val defaultOkHttpBuilder = getDefaultOkHttpBuilder(mAppContext)
        mOkHttpBuilder = requestBuilder?.invoke()?.mBuildOkHttp ?: defaultOkHttpBuilder
        val showLog = requestBuilder?.invoke()?.mShowLog ?: true
        if (showLog) {
            mOkHttpBuilder.addInterceptor(LoggingInterceptor())
        }
        // Retrofit Builder
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(CustomizeGsonConverterFactory.create())
            .client(mOkHttpBuilder.build())
        mRetrofitBuilder = requestBuilder?.invoke()?.mBuildRetrofit ?: retrofitBuilder
    }

    /*=======================================================================*/
    class Builder constructor() {
        internal var mShowLog: Boolean = true
        internal var mBuildOkHttp: OkHttpClient.Builder? = null
        internal var mBuildRetrofit: Retrofit.Builder? = null

        fun showLog(showLog: Boolean) = apply {
            this.mShowLog = showLog
        }

        fun okHttpBuilder(buildOkHttp: () -> OkHttpClient.Builder) = apply {
            this.mBuildOkHttp = buildOkHttp.invoke()
        }

        fun buildRetrofit(buildRetrofit: () -> Retrofit.Builder) = apply {
            this.mBuildRetrofit = buildRetrofit.invoke()
        }
    }
}
