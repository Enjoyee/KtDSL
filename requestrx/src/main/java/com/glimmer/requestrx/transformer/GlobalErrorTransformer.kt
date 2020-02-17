package com.glimmer.requestrx.transformer

import io.reactivex.*

typealias OnNextInterceptor<T> = (T) -> Observable<T>
typealias OnErrorConsumer = (Throwable) -> Unit

open class GlobalErrorTransformer<T> constructor(
    private val onNextInterceptor: OnNextInterceptor<T> = { Observable.just(it) },
    private val onErrorConsumer: OnErrorConsumer = { }
) : ObservableTransformer<T, T>, FlowableTransformer<T, T>, SingleTransformer<T, T>, MaybeTransformer<T, T>, CompletableTransformer {

    override fun apply(upstream: Observable<T>): Observable<T> = upstream.flatMap { onNextInterceptor(it) }.doOnError(onErrorConsumer)

    override fun apply(upstream: Completable): Completable = upstream.doOnError(onErrorConsumer)

    override fun apply(upstream: Flowable<T>): Flowable<T> = upstream.flatMap { onNextInterceptor(it).toFlowable(BackpressureStrategy.BUFFER) }.doOnError(onErrorConsumer)

    override fun apply(upstream: Maybe<T>): Maybe<T> = upstream.flatMap { onNextInterceptor(it).firstElement() }.doOnError(onErrorConsumer)

    override fun apply(upstream: Single<T>): Single<T> = upstream.flatMap { onNextInterceptor(it).firstOrError() }.doOnError(onErrorConsumer)
}