package com.dut.trashdetect.ui.signup

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.dut.trashdetect.base.BaseActivity
import com.dut.trashdetect.base.BaseInput
import com.dut.trashdetect.base.ViewModelProviderFactory
import com.dut.trashdetect.databinding.ActivitySignUpBinding
import com.dut.trashdetect.model.User
import com.dut.trashdetect.ui.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : BaseActivity<ActivitySignUpBinding, SignUpViewModel>() {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseFireStore = FirebaseFirestore.getInstance()

    override fun getLazyBinding() = lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun getLazyViewModel() = viewModels<SignUpViewModel> {
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
            btnRegister.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()
                val fullName = etFullName.text.toString()
                progressBar.isVisible = true
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authResult ->
                        progressBar.isVisible = false
                        firebaseFireStore.collection("User")
                            .document(FirebaseAuth.getInstance().uid ?: "")
                            .set(User(email, fullName))
                        Toast.makeText(
                            this@SignUpActivity,
                            "Register succeeded",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            this@SignUpActivity,
                            "Error creating user",
                            Toast.LENGTH_SHORT
                        ).show()
                        progressBar.isVisible = false
                    }
            }
            tvLoginNow.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            }
            ivBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun observe() {

    }
}
