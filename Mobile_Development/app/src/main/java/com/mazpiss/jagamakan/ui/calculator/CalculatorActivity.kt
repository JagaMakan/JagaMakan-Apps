package com.mazpiss.jagamakan.ui.calculator

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.mazpiss.jagamakan.R
import com.mazpiss.jagamakan.data.local.Calculator
import com.mazpiss.jagamakan.databinding.ActivityCalculatorBinding
import com.mazpiss.jagamakan.utils.UserPreferences
import kotlin.math.round

class CalculatorActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101
        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2
    }

    private lateinit var calculator: Calculator
    private lateinit var binding: ActivityCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCalculate.setOnClickListener(this)
        calculator = intent.getParcelableExtra<Calculator>("CALCULATE") as Calculator
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)

        var actionBarTitle = ""
        var btnTitle = ""
        when (formType) {
            TYPE_ADD -> {
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> {
                btnTitle = "Hitung Kalori"
                showPreferenceInForm()
            }
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.btnCalculate.text = btnTitle

        setupTextChangeListeners()
        enableCalculateButton()
    }

    private fun setupTextChangeListeners() {
        binding.genderGroup.setOnCheckedChangeListener { _, _ -> enableCalculateButton() }
        binding.weight.addTextChangedListener { enableCalculateButton() }
        binding.height.addTextChangedListener { enableCalculateButton() }
        binding.age.addTextChangedListener { enableCalculateButton() }
    }

    private fun enableCalculateButton() {
        val genderChecked = binding.genderGroup.checkedRadioButtonId != View.NO_ID
        val weightFilled = binding.weight.text.toString().isNotBlank()
        val heightFilled = binding.height.text.toString().isNotBlank()
        val ageFilled = binding.age.text.toString().isNotBlank()

        val calculateButtonEnabled = genderChecked && weightFilled && heightFilled && ageFilled

        binding.btnCalculate.isEnabled = calculateButtonEnabled
    }

    private fun showPreferenceInForm() {
        if (calculator.gender) {
            binding.maleRadio.isChecked = true
        } else {
            binding.femaleRadio.isChecked = true
        }
        binding.weight.setText(calculator.weight.toString())
        binding.height.setText(calculator.height.toString())
        binding.age.setText(calculator.age.toString())
        binding.KaloriLabel.text = calculator.calory

        binding.weight.text = null
        binding.age.text = null
        binding.height.text = null

        if (calculator.activity) {
            binding.lowRadio.isChecked = true
        } else {
            binding.lowRadio.isChecked = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View?) {
        if (view?.id == R.id.btnCalculate) {
            if (validateFields()) {
                saveUser()
                setResultAndFinish()
            }
        }
    }

    private fun validateFields(): Boolean {
        val weight = binding.weight.text.toString().trim()
        val height = binding.height.text.toString().trim()
        val age = binding.age.text.toString().trim()

        if (weight.isEmpty() || height.isEmpty() || age.isEmpty()) {
            showError("Field tidak boleh kosong")
            return false
        }

        return true
    }

    private fun showError(message: String) {
        Toast.makeText(this, "ERROR: $message", Toast.LENGTH_LONG).show()
    }

    private fun setResultAndFinish() {
        val resultIntent = Intent()
        resultIntent.putExtra(EXTRA_RESULT, calculator)
        setResult(RESULT_CODE, resultIntent)
        finish()
    }

    private fun calculateBMR(): Double {
        val weight = binding.weight.text.toString().toDoubleOrNull()
        val height = binding.height.text.toString().toDoubleOrNull()
        val age = binding.age.text.toString().toDoubleOrNull()

        if (weight == null || height == null || age == null) {
            Toast.makeText(this, "ERROR: Incorrect format", Toast.LENGTH_LONG).show()
            return 0.0
        }

        val genderMultiplier = if (binding.maleRadio.isChecked) {
            66.0 + (13.7 * weight) + (5.0 * height) - (6.8 * age)
        } else {
            665.0 + (9.6 * weight) + (1.8 * height) - (4.7 * age)
        }

        val activityMultiplier = when {
            binding.lowRadio.isChecked -> 1.4
            binding.mediumRadio.isChecked -> 1.78
            binding.highRadio.isChecked -> 2.1
            else -> 1.0
        }

        val bmr = genderMultiplier * activityMultiplier

        val roundedBMR = round(bmr).toInt()
        binding.KaloriLabel.text = "Kalori: $roundedBMR/Kal"

        return roundedBMR.toDouble()
    }

    private fun saveUser() {
        val userPreference = UserPreferences(this)

        calculator.gender = binding.genderGroup.checkedRadioButtonId == R.id.maleRadio
        calculator.weight = binding.weight.text.toString().toInt()
        calculator.height = binding.height.text.toString().toInt()
        calculator.age = binding.age.text.toString().toInt()
        calculator.activity = binding.activityGroup.checkedRadioButtonId == R.id.mediumRadio

        val roundedBMR = calculateBMR()

        calculator.calory = roundedBMR.toInt().toString()
        binding.KaloriLabel.text = "Kalori: ${roundedBMR.toInt()}"
        calculator.globalBMR = roundedBMR

        userPreference.setUser(calculator)
        userPreference.setBMR(roundedBMR)
        userPreference.setActivity(calculator.activity)
        userPreference.setGlobalBMR(roundedBMR)

        Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_SHORT).show()
    }
}
