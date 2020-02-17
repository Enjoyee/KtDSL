package com.glimmer.requestrx.usecase

import android.app.Activity
import com.glimmer.requestrx.entity.IBaseEntity
import com.glimmer.requestrx.schedulers.SchedulersCompat
import com.glimmer.requestrx.transformer.GlobalErrorProcessor
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

/**
 * 接口用例
 */
abstract class UseCase<T : IBaseEntity> constructor(private val activity: Activity?, private val showErrToast: Boolean = true) {
    private lateinit var mParam: HashMap<String, Any>

    open fun setParamMap(param: () -> HashMap<String, Any>): UseCase<T> {
        this.mParam = param.invoke()
        return this
    }

    abstract fun buildUseCaseObservable(): Observable<T>

    open fun build(vararg composers: ObservableTransformer<T, T>?): Observable<T> {
        var observable = buildUseCaseObservable().compose(SchedulersCompat.applyIo()).compose(GlobalErrorProcessor.processGlobalApiError<T>(activity, showErrToast))
        composers.iterator().forEach { composer ->
            composer?.apply { observable = observable.compose(composer) }
        }
        return observable
    }

}