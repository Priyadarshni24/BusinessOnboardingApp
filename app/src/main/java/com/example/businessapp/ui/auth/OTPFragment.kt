package com.example.businessapp.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentForgotPasswordBinding
import com.example.businessapp.databinding.FragmentOTPBinding

class OTPFragment : Fragment(R.layout.fragment_o_t_p) {

    private var _binding: FragmentOTPBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentOTPBinding.bind(view)
        /*binding.txtLogin.setOnClickListener {

            findNavController().popBackStack()

        }*/

    }
}