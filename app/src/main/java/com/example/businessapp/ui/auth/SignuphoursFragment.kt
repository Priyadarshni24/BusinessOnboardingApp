package com.example.businessapp.ui.auth

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentTimeslotsignupBinding
import com.example.businessapp.network.RetrofitClient
import com.example.businessapp.viewmodel.SignupViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import kotlin.getValue
import com.google.android.material.chip.Chip
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody

class SignuphoursFragment : Fragment(R.layout.fragment_timeslotsignup) {

    private var _binding: FragmentTimeslotsignupBinding? = null
    private val binding get() = _binding!!
    private fun String.toRequestBodyPart(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())


    private val viewModel: SignupViewModel by activityViewModels()

    private var selectedTimes: List<String> = emptyList()
    private var selectedDay: String = "mon"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTimeslotsignupBinding.bind(view)

        // ✅ Example: get selected times from chips
        binding.signup.setOnClickListener {

            selectedTimes = getSelectedTimes()

            if (selectedTimes.isEmpty()) {
                Toast.makeText(requireContext(), "Select at least one time slot", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Save to ViewModel
            viewModel.selectedDays = selectedDay
            viewModel.selectedHours = selectedTimes

            // ✅ Call API
            registerUser()
        }
        binding.imgback.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // ✅ FINAL API CALL
    private fun registerUser() {

        if (viewModel.documentUri == null) {
            Toast.makeText(requireContext(), "Document missing", Toast.LENGTH_SHORT).show()
            return
        }

        val filePart = getFilePart(viewModel.documentUri!!)

        val businessHoursJson = JSONObject().apply {
            put(viewModel.selectedDays, viewModel.selectedHours.joinToString())
        }.toString()

        lifecycleScope.launch {
            Log.e("TAG", "businessHoursJson: $businessHoursJson")
            try {
                val response = RetrofitClient.api.register(

                    viewModel.full_name.toRequestBodyPart(),
                    viewModel.email.toRequestBodyPart(),
                    viewModel.phone.toRequestBodyPart(),
                    viewModel.password.toRequestBodyPart(),
                    "farmer".toRequestBodyPart(),

                    viewModel.business_name.toRequestBodyPart(),
                    viewModel.informal_name.toRequestBodyPart(),
                    viewModel.address.toRequestBodyPart(),
                    viewModel.city.toRequestBodyPart(),
                    viewModel.state.toRequestBodyPart(),
                    viewModel.zip_code.toRequestBodyPart(),

                    filePart,
                    businessHoursJson.toRequestBodyPart(),

                    "dummy_token".toRequestBodyPart(),
                    "email".toRequestBodyPart(),
                    "".toRequestBodyPart()
                )
                Log.e("TAG", "registerUser: $response")
                if (response.isSuccessful && response.body()?.success == true) {

                    Toast.makeText(requireContext(), "Signup Success 🎉", Toast.LENGTH_SHORT).show()

                } else {
                    Log.e("TAG", "registerUser else: ${response.body()}")
                    Toast.makeText(requireContext(), response.body()?.message ?: "Signup Failed", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Toast.makeText(requireContext(), e.message ?: "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ✅ Helper: Convert String → RequestBody


    // ✅ Get selected chips
    private fun getSelectedTimes(): List<String> {
        val selected = mutableListOf<String>()

        val chipIds = binding.chipGroupTime.checkedChipIds

        chipIds.forEach {
            val chip = binding.root.findViewById<Chip>(it)
            selected.add(chip.text.toString())
        }

        return selected
    }

    // ✅ File conversion
    private fun getFilePart(uri: Uri): MultipartBody.Part {

        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "upload_file")

        file.outputStream().use { inputStream?.copyTo(it) }

        val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())

        return MultipartBody.Part.createFormData(
            "registration_proof",
            file.name,
            requestFile
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}