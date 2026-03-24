package com.example.businessapp.ui.auth

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.children
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
import org.json.JSONArray

class SignuphoursFragment : Fragment(R.layout.fragment_timeslotsignup) {

    private var _binding: FragmentTimeslotsignupBinding? = null
    private val binding get() = _binding!!
    private fun String.toRequestBodyPart(): RequestBody =
        this.toRequestBody("text/plain".toMediaTypeOrNull())


    private val viewModel: SignupViewModel by activityViewModels()



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentTimeslotsignupBinding.bind(view)

        setupDaySelection()
        setupTimeSelection()

        binding.signup.setOnClickListener {

            if (viewModel.businessHoursMap.isEmpty()) {
                Toast.makeText(requireContext(), "Select business hours", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

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

        val businessHoursJson = JSONObject()

        viewModel.businessHoursMap.forEach { (day, slots) ->
            val array = JSONArray()
            slots.forEach { array.put(it) }
            businessHoursJson.put(day, array)
        }

        lifecycleScope.launch {
            Log.e("TAG", "businessHoursJson: $businessHoursJson")
            try {
                   Log.e("API_INPUT", """
                    full_name = ${viewModel.full_name}
                    email = ${viewModel.email}
                    phone = ${viewModel.phone}
                    password = ${viewModel.password}
                    role = farmer
                    
                    business_name = ${viewModel.business_name}
                    informal_name = ${viewModel.informal_name}
                    address = ${viewModel.address}
                    city = ${viewModel.city}
                    state = ${viewModel.state}
                    zip_code = ${viewModel.zip_code}
                    
                    device_token = dummy_token
                    type = email
                    social_id = 0imfnc8mVLWwsAawjYr4Rx-Af50DDqtlx
                    """.trimIndent())
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
                    businessHoursJson.toString().toRequestBodyPart(),

                    "dummy_token".toRequestBodyPart(),
                    "email/facebook/google/apple".toRequestBodyPart(),

                    "0imfnc8mVLWwsAawjYr4Rx-Af50DDqtlx".toRequestBodyPart()
                )

                Log.e("RAW_RESPONSE", response.errorBody()?.string() ?: "no error")
                val raw = response.body()?.string()
                Log.e("API_RAW", raw ?: "null")
                if (response.isSuccessful && response.isSuccessful.toString()[0].equals("true")) {

                    Toast.makeText(requireContext(), "Signup Success 🎉", Toast.LENGTH_SHORT).show()

                } else {
                    Log.e("TAG", "registerUser else: ${response.body().toString()}")
                  //  Toast.makeText(requireContext(), response.body() ?: "Signup Failed", Toast.LENGTH_SHORT).show()
                }

            } catch (e: Exception) {
                Log.e("TAG", "Error full: ", e)
                Toast.makeText(requireContext(), "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupDaySelection() {

        val dayMap = mapOf(
            binding.dayMon to "mon",
            binding.dayTue to "tue",
            binding.dayWed to "wed",
            binding.dayThu to "thu",
            binding.dayFri to "fri",
            binding.daySat to "sat",
            binding.daySun to "sun"
        )

        dayMap.forEach { (view, dayKey) ->

            view.setOnClickListener {

                viewModel.selectedDay = dayKey

                resetAllDays()
                view.setBackgroundResource(R.drawable.bg_day_selected)

                restoreSelectedSlots(dayKey)
            }
        }

        // default selection
        binding.dayMon.performClick()
    }
    private fun resetAllDays() {
        listOf(
            binding.dayMon, binding.dayTue, binding.dayWed,
            binding.dayThu, binding.dayFri, binding.daySat, binding.daySun
        ).forEach {
            it.setBackgroundResource(R.drawable.bg_day_unselected)
        }
    }
    private fun setupTimeSelection() {

        binding.chipGroupTime.setOnCheckedStateChangeListener { group, checkedIds ->

            val day = viewModel.selectedDay

            val selectedSlots = mutableListOf<String>()

            checkedIds.forEach { id ->
                val chip = group.findViewById<Chip>(id)
                selectedSlots.add(chip.text.toString())
            }

            viewModel.businessHoursMap[day as String] = selectedSlots
        }
    }

    private fun restoreSelectedSlots(day: String) {

        val selectedSlots = viewModel.businessHoursMap[day] ?: emptyList()

        binding.chipGroupTime.children.forEach { view ->
            val chip = view as Chip
            chip.isChecked = selectedSlots.contains(chip.text.toString())
        }
    }
    // ✅ Helper: Convert String → RequestBody
    /*If each day has different time slots, you should store like:

    Map<String, List<String>>t

    Then:

    val businessHoursJson = JSONObject()

    viewModel.businessHoursMap.forEach { (day, hoursList) ->
        val array = JSONArray()
        hoursList.forEach { array.put(it) }
        businessHoursJson.put(day, array)
    }*/

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

    private fun getFilePart(uri: Uri): MultipartBody.Part {

        val contentResolver = requireContext().contentResolver

        val fileName = getFileName(uri)?.let {
            if (it.endsWith(".pdf")) it else "$it.pdf"
        } ?: "document.pdf"

        val file = File(requireContext().cacheDir, fileName)

        contentResolver.openInputStream(uri)?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        val requestFile = file.asRequestBody("application/pdf".toMediaTypeOrNull())

        Log.e("FILE_DEBUG", "name=${file.name}, size=${file.length()}")

        return MultipartBody.Part.createFormData(
            "registration_proof",
            file.name,
            requestFile
        )
    }
    private fun getFileName(uri: Uri): String? {
        val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
        return cursor?.use {
            val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            it.moveToFirst()
            it.getString(nameIndex)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}