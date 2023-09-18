package com.dut.trashdetect.ui.test

import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.dut.trashdetect.base.BaseActivity
import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.ViewModelProviderFactory
import com.dut.trashdetect.databinding.TestLayoutBinding
import kotlinx.coroutines.launch

class TestActivity: BaseActivity<TestLayoutBinding, TestViewModel>() {
    override fun getLazyBinding() = lazy { TestLayoutBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<TestViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {
        binding.scrollView.viewTreeObserver.addOnScrollChangedListener {
            val enableSwipeToRefresh = binding.scrollView.scrollY != 0
            viewModel.stateFlowSwipeEnabled.value = !enableSwipeToRefresh
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlowSwipeEnabled.collect { enableSwipeToRefresh ->
                    Log.d("TEST1", "enableSwipeToRefresh: ${enableSwipeToRefresh}")
                }
            }
        }
    }
}
