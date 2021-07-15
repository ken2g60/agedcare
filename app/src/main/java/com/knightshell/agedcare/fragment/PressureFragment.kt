package com.knightshell.agedcare.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.NumberPicker.OnValueChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.knightshell.agedcare.Endpoints
import com.knightshell.agedcare.MainActivity
import com.knightshell.agedcare.R
import com.knightshell.agedcare.VolleyService
import com.knightshell.agedcare.database.AgedCaredDatabaseHelper
import com.knightshell.agedcare.model.PressureModel
import com.knightshell.agedcare.sharedpref.SharedPreference
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class PressureFragment : Fragment() {

    var diastolicValue = 0
    var systolicValue = 0
    var dateValue: String = ""
    var timeValue: String = ""
    internal lateinit var dateEditTextView: EditText
    internal lateinit var timeEditTextView: EditText
    internal lateinit var submitButton: Button
    lateinit var sharedPreference: SharedPreference



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_pressure, container, false)


        dateEditTextView = view.findViewById(R.id.date)
        timeEditTextView = view.findViewById(R.id.time)
        submitButton = view.findViewById(R.id.submitButton)

        sharedPreference = SharedPreference(context!!.applicationContext)




        dateEditTextView.setOnClickListener { view ->
            val cal = Calendar.getInstance()
            val timeSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.get(Calendar.DAY_OF_YEAR)
                cal.get(Calendar.MONTH)
                cal.get(Calendar.DAY_OF_MONTH)
                val myFormat = "MM/dd/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                dateEditTextView.setText("" + dayOfMonth + "/" + (month + 1) + "/" + year);
                dateValue = sdf.format(cal.time)
            }
            context?.let { DatePickerDialog(it, timeSetListener, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR),cal.get(Calendar.DAY_OF_MONTH)).show() }
        }


        timeEditTextView.setOnClickListener { view ->
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                timePicker.is24HourView
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                timeEditTextView.setText(SimpleDateFormat("hh:mm a").format(cal.time))
                timeValue = SimpleDateFormat("hh:mm a").format(cal.time)
            }
            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }



        val systolic = view.findViewById<NumberPicker>(R.id.systolic)
        systolic.maxValue = 350
        systolic.minValue = 20
        systolic.wrapSelectorWheel = false

        systolic.setOnValueChangedListener { _, _, newValue ->
            systolicValue = newValue
        }



        val diastolic = view.findViewById<NumberPicker>(R.id.diastolic)
        diastolic.maxValue = 350
        diastolic.minValue = 20

        diastolic.wrapSelectorWheel = false


        diastolic.setOnValueChangedListener { _, _, newValue ->
            diastolicValue = newValue
        }



        submitButton.setOnClickListener { view ->

            if(dateEditTextView.text.trim().toString().isEmpty()){
                dateEditTextView.setError("Please set the date")
            } else if(timeEditTextView.text.trim().toString().isEmpty()){
                timeEditTextView.setError("Please set the time")
            }else {
                uploadPressureData()
            }
        }

        return view
    }


    private fun uploadPressureData(){

        val stringRequest = object : StringRequest(
            Request.Method.POST, Endpoints.USER_PRESSURE_ENDPOINT,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(context, volleyError.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                Log.d("response", response!!.statusCode.toString())
                if (response!!.statusCode == 201){
                    // do something
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity!!.runOnUiThread {
                        Toast.makeText(context, "Pressure Submitted Successfully", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(context, R.string.connection, Toast.LENGTH_SHORT).show()
                }
                return super.parseNetworkResponse(response)
            }
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()

                val databaseHandler: AgedCaredDatabaseHelper= AgedCaredDatabaseHelper(requireContext())
                val status = databaseHandler.addPressure(PressureModel(dateValue, timeValue, systolicValue, diastolicValue))


                // retrieve savedUsername
                val savedPhoneNumber = sharedPreference.sharedPref.getString("phonenumber", "")

                params.put("username", savedPhoneNumber.toString())
                params.put("systolic", systolicValue.toString())
                params.put("diastolic", diastolicValue.toString())
                params.put("time", timeValue)
                params.put("date", dateValue)

                return params
            }
        }
        VolleyService.requestQueue.add(stringRequest)
        VolleyService.requestQueue.start()

    }


    
}