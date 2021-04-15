package com.kisan.cookbook.views.activity

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kisan.cookbook.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.apply {
            signIn.setOnClickListener {
                startActivity(Intent(this@SignUpActivity, SignInActivity::class.java))
            }

            signUpBtn.setOnClickListener {
                val emailStr: String = emailLayout.editText?.text.toString()
                val password1: String = passLayout.editText?.text.toString()
                val password2: String = pass2Layout.editText?.text.toString()
                if (validate(emailStr, password1, password2)) {
                    Toast.makeText(applicationContext, "Validated", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun validate(email: String, p1: String, p2: String): Boolean {
        if (email.isEmpty() || p1.isEmpty() || p2.isEmpty()) {
            Toast.makeText(this, "Field cannot be empty", Toast.LENGTH_LONG).show()
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_LONG).show()
        } else if (p2 != p1) {
            Toast.makeText(this, "Password doesn't match", Toast.LENGTH_LONG).show()
        } else if (p2.length < 8 || p1.length < 8) {
            Toast.makeText(this, "Password is too short", Toast.LENGTH_LONG).show()
        } else {
            return true
        }
        return false
    }
}