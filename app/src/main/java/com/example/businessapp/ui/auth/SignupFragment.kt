package com.example.businessapp.ui.auth

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.businessapp.R
import com.example.businessapp.viewmodel.SignupViewModel
import com.example.businessapp.databinding.FragmentLoginBinding
import com.example.businessapp.databinding.FragmentSignupBinding

class SignupFragment : Fragment(R.layout.fragment_signup) {
    private var _binding: FragmentSignupBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSignupBinding.bind(view)
        viewModel.full_name = binding.editFullName.text.toString()
        viewModel.email = binding.editEmail.text.toString()
        viewModel.phone = binding.editphonenumber.text.toString()
        if(binding.editphonenumber.text.toString().equals(binding.reeditPassword.text.toString()))
            viewModel.password = binding.editPassword.text.toString()
        else
            binding.reeditPassword.error = "Password does not match"

        binding.btncontibue.setOnClickListener {

            findNavController().navigate(R.id.action_signupFragment_to_signupverificationFragment)
        }

    }

}