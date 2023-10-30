package com.example.tryapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tryapp.DbContract
import com.example.tryapp.databinding.FragmentRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: FragmentRegisterBinding
    private val dbContract = DbContract()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val fullName = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            val nohp = binding.etNohp.text.toString()

            if (validateInput(fullName, email, password, nohp)) {
                registerUser(fullName, email, password, nohp)
            }
        }

        binding.tvLogin.setOnClickListener {
            switchToLoginActivity()
        }
    }

    private fun registerUser(fullName: String, email: String, password: String, nohp: String) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, dbContract.urlRegister,
            Response.Listener { response ->
                Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["fullname"] = fullName
                params["email"] = email
                params["password"] = password
                params["nohp"] = nohp
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(stringRequest)
    }

    private fun validateInput(fullName: String, email: String, password: String, nohp: String): Boolean {
        val emailPattern = Patterns.EMAIL_ADDRESS

        if (fullName.isEmpty()) {
            binding.etFullName.error = "Nama lengkap diperlukan"
            return false
        }

        if (email.isEmpty()) {
            binding.etEmail.error = "Email diperlukan"
            return false
        } else if (!emailPattern.matcher(email).matches()) {
            binding.etEmail.error = "Email tidak valid"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Kata Sandi diperlukan"
            return false
        } else if (password.length < 8) {
            binding.etPassword.error = "Kata Sandi minimal 8 karakter"
            return false
        }

        if (nohp.isEmpty()) {
            binding.etNohp.error = "Nomor HP diperlukan"
            return false
        }

        return true
    }

    private fun switchToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
}