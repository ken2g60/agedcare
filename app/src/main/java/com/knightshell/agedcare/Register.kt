package com.knightshell.agedcare

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.*
import com.android.volley.toolbox.StringRequest
import com.google.android.material.snackbar.Snackbar
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.knightshell.agedcare.sharedpref.SharedPreference
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject

class Register : BaseActivity() {

    lateinit var emailEditText: EditText
    lateinit var phoneNumberEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var submitButton: Button
    lateinit var loginText: TextView


    private lateinit var phoneNumberUtil: PhoneNumberUtil

    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        sharedPreference = SharedPreference(applicationContext)

        emailEditText = findViewById(R.id.emailInputField)
        phoneNumberEditText = findViewById(R.id.phoneNumberInputField)
        passwordEditText = findViewById(R.id.passwordInputField)
        loginText = findViewById(R.id.link_signin)
        phoneNumberUtil = PhoneNumberUtil.getInstance()


        loginText.setOnClickListener { view ->
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }
         submitButton = findViewById(R.id.registerButton)

        // phonenumber validation
        phoneNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                val number = editable.toString()
                val countryIsoCode = getCountryIsoCode(number)
                phoneNumberEditText.setError(countryIsoCode ?: "Can't detect a country")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })


        submitButton.setOnClickListener { view ->

            if(emailEditText.text.toString().trim().isEmpty()){
                Toast.makeText(applicationContext, "UserName is required", Toast.LENGTH_SHORT).show()

            } else if(phoneNumberEditText.text.toString().trim().isEmpty()) {
                phoneNumberEditText.setError("PhonNumber is Required")

            } else if(passwordEditText.text.toString().trim().isEmpty()){
                passwordEditText.setError("Password is Required")
            }else{
                signUpUser()
            }

        }
    }

    private fun getCountryIsoCode(number: String): String? {
        val validatedNumber = if (number.startsWith("+")) number else "+$number"
        val phoneNumber = try {
            phoneNumberUtil.parse(validatedNumber, null)
        } catch (e: NumberParseException) {
            Log.e("geocode_phonenumber", "error during parsing a number")
            null
        }
        if(phoneNumber == null) return null
        return phoneNumberUtil.getRegionCodeForCountryCode(phoneNumber.countryCode)
    }
    private fun signUpUser() {

        val email = emailEditText.text.toString()
        val phonenumber = phoneNumberEditText.text.toString()
        val password = passwordEditText.text.toString()



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
                    Toast.makeText(applicationContext, "Something Happened", Toast.LENGTH_LONG).show()
                }
            }) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                if (response!!.statusCode == 201){
                    // do this
                    val snack = Snackbar.make(root_layout, R.string.authentication_register, Snackbar.LENGTH_LONG).show()


                    sharedPreference.setLoggedIn("phonenumber", phonenumber)
                    val intent = Intent(applicationContext, Login::class.java)
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
                params.put("email", email)
                params.put("phonenumber", phonenumber)
                params.put("password", password)
                return params
            }
        }
        VolleyService.requestQueue.add(stringRequest)
        VolleyService.requestQueue.start()


    }
}
