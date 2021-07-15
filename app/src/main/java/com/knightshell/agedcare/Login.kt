package com.knightshell.agedcare

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
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
import io.sentry.core.Sentry
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import org.json.JSONObject


class Login : BaseActivity() {


    internal lateinit var phoneNumberEditTextView: EditText
    internal lateinit var passwordEditTextView: EditText
    internal lateinit var loginButton: Button
    internal lateinit var link_signup: TextView

    private lateinit var phoneNumberUtil: PhoneNumberUtil
    lateinit var sharedPreference: SharedPreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        phoneNumberEditTextView = findViewById(R.id.phonenumberInputField)
        passwordEditTextView = findViewById(R.id.passwordInputField)
        loginButton = findViewById(R.id.loginButton)
        link_signup = findViewById(R.id.link_signup)
        phoneNumberUtil = PhoneNumberUtil.getInstance()



        sharedPreference = SharedPreference(applicationContext)
//        val checkLogin = sharedPreference.sharedPref.getBoolean("isLoggedIn", false)
//        if(!checkLogin){
//            val dashboard = Intent(this, MainActivity::class.java)
//            startActivity(dashboard)
//        }else{
//            val login = Intent(this, Login::class.java)
//            startActivity(login)
//        }



        link_signup.setOnClickListener { view ->
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        // phonenumber validation
        phoneNumberEditTextView.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(editable: Editable?) {
                val number = editable.toString()
                val countryIsoCode = getCountryIsoCode(number)
                // countryTextView.text = countryIsoCode ?: "Can't detect a country"
                phoneNumberEditTextView.setError(countryIsoCode ?: "Can't detect a country")
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })


        loginButton.setOnClickListener { view ->

            // validation
            if(phoneNumberEditTextView.text.toString().trim().isEmpty()){
                phoneNumberEditTextView.setError("PhoneNumber is Required")

            } else if(passwordEditTextView.text.toString().trim().isEmpty()){
                passwordEditTextView.setError("Password is required")
            }else{
                authenticateUser()
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

    private fun authenticateUser() {

        val phonenumber = phoneNumberEditTextView.text.toString()
        val password = passwordEditTextView.text.toString()

        val stringRequest = object : StringRequest(Request.Method.POST, Endpoints.USER_AUTHENTICATION_LOGIN_ENDPOINT,
            Response.Listener<String> { response ->
                try {
                    val obj = JSONObject(response)
                    sharedPreference.setToken("token", obj.getString("token"))
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            object : Response.ErrorListener {
                override fun onErrorResponse(volleyError: VolleyError) {
                    // Sentry.captureMessage(volleyError.toString())
                    Toast.makeText(applicationContext, volleyError.message.toString(), Toast.LENGTH_LONG).show()
                }
            }) {
            override fun parseNetworkResponse(response: NetworkResponse?): Response<String> {
                if (response!!.statusCode == 200){
                    // do something with the data
                    // store the data preference

                    // check if null
                    val myPrefs: SharedPreferences = getSharedPreferences(
                        "agedcarePrefs",
                        Context.MODE_PRIVATE
                    )
                    val loginPhonenumber = myPrefs.getString("phonenumber", "")
                    if(loginPhonenumber!!.isEmpty()){
                        sharedPreference.setLoggedIn("phonenumber", phonenumber)
                    }

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }else{
                    // do something else
                    val snack = Snackbar.make(root_layout, "Credentials are not Valid.", Snackbar.LENGTH_LONG).show()
                }
                return super.parseNetworkResponse(response)
            }
            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = phonenumber
                params["password"] = password
                return params
            }
        }
        VolleyService.requestQueue.add(stringRequest)
        VolleyService.requestQueue.start()


    }
}
