package com.example.businessapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentForgotPasswordBinding
import com.example.businessapp.databinding.FragmentResetPasswordBinding

class ResetPasswordFragment : Fragment(R.layout.fragment_reset_password) {

    private var _binding: FragmentResetPasswordBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentResetPasswordBinding.bind(view)
        binding.txtotp.setOnClickListener {


            findNavController().navigate(
                R.id.action_resetPasswordFragment_to_OTPFragment
            )

        }

    }

}