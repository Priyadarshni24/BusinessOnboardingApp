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
import com.example.businessapp.databinding.FragmentLoginBinding
import com.example.businessapp.model.ForgotPasswordRequest
import com.example.businessapp.model.LoginRequest
import com.example.businessapp.network.RetrofitClient
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ForgotPasswordFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentForgotPasswordBinding.bind(view)
        binding.txtLogin.setOnClickListener {

            findNavController().popBackStack()

        }
        binding.btnSubmit.setOnClickListener{
            val mobile = binding.etphonenumber.text.toString()

            if (mobile.isEmpty()) {
                binding.etphonenumber.error = "Enter valid mobile"
            } else {
                forgetPassword("+1$mobile") // if required
            }
        }
        binding.txtResetpassword.setOnClickListener {

                findNavController().navigate(
                    R.id.action_forgotPasswordFragment_to_OTPFragment
                )

        }

    }
    private fun forgetPassword(mobile: String) {

        lifecycleScope.launch {

            try {
                // ✅ FIX: Create request
                val request = ForgotPasswordRequest(mobile)

                val response = RetrofitClient.api.forgotPassword(request)

                if (response.isSuccessful) {

                    val data = response.body()

                    if (data?.success == true) {

                        Toast.makeText(requireContext(), data.message, Toast.LENGTH_SHORT).show()

                        // ✅ Navigate to OTP Fragment
                        val bundle = Bundle()
                        bundle.putString("mobile", mobile)
                       // bundle.putString("token", "otp")
                        findNavController().navigate(
                            R.id.action_forgotPasswordFragment_to_OTPFragment,
                            bundle
                        )

                    } else {
                        Toast.makeText(requireContext(), data?.message ?: "Failed", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    val error = response.errorBody()?.string()
                    Log.e("API_ERROR", error ?: "Unknown error")

                    Toast.makeText(requireContext(), "Server Error", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("EXCEPTION", e.message ?: "Exception")

                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
}