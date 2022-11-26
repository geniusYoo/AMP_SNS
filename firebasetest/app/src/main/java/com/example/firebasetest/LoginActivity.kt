package com.example.firebasetest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasetest.databinding.ActivityLoginBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val userEmail = binding.emailEditText.text.toString()
            val userPassword = binding.pwdEditText.text.toString()
            doLogin(userEmail, userPassword)
        }
        binding.signUPButton.setOnClickListener {

        }
    }

    private fun doLogin(userEmail: String, password: String) {
        Firebase.auth.signInWithEmailAndPassword(userEmail, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    startActivity(
                        Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w("LoginActivity", "signInWithEmail", it.exception)
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}