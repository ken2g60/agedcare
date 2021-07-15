package com.knightshell.agedcare

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject


class Payment : AppCompatActivity() {

    lateinit var subscription: Spinner
    lateinit var packageInputField: Spinner
    lateinit var firstNameInputField: EditText
    lateinit var lastNameInputField: EditText
    lateinit var phoneNumberInputField: EditText
    lateinit var subscriptionButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        subscription = findViewById(R.id.subscription)
        packageInputField = findViewById(R.id.package_type)
        firstNameInputField = findViewById(R.id.firstNameInputField)
        lastNameInputField = findViewById(R.id.lastNameInputField)
        phoneNumberInputField = findViewById(R.id.phoneNumberInputField)
        subscriptionButton = findViewById(R.id.subscriptionButton)

        // dynamic form populating
        subscription.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent?.getItemAtPosition(position).toString()


                if(selectedItem == "Monthly"){
                    var monthPackage = listOf("Single pack - GH 15", "Double pack - GH 27", "Full pack - GH 41")
                    val spinner = findViewById(R.id.package_type) as Spinner
                    val arrayadapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, monthPackage)
                    spinner.adapter = arrayadapter
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectItem = parent?.getItemAtPosition(position).toString()
                            Toast.makeText(applicationContext, selectItem, Toast.LENGTH_LONG).show()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                    }
                }

                else if(selectedItem == "Three Month"){
                    var monthPackage = listOf("Single pack - GH 45", "Double pack - GH 81", "Full pack - GH 123")
                    val spinner = findViewById(R.id.package_type) as Spinner
                    val arrayadapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, monthPackage)
                    spinner.adapter = arrayadapter
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectItem = parent?.getItemAtPosition(position).toString()
                            Toast.makeText(applicationContext, selectItem, Toast.LENGTH_LONG).show()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                    }


                }
                else if(selectedItem == "Six Month"){
                    var monthPackage = listOf("Single pack - GH 90", "Double pack - GH 162", "Full pack - GH 246")
                    val spinner = findViewById(R.id.package_type) as Spinner
                    val arrayadapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, monthPackage)
                    spinner.adapter = arrayadapter
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectItem = parent?.getItemAtPosition(position).toString()
                            Toast.makeText(applicationContext, selectItem, Toast.LENGTH_LONG).show()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                    }

                }
                else if(selectedItem == "Annual"){
                    var monthPackage = listOf("Single pack - GH 180", "Double pack - GH 324", "Full pack - GH 492")
                    val spinner = findViewById(R.id.package_type) as Spinner
                    val arrayadapter = ArrayAdapter(applicationContext, android.R.layout.simple_spinner_dropdown_item, monthPackage)
                    spinner.adapter = arrayadapter
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                            val selectItem = parent?.getItemAtPosition(position).toString()
                            Toast.makeText(applicationContext, selectItem, Toast.LENGTH_LONG).show()
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {}

                    }

                }else if(selectedItem == "Select Subscription"){
                    val spinner = findViewById(R.id.package_type) as Spinner
                    spinner.adapter = null;
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        subscriptionButton.setOnClickListener {


            if(firstNameInputField.text.toString().trim().isEmpty()){
                firstNameInputField.setError("Lastname is Required")

            } else if(lastNameInputField.text.toString().trim().isEmpty()) {
                lastNameInputField.setError("Lastname is Required")

            } else if(phoneNumberInputField.text.toString().trim().isEmpty()){
                phoneNumberInputField.setError("Mobile Money Number is Required")
            } else if(subscription.selectedItem.toString().trim() == "Select Subscription"){
                Toast.makeText(this, "Please Select Subscription", Toast.LENGTH_SHORT).show()
            }  else{
                // upload the details
                uploadDetails()
            }
        }
    }

    private fun uploadDetails() {


        val phoneNumber = phoneNumberInputField.text.toString()
        val firstName = firstNameInputField.text.toString()
        val lastName = lastNameInputField.text.toString()
        val subscriptionType = subscription.selectedItem.toString()
        val packageType = packageInputField.selectedItem.toString()


        val stringRequest = object : StringRequest(
            Request.Method.POST, Endpoints.USER_AUTHENTICATION_REGISTER_ENDPOINT,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    Toast.makeText(applicationContext, volleyError.message, Toast.LENGTH_LONG).show()
                }
            }) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                if (response!!.statusCode == 201){
                    // do this
                    val snack = Snackbar.make(root_layout, "Subscription Done", Snackbar.LENGTH_LONG).show()

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)

                }else{
                    // do samething else
                    val snack = Snackbar.make(root_layout, "Something Wrong Happened", Snackbar.LENGTH_LONG).show()

                }

                return super.parseNetworkResponse(response)
            }
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("phoneNumber", phoneNumber)
                params.put("firstName", firstName)
                params.put("lastName", lastName)
                params.put("subscriptionType", subscriptionType)
                params.put("packageType", packageType)
                return params
            }
        }
        VolleyService.requestQueue.add(stringRequest)
        VolleyService.requestQueue.start()

    }
}