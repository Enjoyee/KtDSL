package com.glimmer.uutil

import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.LogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy

object L {
    private var loggable: Boolean = true
    private var logTag = "Glimmer"

    init {
        buildLog()
    }

    fun logTag(tag: String): L {
        logTag = tag
        return this
    }

    fun loggable(enable: Boolean): L {
        loggable = enable
        return this
    }

    fun buildLog() {
        Logger.clearLogAdapters()
        Logger.addLogAdapter(
            object : AndroidLogAdapter(
                PrettyFormatStrategy.newBuilder()
                    .methodCount(0)
                    .tag(logTag)
                    .build()
            ) {
                override fun isLoggable(priority: Int, tag: String?): Boolean {
                    return loggable
                }
            }
        )
    }

    /*===================================================*/
    fun addLogAdapter(@NonNull adapter: LogAdapter) {
        checkLoggable {
            Logger.addLogAdapter(adapter)
        }
    }

    fun log(priority: Int, @Nullable tag: String?, @Nullable message: String?, @Nullable throwable: Throwable?) {
        checkLoggable {
            Logger.log(priority, tag, message, throwable)
        }
    }

    fun d(@NonNull message: String, @Nullable vararg args: Any?) {
        checkLoggable {
            Logger.d(message, *args)
        }
    }

    fun d(@Nullable `object`: Any?) {
        checkLoggable {
            Logger.d(`object`)
        }
    }

    fun e(@Nullable throwable: Throwable? = null, @NonNull message: String, @Nullable vararg args: Any?) {
        checkLoggable {
            Logger.e(throwable, message, *args)
        }
    }

    fun i(@NonNull message: String, @Nullable vararg args: Any?) {
        checkLoggable {
            Logger.i(message, *args)
        }
    }

    fun v(@NonNull message: String, @Nullable vararg args: Any?) {
        checkLoggable {
            Logger.v(message, *args)
        }
    }

    fun w(@NonNull message: String, @Nullable vararg args: Any?) {
        checkLoggable {
            Logger.w(message, *args)
        }
    }

    fun wtf(@NonNull message: String, @Nullable vararg args: Any?) {
        checkLoggable {
            Logger.wtf(message, *args)
        }
    }

    fun json(@Nullable json: String?) {
        checkLoggable {
            Logger.json(json)
        }
    }

    fun xml(@Nullable xml: String?) {
        checkLoggable {
            Logger.xml(xml)
        }
    }

    private fun checkLoggable(block: () -> Unit) {
        if (loggable) {
            block.invoke()
        }
    }
}