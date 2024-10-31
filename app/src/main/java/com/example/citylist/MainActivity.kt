package com.example.citylist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.citylist.ui.theme.CityListTheme


import android.view.View
import android.widget.*
import com.example.citylist.R

class MainActivity : ComponentActivity() {

    private lateinit var addressHelper: AddressHelper
    private lateinit var spinnerProvince: Spinner
    private lateinit var spinnerDistrict: Spinner
    private lateinit var spinnerWard: Spinner
    private lateinit var calendarView: CalendarView
    private lateinit var btnToggleCalendar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize AddressHelper
        addressHelper = AddressHelper(resources)

        // Find views
        spinnerProvince = findViewById(R.id.spinnerProvince)
        spinnerDistrict = findViewById(R.id.spinnerDistrict)
        spinnerWard = findViewById(R.id.spinnerWard)
        calendarView = findViewById(R.id.calendarView)
        btnToggleCalendar = findViewById(R.id.btnToggleCalendar)

        // Setup calendar toggle
        btnToggleCalendar.setOnClickListener {
            if (calendarView.visibility == View.GONE) {
                calendarView.visibility = View.VISIBLE
            } else {
                calendarView.visibility = View.GONE
            }
        }

        // Load provinces
        val provinces = addressHelper.getProvinces()
        val provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        spinnerProvince.adapter = provinceAdapter

        // Setup listeners
        spinnerProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedProvince = provinces[position]
                loadDistricts(selectedProvince)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedProvince = spinnerProvince.selectedItem.toString()
                val selectedDistrict = parent.getItemAtPosition(position).toString()
                loadWards(selectedProvince, selectedDistrict)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        findViewById<Button>(R.id.btnSubmit).setOnClickListener { validateForm() }
    }

    private fun loadDistricts(province: String) {
        val districts = addressHelper.getDistricts(province)
        val districtAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, districts)
        spinnerDistrict.adapter = districtAdapter
    }

    private fun loadWards(province: String, district: String) {
        val wards = addressHelper.getWards(province, district)
        val wardAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, wards)
        spinnerWard.adapter = wardAdapter
    }

    private fun validateForm() {
        val etMSSV: EditText = findViewById(R.id.etMSSV)
        val etHoTen: EditText = findViewById(R.id.etHoTen)
        val etEmail: EditText = findViewById(R.id.etEmail)
        val etPhone: EditText = findViewById(R.id.etPhone)
        val radioGroupGender: RadioGroup = findViewById(R.id.radioGroupGender)
        val checkBoxTerms: CheckBox = findViewById(R.id.checkBoxTerms)

        val mssv = etMSSV.text.toString()
        val hoTen = etHoTen.text.toString()
        val email = etEmail.text.toString()
        val phone = etPhone.text.toString()
        val gender = when (radioGroupGender.checkedRadioButtonId) {
            R.id.radioMale -> "Nam"
            R.id.radioFemale -> "Nữ"
            else -> ""
        }
        val termsAccepted = checkBoxTerms.isChecked

        if (mssv.isBlank() || hoTen.isBlank() || email.isBlank() || phone.isBlank() || gender.isBlank() || !termsAccepted) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_SHORT).show()
        } else {
            // Thực hiện hành động khi form hợp lệ
            Toast.makeText(this, "Form hợp lệ! Thực hiện đăng ký...", Toast.LENGTH_SHORT).show()
        }
    }
}
