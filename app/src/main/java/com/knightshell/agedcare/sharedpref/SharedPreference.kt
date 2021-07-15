package com.knightshell.agedcare.sharedpref

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.knightshell.agedcare.Login


class SharedPreference(val context: Context){
    private val PREF_NAME = "agedcarePrefs"
    private  val IS_USER_LOGIN = "isLoggedIn"
    val sharedPref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)


    fun setLoggedIn(KEY_NAME: String, VALUE: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, VALUE)
        editor.putBoolean(IS_USER_LOGIN, true)
        editor.apply()
    }

    fun setToken(KEY_NAME: String, VALUE: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, VALUE)
        editor.apply()
    }


    fun checkUserLoggedIn(): Boolean {
        if(!this.isUserLoggedIn()){
            val intent = Intent(context, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            return true
        }
        return false
    }

    private fun isUserLoggedIn(): Boolean{
        return sharedPref.getBoolean(IS_USER_LOGIN, false)
    }

    fun logout(){
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.clear()
        editor.apply()
    }
}