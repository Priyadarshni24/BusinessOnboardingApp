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
import com.example.businessapp.databinding.FragmentOTPBinding
import com.example.businessapp.model.VerifyOtpRequest
import com.example.businessapp.network.RetrofitClient
import kotlinx.coroutines.launch
import org.json.JSONObject

class OTPFragment : Fragment(R.layout.fragment_o_t_p) {

    private var _binding: FragmentOTPBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentOTPBinding.bind(view)
       val mobile = arguments?.getString("mobile") ?: ""
        binding.btnSubmit.setOnClickListener {
                val otp = binding.editOtp.text.toString()

                if (otp.length < 4) {
                    binding.editOtp.error = "Enter valid OTP"
                } else {
                    verifyOtp(otp)
                }

        }


    }
    private fun verifyOtp( otp: String) {

        lifecycleScope.launch {

            try {
                val request = VerifyOtpRequest(otp)

                val response = RetrofitClient.api.verifyOtp(request)
                // 👉 Pass OTP as token

                if (response.isSuccessful) {

                    val data = response.body()

                    if (data?.success == true) {

                        Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()

                        // 👉 Pass OTP as token
                        val bundle = Bundle()
                        bundle.putString("token", otp)

                        findNavController().navigate(
                            R.id.action_OTPFragment_to_resetPasswordFragment,
                            bundle
                        )

                    } else {
                        Toast.makeText(requireContext(), data?.message ?: "Invalid OTP", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    val errorBody = response.errorBody()?.string()

                    // 👉 Parse error message properly
                    try {
                        val json = JSONObject(errorBody ?: "")
                        val message = json.getString("message")
                        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Server Error", Toast.LENGTH_SHORT).show()
                    }
                    val bundle = Bundle()
                bundle.putString("token", otp)

                findNavController().navigate(
                    R.id.action_OTPFragment_to_resetPasswordFragment,
                    //bundle
                )}
            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}