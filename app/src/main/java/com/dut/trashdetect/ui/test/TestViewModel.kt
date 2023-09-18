package com.dut.trashdetect.ui.test

import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class TestViewModel(val input: BaseInput.NoInput): BaseViewModel(input) {
    val stateFlowSwipeEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
}
