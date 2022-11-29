package com.dut.trashdetect.ui.signin

import androidx.activity.viewModels
import com.dut.trashdetect.base.BaseActivity
import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.ViewModelProviderFactory
import com.dut.trashdetect.databinding.ActivitySignInBinding

class SignInActivity: BaseActivity<ActivitySignInBinding, SignInViewModel>(){
    override fun getLazyBinding() = lazy {ActivitySignInBinding.inflate(layoutInflater)}

    override fun getLazyViewModel() = viewModels<SignInViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {

    }
}
