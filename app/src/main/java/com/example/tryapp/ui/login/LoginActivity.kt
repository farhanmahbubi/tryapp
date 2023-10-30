package com.example.tryapp.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tryapp.DbContract
import com.example.tryapp.MainActivity
import com.example.tryapp.databinding.FragmentLoginBinding


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: FragmentLoginBinding
    private val dbContract = DbContract()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInput(email, password)) {
                loginUser(email, password)
            }
        }

        binding.tvRegister.setOnClickListener {
            switchToRegisterActivity()
        }
    }

    private fun loginUser(email: String, password: String) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, dbContract.urlLogin,
            Response.Listener { response ->
                Toast.makeText(applicationContext, response.toString(), Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            },
            Response.ErrorListener { error ->
                Toast.makeText(applicationContext, error.toString(), Toast.LENGTH_SHORT).show()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                params["password"] = password
                return params
            }
        }

        val requestQueue = Volley.newRequestQueue(applicationContext)
        requestQueue.add(stringRequest)
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Email diperlukan"
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Kata Sandi diperlukan"
            return false
        }

        return true
    }

    private fun switchToRegisterActivity() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}