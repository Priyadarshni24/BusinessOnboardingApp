package com.example.businessapp.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentForgotPasswordBinding
import com.example.businessapp.databinding.FragmentResetPasswordBinding
import com.example.businessapp.model.ResetPasswordRequest
import com.example.businessapp.network.RetrofitClient
import kotlinx.coroutines.launch

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentResetPasswordBinding.bind(view)
       // val token = arguments?.getString("token") ?: ""
        binding.btnSubmit.setOnClickListener {
                val token = "895642";
                val password = binding.editnewPassword.text.toString()
                val confirm = binding.editconfirmPassword.text.toString()

                if (token.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    Toast.makeText(requireContext(), "All fields required", Toast.LENGTH_SHORT).show()
                } else if (password != confirm) {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                } else {
                    resetPassword(token, password, confirm)
                }

        }
        binding.txtotp.setOnClickListener {


            findNavController().navigate(
                R.id.action_resetPasswordFragment_to_OTPFragment
            )

        }

    }
    private fun resetPassword(token: String, password: String, cpassword: String) {

        lifecycleScope.launch {

            try {
                val request = ResetPasswordRequest(token, password, cpassword)

                val response = RetrofitClient.api.resetPassword(request)

                if (response.isSuccessful) {

                    val data = response.body()

                    if (data?.success == true) {

                        Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()

                        // 👉 Navigate to Login
                        findNavController().navigate(R.id.loginFragment)

                    } else {
                        Toast.makeText(requireContext(), data?.message ?: "Failed", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    val error = response.errorBody()?.string()
                    Log.e("RESET_ERROR", error ?: "Error")

                    Toast.makeText(requireContext(), "Server Error", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}