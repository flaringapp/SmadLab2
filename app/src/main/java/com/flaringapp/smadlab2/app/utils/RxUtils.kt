package com.flaringapp.app.utils

import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

object RxUtils {

    fun <T> createDelayedFlowable(publishSubject: PublishSubject<T>, delay: Long): Flowable<T> {
        var lastUpdateTime = 0L

        return publishSubject
            .toFlowable(BackpressureStrategy.LATEST)
            .flatMap {
                if (System.currentTimeMillis() - lastUpdateTime >= delay) Flowable.just(it).also {
                    lastUpdateTime = System.currentTimeMillis()
                }
                else Flowable.empty()
            }
            .onComputationThread()
    }

}

fun <T> Flowable<T>.onComputationThread(): Flowable<T> = this
    .subscribeOn(Schedulers.computation())
    .observeOn(Schedulers.computation())

fun <T> Observable<T>.onComputationThread() = this
    .subscribeOn(Schedulers.computation())
    .observeOn(Schedulers.computation())

fun <T> Single<T>.onComputationThread() = this
    .subscribeOn(Schedulers.computation())
    .observeOn(Schedulers.computation())

fun Completable.onComputationThread() = this
    .subscribeOn(Schedulers.computation())
    .observeOn(Schedulers.computation())

fun <T> Flowable<T>.onApiThread(): Flowable<T> = this
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.io())

fun <T> Observable<T>.onApiThread() = this
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.io())

fun <T> Single<T>.onApiThread() = this
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.io())

fun Completable.onApiThread() = this
    .subscribeOn(Schedulers.io())
    .observeOn(Schedulers.io())

fun <T> Flowable<T>.observeOnUI() = this
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Observable<T>.observeOnUI() = this
    .observeOn(AndroidSchedulers.mainThread())

fun <T> Single<T>.observeOnUI() = this
    .observeOn(AndroidSchedulers.mainThread())

fun Completable.observeOnUI() = this
    .observeOn(AndroidSchedulers.mainThread())

typealias Calculation<T> = () -> T
fun <T> rxCalculation(calculation: Calculation<T>): Single<T> {
    return Single.create<T> { emitter ->
        val result = calculation.invoke()
        emitter.onSuccess(result)
    }
        .onApiThread()
        .observeOnUI()
}