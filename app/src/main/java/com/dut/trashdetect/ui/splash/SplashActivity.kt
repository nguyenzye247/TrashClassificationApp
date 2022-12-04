package com.dut.trashdetect.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.dut.trashdetect.databinding.ActivitySplashBinding
import com.dut.trashdetect.ui.main.MainActivity
import com.dut.trashdetect.ui.signin.SignInActivity
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val fireAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ActivitySplashBinding.inflate(layoutInflater).root)

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserLoggedIn()
        }, 2000)
        fireAuth.addAuthStateListener {
            if (it.currentUser == null) {
                startActivity(
                    Intent(this, SignInActivity::class.java)
                )
            }
        }
    }

    private fun checkUserLoggedIn() {
         fireAuth.currentUser?.let {
             startActivity(Intent(this@SplashActivity, MainActivity::class.java))
             finish()
         } ?: kotlin.run {
             startActivity(Intent(this@SplashActivity, SignInActivity::class.java))
             finish()
         }
    }
}
