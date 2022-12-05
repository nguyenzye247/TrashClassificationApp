package com.dut.trashdetect.base

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

open class BaseViewModel(val inputData: BaseInput) : ViewModel() {
    protected val subscription: CompositeDisposable = CompositeDisposable()

    protected fun addDisposables(vararg ds: Disposable) {
        ds.forEach { subscription.add(it) }
    }

    override fun onCleared() {
        super.onCleared()
        subscription.clear()
    }
}
