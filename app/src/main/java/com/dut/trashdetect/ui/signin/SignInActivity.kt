package com.dut.trashdetect.ui.signin

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.dut.trashdetect.base.BaseActivity
import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.ViewModelProviderFactory
import com.dut.trashdetect.databinding.ActivitySignInBinding
import com.dut.trashdetect.ui.main.MainActivity
import com.dut.trashdetect.ui.signup.SignUpActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity<ActivitySignInBinding, SignInViewModel>() {
    private val firebaseAuth = FirebaseAuth.getInstance()

    override fun getLazyBinding() = lazy { ActivitySignInBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<SignInViewModel> {
        ViewModelProviderFactory(BaseInput.NoInput)
    }

    override fun setupInit() {
        initViews()
        initListeners()
        observe()
    }

    private fun initViews() {
        binding.apply {

        }
    }

    private fun initListeners() {
        binding.apply {
            btnLogin.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                progressBar.isVisible = true
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        progressBar.isVisible = false
                        Toast.makeText(this@SignInActivity, "Login success", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
                    }
                    .addOnFailureListener {
                        progressBar.isVisible = false
                        Toast.makeText(this@SignInActivity, "Login failed", Toast.LENGTH_SHORT)
                            .show()
                    }
            }
            tvRegisterNow.setOnClickListener {
                startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            }
        }
    }

    private fun observe() {

    }
}
