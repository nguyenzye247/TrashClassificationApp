package com.dut.trashdetect.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelProviderFactory(private val input: BaseInput) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {

        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}
