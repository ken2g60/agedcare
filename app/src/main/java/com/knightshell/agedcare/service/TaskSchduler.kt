package com.knightshell.agedcare.service

import android.app.NotificationManager
import android.app.Service
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.knightshell.agedcare.R
import com.knightshell.agedcare.database.AgedCaredDatabaseHelper
import java.util.*

class TaskSchduler: Service() {

    private var notificationManager: NotificationManager? = null
    val CHANNEL_ID = "com.nextech.agedcare"
    val notificationID = 101
    val textTitle = ""
    val textContent = ""

    override fun onCreate() {
        Log.i("SERVICE", "task query")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        super.onCreate()
    }
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
        return null
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        showNotification()
        queryData()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun queryData() {
        val calendar = Calendar.getInstance()
        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
            // run code here
            val databaseHandler: AgedCaredDatabaseHelper= AgedCaredDatabaseHelper(applicationContext)
            databaseHandler.weeklyPressureQuery()
        }
        if(calendar.get(Calendar.MONTH) == 1){
            // run code here
            val databaseHandler: AgedCaredDatabaseHelper= AgedCaredDatabaseHelper(applicationContext)
            databaseHandler.monthlyPressureQuery()
        }
    }

    private fun showNotification(){
        var builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications_active)
            .setContentTitle(textTitle)
            .setContentText(textContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()
        notificationManager?.notify(notificationID, builder)
    }

    override fun onDestroy() {
        super.onDestroy()
    }


}