package com.dut.trashdetect.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import io.reactivex.rxjava3.disposables.CompositeDisposable

abstract class BaseActivity<T : ViewBinding, V : BaseViewModel> : AppCompatActivity() {
    protected val subscription: CompositeDisposable = CompositeDisposable()
    abstract fun getLazyBinding(): Lazy<T>
    abstract fun getLazyViewModel(): Lazy<V>
    abstract fun setupInit()

    protected val binding by getLazyBinding()
    protected val viewModel by getLazyViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupInit()
    }

    override fun onDestroy() {
        super.onDestroy()
        subscription.clear()
    }
}