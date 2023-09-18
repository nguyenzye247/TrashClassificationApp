package com.dut.trashdetect.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dut.trashdetect.ui.test.TestViewModel

class ViewModelProviderFactory(private val input: BaseInput) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(TestViewModel::class.java) -> {
                return TestViewModel(input as BaseInput.NoInput) as T
            }
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
