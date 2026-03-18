package com.example.businessapp.ui.auth


import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentLoginBinding
import com.example.businessapp.databinding.FragmentOnboardingBinding
import com.example.businessapp.model.LoginRequest
import com.example.businessapp.network.RetrofitClient
import com.google.android.gms.common.api.Response

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class LoginFragment : Fragment(R.layout.fragment_login) {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentLoginBinding.bind(view)
        binding.txtForgot.setOnClickListener {

            findNavController().navigate(
                R.id.action_loginFragment_to_forgotPasswordFragment
            )

        }
        binding.txtCreateaccount.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_signupFragment
            )
        }
        binding.btnLogin.setOnClickListener {

            val email = binding.editEmail.text.toString()
            val password = binding.editPassword.text.toString()
            Log.d("LOGIN_FRAGMENT", "Email: $email, Password: $password")
            loginUser(email, password)
        }

    }
    private fun loginUser(email: String, password: String) {

        lifecycleScope.launch {

            try {

                val request = LoginRequest(
                    email = email,
                    password = password,
                    role = "farmer",
                    device_token = "test_token",
                    type = "email",
                    social_id = ""
                )

                val response = RetrofitClient.api.login(request)

                if (response.isSuccessful) {

                    val data = response.body()
                    Log.d("LOGIN_SUCCESS", data.toString())

                    Toast.makeText(requireContext(), "Login Success", Toast.LENGTH_SHORT).show()

              //      findNavController().navigate(R.id.homeFragment)

                } else {

                    val error = response.errorBody()?.string()
                    Log.e("LOGIN_ERROR", error.toString())

                    Toast.makeText(requireContext(), "Login Failed", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("EXCEPTION", e.message.toString())
            }
        }
    }

}