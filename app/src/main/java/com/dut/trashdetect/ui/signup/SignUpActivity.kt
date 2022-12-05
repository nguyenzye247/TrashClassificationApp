package com.dut.trashdetect.ui.signup

import androidx.activity.viewModels
import com.dut.trashdetect.base.BaseActivity
import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.ViewModelProviderFactory
import com.dut.trashdetect.databinding.ActivitySignUpBinding

class SignUpActivity: BaseActivity<ActivitySignUpBinding, SignUpViewModel>() {
    override fun getLazyBinding() = lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<SignUpViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {

    }
}
