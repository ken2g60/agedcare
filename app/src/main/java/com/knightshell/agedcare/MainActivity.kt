package com.knightshell.agedcare

import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.dynamicinvites.kotlin.util.DynamicLinksUtil
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.knightshell.agedcare.adapter.ViewPagerAdapter
import com.knightshell.agedcare.database.AgedCaredDatabaseHelper
import com.knightshell.agedcare.helper.ConnectivityReceiver
import com.knightshell.agedcare.service.TaskSchduler

class MainActivity : BaseActivity() {

    private lateinit var viewpageradapter: ViewPagerAdapter
    lateinit var viewPager: ViewPager
    lateinit var tabLayout: TabLayout
    lateinit var broadcastReceiver: BroadcastReceiver
    lateinit var connectivityReceiver: ConnectivityReceiver
    lateinit var intentFilter: IntentFilter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tab_layout)


        viewpageradapter = ViewPagerAdapter(supportFragmentManager)
        viewPager.adapter = viewpageradapter
        tabLayout.setupWithViewPager(viewPager)

        // recievier
        intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        connectivityReceiver = ConnectivityReceiver()


        // start the service
        val intent = Intent(this, TaskSchduler::class.java)
        startService(intent)

        val databaseHandler: AgedCaredDatabaseHelper= AgedCaredDatabaseHelper(applicationContext)
        databaseHandler.weeklyPressureQuery()
        databaseHandler.monthlyPressureQuery()
    }

    override fun onStart() {
        super.onStart()

            //registerReceiver(broadcastReceiver, intentFilter)
    }
    override fun onStop() {
        super.onStop()
            // unregisterReceiver(broadcastReceiver)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){
            R.id.action_agedcare -> {

                val link = DynamicLinksUtil.generateContentLink()

                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, link.toString())

                startActivity(Intent.createChooser(intent, "Share Link"))
                return true
            }
            R.id.action_subscription -> {
                val intent = Intent(this, Payment::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }




}
