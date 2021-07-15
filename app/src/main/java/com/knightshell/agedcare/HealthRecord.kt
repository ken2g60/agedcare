package com.knightshell.agedcare

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.knightshell.agedcare.database.AgedCaredDatabaseHelper

class HealthRecord : AppCompatActivity() {

    lateinit var bloodSugarEditField: EditText
    lateinit var bloodPressureEditText: EditText
    lateinit var bloodCholestrolEditText: EditText
    lateinit var bloodLevelEditText: EditText
    lateinit var weightEditText: EditText
    lateinit var submitButton: Button

    val dbHelper = AgedCaredDatabaseHelper(this@HealthRecord)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_health_record)

        bloodSugarEditField = findViewById(R.id.bloodSugar)
        bloodPressureEditText = findViewById(R.id.bloodPressure)
        bloodCholestrolEditText = findViewById(R.id.bloodCholestrol)
        bloodLevelEditText = findViewById(R.id.bloodLevel)
        weightEditText = findViewById(R.id.weight)
        submitButton = findViewById(R.id.submit)

        submitButton.setOnClickListener { view ->
            recordHealthData()
        }


    }


    private fun recordHealthData() {
        TODO("Validate the inputs fields before submitting")
        var bloodSugar = bloodSugarEditField.toString()
        var bloodPressure = bloodPressureEditText.toString()
        var bloodCholestrol = bloodCholestrolEditText.toString()
        var bloodLevel = bloodLevelEditText.toString()
        var weight = weightEditText.toString()

        TODO("Sqlite Database Store Data")
        val db = dbHelper.writableDatabase

        Toast.makeText(this, getString(R.string.healthdataMessage), Toast.LENGTH_LONG).show()

    }
}