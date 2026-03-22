package com.example.businessapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel

class SignupViewModel : ViewModel() {

    // Step 1
    var full_name: String = ""
    var email: String = ""
    var phone: String = ""
    var password: String = ""
    var role: String = ""

    // Step 2
    var business_name: String = ""
    var informal_name: String = ""
    var address: String = ""
    var city: String = ""
    var state: String = ""
    var zip_code: String = ""

    // Step 3
    var documentUri: Uri? = null

    // Step 4
    var selectedDays: String = ""
    var selectedHours: List<String> = emptyList()

}