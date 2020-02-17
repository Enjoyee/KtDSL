package com.glimmer.requestrx.schedulers

import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * 线程调度器封装类
 */
class SchedulersCompat private constructor() {

    companion object {
        fun <T> applyComputation(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applyIo(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applyNew(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applyTrampoline(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.trampoline())
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }

        fun <T> applyExecutor(): ObservableTransformer<T, T> {
            return ObservableTransformer {
                it.subscribeOn(Schedulers.from(ExecutorManager.eventExecutor))
                    .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

}