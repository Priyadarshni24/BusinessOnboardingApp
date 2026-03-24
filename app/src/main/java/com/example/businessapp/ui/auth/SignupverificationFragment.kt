package com.example.businessapp.ui.auth

import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentSignupverificationBinding
import com.example.businessapp.viewmodel.SignupViewModel
import kotlin.getValue

class SignupverificationFragment : Fragment(R.layout.fragment_signupverification) {

    private var _binding: FragmentSignupverificationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignupViewModel by activityViewModels()

    private var selectedFileUri: Uri? = null

    // ✅ File Picker
    private val filePicker =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedFileUri = it

                val fileName = getFileName(it)
                binding.txtFileName.text = fileName
                binding.llFilePreview.visibility = View.VISIBLE
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentSignupverificationBinding.bind(view)

        // ✅ Open file picker
        binding.imgattach.setOnClickListener {
            filePicker.launch("*/*") // supports pdf, image
        }

        // ✅ Remove selected file
        binding.imgRemove.setOnClickListener {
            selectedFileUri = null
            binding.llFilePreview.visibility = View.GONE
        }

        // ✅ Continue button
        binding.btncontibue.setOnClickListener {

            // 🔴 Validation
            if (selectedFileUri == null) {
                Toast.makeText(requireContext(), "Please attach document", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Save in Shared ViewModel
            viewModel.documentUri = selectedFileUri

            // 👉 Navigate to next fragment
            findNavController().navigate(
                R.id.action_signupverificationFragment_to_signuphoursFragment
            )
        }
        binding.imgback.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // ✅ Get file name from URI
    private fun getFileName(uri: Uri): String {
        var name = "file"
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (it.moveToFirst()) {
                name = it.getString(index)
            }
        }
        return name
    }

    // ✅ Prevent memory leak
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



