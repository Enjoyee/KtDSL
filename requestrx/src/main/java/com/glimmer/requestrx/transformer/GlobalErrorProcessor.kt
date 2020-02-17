package com.glimmer.requestrx.transformer

import android.app.Activity
import android.content.Intent
import android.net.ParseException
import com.glimmer.requestrx.entity.IBaseEntity
import com.glimmer.requestrx.request.Request
import com.glimmer.requestrx.utils.OkLog
import com.glimmer.requestrx.utils.toast
import com.google.gson.JsonParseException
import io.reactivex.Observable
import org.json.JSONException
import retrofit2.HttpException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.UnknownHostException

object GlobalErrorProcessor {

    fun <T : IBaseEntity> processGlobalApiError(activity: Activity?, showErrToast: Boolean = true): GlobalErrorTransformer<T> =
        GlobalErrorTransformer(
            onErrorConsumer = { throwable ->
                OkLog.start("GlobalErrorProcessor")
                val msg = when (throwable) {
                    is HttpException -> Request.getBuilder()?.BAD_NETWORK_MSG ?: "网络出错"
                    is ConnectException, is UnknownHostException -> Request.getBuilder()?.CONNECT_ERROR_MSG ?: "连接服务器失败"
                    is InterruptedIOException -> Request.getBuilder()?.CONNECT_TIMEOUT_MSG ?: "连接超时"
                    is JsonParseException, is JSONException, is ParseException -> Request.getBuilder()?.PARSE_ERROR_MSG ?: "解析数据失败"
                    else -> throwable.localizedMessage ?: Request.getBuilder()?.OTHER_MSG
                }
                OkLog.log("$throwable")
                if (showErrToast) {
                    activity?.runOnUiThread { activity.toast(msg) }
                }
                OkLog.end("GlobalErrorProcessor")
            },

            onNextInterceptor = {
                if (it.isSuccess()) {
                    Observable.just(it)
                } else {
                    if (it.getCode() == Request.getBuilder()?.mTokenLostCode) {
                        activity?.runOnUiThread {
                            Request.getBuilder()?.mLoginCls?.let { cls ->
                                activity.startActivity(Intent(activity, cls))
                            }
                        }
                    }
                    Observable.error<T>(Throwable(it.getMsg()))
                }
            }
        )
}