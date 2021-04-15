package com.kisan.cookbook.views.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.kisan.cookbook.databinding.ActivitySignInBinding
import com.kisan.cookbook.utils.Status
import com.kisan.cookbook.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setUpObserver()

        binding.apply {

            signUp.setOnClickListener {
                startActivity(Intent(this@SignInActivity, SignUpActivity::class.java))
            }
            skipBtn.setOnClickListener {
                authViewModel.signInAnonymously()
            }
            forgetPass.setOnClickListener {
                startActivity(Intent(this@SignInActivity, ForgotPasswordActivity::class.java))
            }
        }

    }

    private fun setUpObserver() {
        authViewModel.signInAnonymouslyResult.observe(this, {
            when (it.status) {
                Status.SUCCESS -> {
                    Log.d("DEBUG", it.data.toString())
                }
                Status.LOADING -> {
                }
                Status.ERROR -> {
                    Log.d("DEBUG", it.message.toString())
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        })

        authViewModel.userStateLiveData.observe(this, {
            if (it != null) {
                goToMainActivity()
            }
        })
    }

    private fun goToMainActivity() {
        startActivity(Intent(this@SignInActivity, MainActivity::class.java))
    }

}