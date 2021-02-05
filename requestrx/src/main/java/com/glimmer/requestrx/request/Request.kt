package com.glimmer.requestrx.request

import android.content.Context
import com.glimmer.requestrx.gson.CustomizeGsonConverterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.util.concurrent.TimeUnit

/**
 * 请求
 */
object Request {
    private lateinit var mAppContext: Context
    private var mBuilder: Builder? = null
    private lateinit var mHeaders: HeaderInterceptor
    private lateinit var mOkHttpBuilder: OkHttpClient.Builder
    private lateinit var mRetrofitBuilder: Retrofit.Builder

    /*=======================================================================*/
    fun init(appContext: Context, baseUrl: String, requestBuilder: Builder? = null) {
        mAppContext = appContext.applicationContext
        mHeaders = HeaderInterceptor()
        initRetrofit(requestBuilder, baseUrl)
    }

    /*=======================================================================*/
    fun <ApiService> createApiService(apiService: Class<ApiService>): ApiService {
        return mRetrofitBuilder.build().create(apiService)
    }

    fun putHead(key: String, value: String): HeaderInterceptor {
        mHeaders.putHead(key, value)
        return mHeaders
    }

    fun getBuilder() = mBuilder

    /*=======================================================================*/
    private fun getDefaultOkHttpBuilder(appContext: Context): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .cache(Cache(appContext.cacheDir, 10 * 1024 * 1024L))
            .addInterceptor(mHeaders)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
    }

    private fun initRetrofit(requestBuilder: Builder? = null, baseUrl: String) {
        // OKHttp Builder
        val defaultOkHttpBuilder = getDefaultOkHttpBuilder(mAppContext)
        mOkHttpBuilder = requestBuilder?.mBuildOkHttp ?: defaultOkHttpBuilder
        val showLog = requestBuilder?.mShowLog ?: true
        if (showLog) {
            mOkHttpBuilder.addInterceptor(LoggingInterceptor())
        }
        // Retrofit Builder
        val retrofitBuilder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(CustomizeGsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(mOkHttpBuilder.build())
        mRetrofitBuilder = requestBuilder?.mBuildRetrofit ?: retrofitBuilder
    }

    /*=======================================================================*/
    class Builder {
        internal var mShowLog: Boolean = true
        internal var mBuildOkHttp: OkHttpClient.Builder? = null
        internal var mBuildRetrofit: Retrofit.Builder? = null
        internal var mTokenLostCode: String = "-1"

        internal var mLoginCls: Class<*>? = null

        internal var PARSE_ERROR_MSG: String = "解析数据失败"
        internal var BAD_NETWORK_MSG: String = "网络出错"
        internal var CONNECT_ERROR_MSG: String = "连接服务器失败"
        internal var CONNECT_TIMEOUT_MSG: String = "连接超时"
        internal var OTHER_MSG: String = "未知错误"

        fun showLog(showLog: Boolean) = apply {
            this.mShowLog = showLog
        }

        fun okHttpBuilder(buildOkHttp: OkHttpClient.Builder) = apply {
            this.mBuildOkHttp = buildOkHttp
        }

        fun buildRetrofit(buildRetrofit: Retrofit.Builder) = apply {
            this.mBuildRetrofit = buildRetrofit
        }

        fun tokenLostCode(code: String) = apply {
            this.mTokenLostCode = code
        }

        fun loginCls(cls: Class<*>) = apply {
            this.mLoginCls = cls
        }

        fun msgParseErr(msg: String) = apply {
            this.PARSE_ERROR_MSG = msg
        }

        fun msgBadNetWork(msg: String) = apply {
            this.BAD_NETWORK_MSG = msg
        }

        fun msgConnectErr(msg: String) = apply {
            this.CONNECT_ERROR_MSG = msg
        }

        fun msgConnectTimeout(msg: String) = apply {
            this.CONNECT_TIMEOUT_MSG = msg
        }

        fun msgOther(msg: String) = apply {
            this.OTHER_MSG = msg
        }

    }
}
