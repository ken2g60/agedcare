package com.knightshell.agedcare.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.NumberPicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.knightshell.agedcare.Endpoints
import com.knightshell.agedcare.MainActivity
import com.knightshell.agedcare.R
import com.knightshell.agedcare.VolleyService
import com.knightshell.agedcare.database.AgedCaredDatabaseHelper
import com.knightshell.agedcare.model.GlucoseModel
import com.knightshell.agedcare.sharedpref.SharedPreference
import io.sentry.core.Sentry
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class GlucoseFragment : Fragment() {


    var glucoseFirstValue: Int = 0
    var glucoseSecondValue: Int = 0
    var dateValue: String = ""
    var timeValue: String = ""
    lateinit var glucoseFirstNumberPickerValue: NumberPicker
    lateinit var glucoseSecondNumberPickerValue: NumberPicker
    lateinit var submit : Button
    lateinit var date: EditText
    lateinit var time: EditText
    lateinit var sharedPreference: SharedPreference



    var dbHandler: AgedCaredDatabaseHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.fragment_glucose, container, false)

        sharedPreference = SharedPreference(context!!.applicationContext)

        glucoseFirstNumberPickerValue = view.findViewById<NumberPicker>(R.id.glucoseFirstValue)
        glucoseFirstNumberPickerValue.minValue = 1
        glucoseFirstNumberPickerValue.maxValue = 25


        glucoseSecondNumberPickerValue = view.findViewById<NumberPicker>(R.id.glucoseSecondValue)
        glucoseSecondNumberPickerValue.minValue = 0
        glucoseSecondNumberPickerValue.maxValue = 9

        submit = view.findViewById<Button>(R.id.submitButton)
        date = view.findViewById(R.id.date)
        time = view.findViewById(R.id.time)





        date.setOnClickListener { view ->
            val cal = Calendar.getInstance()
            val timeSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                cal.get(Calendar.DAY_OF_YEAR)
                cal.get(Calendar.MONTH)
                cal.get(Calendar.DAY_OF_MONTH)
                val myFormat = "MM/dd/yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
                date.setText("" + dayOfMonth + "/" + (month + 1) + "/" + year);
                dateValue = sdf.format(cal.getTime())
            }
            context?.let { DatePickerDialog(it, timeSetListener, cal.get(Calendar.MONTH), cal.get(Calendar.YEAR),cal.get(Calendar.DAY_OF_MONTH)).show() }
        }

        time.setOnClickListener { view ->
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                time.setText(SimpleDateFormat("hh:mm a").format(cal.time))
                timeValue = SimpleDateFormat("hh:mm a").format(cal.time)
            }

            TimePickerDialog(context, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
        }



        if (glucoseFirstNumberPickerValue != null){

            glucoseFirstNumberPickerValue.wrapSelectorWheel = false

            glucoseFirstNumberPickerValue.setOnValueChangedListener { picker, oldValue, newValue ->
                glucoseFirstValue = newValue

            }
        }

        if(glucoseSecondNumberPickerValue != null){

            glucoseSecondNumberPickerValue.wrapSelectorWheel = false
            glucoseSecondNumberPickerValue.setOnValueChangedListener { picker, oldVal, newVal ->
                glucoseSecondValue = newVal
            }
        }

        submit.setOnClickListener { view ->

            if(date.text.trim().toString().isEmpty()){
                date.setError("Please set the date")
            } else if(time.text.trim().toString().isEmpty()){
                time.setError("Please set the time")
            }else {
                uploadGlucoseData()
            }

        }


        return view
    }

    private fun uploadGlucoseData(){



        val stringRequest = object : StringRequest(
            Request.Method.POST, Endpoints.USER_GLUCOSE_ENDPOINT,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    Sentry.captureMessage(e.printStackTrace().toString())
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Sentry.captureMessage(volleyError.toString())
                    Toast.makeText(context, volleyError.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                if (response!!.statusCode == 201){
                    // do something

                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity!!.runOnUiThread {
                        Toast.makeText(context, "Glucose Submitted Successfully", Toast.LENGTH_SHORT).show()
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
                val totalValue = glucoseFirstValue.toString() +  "."  + glucoseSecondValue.toString()

                Log.d("converted value", totalValue::class.java.simpleName)
                Log.d("value", totalValue)

                val convertedTotal: Float =   totalValue.toFloat()
                Log.d("converted value", convertedTotal::class.java.simpleName)

                val status = databaseHandler.addGlucose(GlucoseModel(dateValue, timeValue, totalValue.toFloat()))

                // retrieve savedUsername
                val savedPhoneNumber = sharedPreference.sharedPref.getString("phonenumber", "")

                params.put("username", savedPhoneNumber.toString())
                params.put("glucose", totalValue)
                params.put("time", timeValue)
                params.put("date", dateValue)
                return params
            }
        }
        VolleyService.requestQueue.add(stringRequest)
        VolleyService.requestQueue.start()

    }
}
