package com.example.businessapp.ui.auth


import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentLoginBinding
import com.example.businessapp.databinding.FragmentOnboardingBinding

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
    }
}