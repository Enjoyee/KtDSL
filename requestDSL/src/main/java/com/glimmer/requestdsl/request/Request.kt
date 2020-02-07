package com.glimmer.requestdsl.request

import android.content.Context
import com.glimmer.requestdsl.gson.CustomizeGsonConverterFactory
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
    fun init(appContext: Context, baseUrl: String, requestDsl: (RequestDsl.() -> Unit)? = null) {
        mAppContext = appContext.applicationContext
        mHeaders = HeaderInterceptor()
        initRetrofit(requestDsl, baseUrl)
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
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
    }

    private fun initRetrofit(requestDsl: (RequestDsl.() -> Unit)?, baseUrl: String) {
        // dsl
        val dsl = if (requestDsl != null) RequestDsl().also(requestDsl) else null
        // OKHttp Builder
        val defaultOkHttpBuilder = getDefaultOkHttpBuilder(mAppContext)
        mOkHttpBuilder = dsl?.mBuildOkHttp?.invoke(defaultOkHttpBuilder) ?: defaultOkHttpBuilder
        val showLog = dsl?.mShowLog ?: true
        if (showLog) {
            mOkHttpBuilder.addInterceptor(LoggingInterceptor())
        }
        // Retrofit Builder
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(CustomizeGsonConverterFactory.create())
            .client(mOkHttpBuilder.build())
        mRetrofitBuilder = dsl?.mBuildRetrofit?.invoke(retrofitBuilder) ?: retrofitBuilder
    }

    /*=======================================================================*/
}

class RequestDsl {
    internal var mBuildOkHttp: ((OkHttpClient.Builder) -> OkHttpClient.Builder)? = null
    internal var mBuildRetrofit: ((Retrofit.Builder) -> Retrofit.Builder)? = null
    internal var mShowLog: Boolean? = null

    fun okHttp(builder: ((OkHttpClient.Builder) -> OkHttpClient.Builder)?) {
        mBuildOkHttp = builder
    }

    fun retrofit(builder: ((Retrofit.Builder) -> Retrofit.Builder)?) {
        mBuildRetrofit = builder
    }

}