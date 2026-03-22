package com.example.businessapp.ui.auth

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.businessapp.R
import com.example.businessapp.databinding.FragmentSignupverificationBinding
import com.example.businessapp.databinding.FragmentTimeslotsignupBinding
import com.example.businessapp.network.RetrofitClient
import com.example.businessapp.viewmodel.SignupViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import kotlin.getValue
import com.google.android.material.chip.Chip
import okhttp3.MediaType

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

            try {
                val response = RetrofitClient.api.register(

                    viewModel.full_name.toRequestBody(),
                    viewModel.email.toRequestBody(),
                    viewModel.phone.toRequestBody(),
                    viewModel.password.toRequestBody(),
                    "farmer".toRequestBody(),

                    viewModel.business_name.toRequestBody(),
                    viewModel.informal_name.toRequestBody(),
                    viewModel.address.toRequestBody(),
                    viewModel.city.toRequestBody(),
                    viewModel.state.toRequestBody(),
                    viewModel.zip_code.toRequestBody(),

                    filePart,
                    businessHoursJson.toRequestBody(),

                    "dummy_token".toRequestBody(),
                    "email".toRequestBody(),
                    "".toRequestBody()
                )

                if (response.isSuccessful && response.body()?.success == "true") {

                    Toast.makeText(requireContext(), "Signup Success 🎉", Toast.LENGTH_SHORT).show()

                } else {
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