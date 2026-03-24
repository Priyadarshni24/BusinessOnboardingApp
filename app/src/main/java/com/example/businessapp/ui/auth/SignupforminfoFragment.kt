package com.example.businessapp.ui.auth

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentSignupBinding
import com.example.businessapp.databinding.FragmentSignupfarminfoBinding
import com.example.businessapp.viewmodel.SignupViewModel
import kotlin.getValue

class SignupforminfoFragment : Fragment(R.layout.fragment_signupfarminfo) {
    private var _binding: FragmentSignupfarminfoBinding? = null
    private val binding get() = _binding!!

    private var selectedFileUri: Uri? = null
    val states = listOf(
        "Andhra Pradesh",
        "Karnataka",
        "Tamil Nadu",
        "Kerala",
        "Maharashtra",
        "Delhi"
    )
    private val viewModel: SignupViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSignupfarminfoBinding.bind(view)
        viewModel.business_name = binding.editbusinessname.text.toString()
        viewModel.informal_name = binding.editinformalname.text.toString()
        viewModel.address = binding.editstreetaddress.text.toString()
        viewModel.city = binding.editCity.text.toString()
        viewModel.state = binding.etState.text.toString()
        viewModel.zip_code = binding.etZipcode.text.toString()



        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, states)

        binding.etState.setAdapter(adapter)
        binding.btncontibue.setOnClickListener {
            val state = binding.etState.text.toString()
            val zipcode = binding.etZipcode.text.toString()

            if (state.isEmpty()) {
                binding.etState.error = "Select state"
                return@setOnClickListener
            }

            if (zipcode.length < 6) {
                binding.etZipcode.error = "Enter valid zipcode"
                return@setOnClickListener
            }
                findNavController().navigate(R.id.action_signupforminfoFragment_to_signupverificationFragment)
        }
        binding.imgback.setOnClickListener {
            findNavController().popBackStack()
        }


    }

}