package com.knightshell.agedcare

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.knightshell.agedcare.helper.ConnectivityReceiver

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var mSnackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun showMessage(isConnected: Boolean){

        if(isConnected){
            val message = "You are offline."
            mSnackBar = Snackbar.make(findViewById(R.id.root_layout), message, Snackbar.LENGTH_LONG)
            mSnackBar?.duration = Snackbar.LENGTH_INDEFINITE
            mSnackBar!!.setAction("DISMISS", View.OnClickListener {
                mSnackBar!!.dismiss()
            })
            mSnackBar?.show()
        }
    }

    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }

}