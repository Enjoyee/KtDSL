package com.glimmer.enjoy

import android.app.Application
import com.glimmer.enjoy.fund.repository.ToStringConverterFactory
import com.glimmer.mvvm.Hammer
import com.glimmer.mvvm.view.IApplication
import com.glimmer.requestdsl.request.RequestDSL
import com.hz.statistics.EvilKt
import retrofit2.Retrofit

class EnjoyApp : Application(), IApplication {

    companion object {
        lateinit var INSTANCE: EnjoyApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        Hammer.init {
            showLog { true }
            showViewLifecycleLog { false }
            logTag { "FundApp" }
            retrofit {
                Retrofit.Builder()
                    .baseUrl("http://www.github.com")
                    .addConverterFactory(ToStringConverterFactory())
                    .client(RequestDSL.getDefaultOkHttpBuilder(INSTANCE).build())
            }
        }
    }

}