package com.knightshell.agedcare.fragment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
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
import com.knightshell.agedcare.model.WeightModel
import com.knightshell.agedcare.sharedpref.SharedPreference
import io.sentry.core.Sentry
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class WeightFragment : Fragment() {


    var weightFValue: Int = 0
    var weightSValue: Int = 0
    var dateValue: String = ""
    var timeValue: String = ""
    lateinit var weightFirstValue: NumberPicker
    lateinit var weightSecondValue: NumberPicker
    lateinit var date: EditText
    lateinit var time: EditText
    lateinit var submit: Button
    lateinit var sharedPreference: SharedPreference

    var dbHandler: AgedCaredDatabaseHelper? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view: View = inflater.inflate(R.layout.fragment_weight, container, false)
        weightFirstValue = view.findViewById<NumberPicker>(R.id.weightFirstValue)
        weightSecondValue = view.findViewById<NumberPicker>(R.id.weightSecondValue)
        date = view.findViewById(R.id.date)
        time = view.findViewById(R.id.time)
        submit = view.findViewById(R.id.submitButton)

        sharedPreference = SharedPreference(context!!.applicationContext)


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
            context?.let { DatePickerDialog(it, timeSetListener, cal.get(Calendar.MONTH), cal.get(
                Calendar.YEAR),cal.get(Calendar.DAY_OF_MONTH)).show() }
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

        weightFirstValue.minValue = 5
        weightFirstValue.maxValue = 400


        weightSecondValue.minValue = 0;0
        weightSecondValue.maxValue = 99


        if (weightFirstValue != null){
            weightFirstValue.wrapSelectorWheel = false

            weightFirstValue.setOnValueChangedListener { picker, oldValue, newValue ->
                weightFValue = newValue
            }
        }

        if (weightSecondValue != null){
            weightSecondValue.wrapSelectorWheel = false

            weightSecondValue.setOnValueChangedListener { picker, oldValue, newValue ->
                weightSValue = newValue
            }
        }

        submit.setOnClickListener { view ->

            if(date.text.trim().toString().isEmpty()){
                date.setError("Please set the date")
            } else if(time.text.trim().toString().isEmpty()){
                time.setError("Please set the time")
            }else {
                uploadWeightData()
            }

        }


        return view

    }

    private fun uploadWeightData(){



        val stringRequest = object : StringRequest(
            Request.Method.POST, Endpoints.USER_WEIGHT_ENDPOINT,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)

                } catch (e: JSONException) {
                    Sentry.captureMessage(e.printStackTrace().toString())
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Sentry.captureMessage(volleyError.printStackTrace().toString())
                    Toast.makeText(context, volleyError.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                if(response!!.statusCode == 201){

                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                    activity!!.runOnUiThread {
                        Toast.makeText(context, "Weight Submitted Successfully", Toast.LENGTH_SHORT).show()
                    }
                }
                else if(response.statusCode == 400){
                    Toast.makeText(context, response.statusCode, Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(context, R.string.connection, Toast.LENGTH_SHORT).show()
                }
                return super.parseNetworkResponse(response)
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()


                val databaseHandler: AgedCaredDatabaseHelper= AgedCaredDatabaseHelper(requireContext())
                val totalWeight = weightFValue.toString() + "." + weightSValue.toString()
                val status = databaseHandler.addWeight(WeightModel(dateValue, timeValue, totalWeight.toFloat()))



                val savedPhonenumber = sharedPreference.sharedPref.getString("phonenumber", "")

                params.put("username", savedPhonenumber.toString())
                params.put("time", timeValue)
                params.put("date", dateValue)
                params.put("weight", totalWeight)
                return params
            }
        }
        VolleyService.requestQueue.add(stringRequest)
        VolleyService.requestQueue.start()

    }
}


