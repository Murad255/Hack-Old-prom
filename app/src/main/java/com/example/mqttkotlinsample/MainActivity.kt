package com.example.mqttkotlinsample

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.annotation.RequiresApi

import androidx.fragment.app.Fragment
import com.example.mqttkotlinsample.fragments.*
import com.example.mqttkotlinsample.services.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        COM_mainActivity = this
        log("START THE FOREGROUND SERVICE ON DEMAND")
            // actionOnService(Actions.START)

//        supportFragmentManager.beginTransaction()
//            .replace(R.id.nav_fragment, ConfirmSelectionFragment())
//            .commitAllowingStateLoss()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, LoginFragment(::createNavigation))
            .commitAllowingStateLoss()

        /* Check if Internet connection is available */
        if (!isConnected()) {
            Log.d(this.javaClass.name, "Internet connection NOT available")

            Toast.makeText(applicationContext, "Internet connection NOT available", Toast.LENGTH_LONG).show()
        }

        findViewById<BottomNavigationView>(R.id.nav_view).visibility =  INVISIBLE
     //   findViewById<Toolbar>(R.id.toolbar).visibility =  INVISIBLE

        val bitmap = BitmapFactory.decodeResource(applicationContext.resources, R.drawable.mind)
        COM_notification= Notification(applicationContext,bitmap)
        COM_notification!!.createNotificationChannel()

    }

    fun createNavigation(){
        val navView = findViewById<BottomNavigationView>(R.id.nav_view)
        navView.visibility =  VISIBLE
        navView.setOnNavigationItemSelectedListener(navListener)
        supportFragmentManager.beginTransaction().replace(
            R.id.nav_fragment,
            ClientFragment()!!
        ).commit()



    }

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null
        when (item.itemId) {
            R.id.fragmentClient -> selectedFragment = ClientFragment()
            //R.id.fragmentTaskList -> selectedFragment = DeliveryTaskFragment()
            R.id.fragmentSettings -> selectedFragment = SettingsFragment()
            else -> selectedFragment = ClientFragment()

        }
        supportFragmentManager.beginTransaction().replace(
            R.id.nav_fragment,
            selectedFragment!!
        ).commit()
        true
    }

    private fun isConnected(): Boolean {
        var result = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
            if (capabilities != null) {
                result = when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                    else -> false
                }
            }
        } else {
            val activeNetwork = cm.activeNetworkInfo
            if (activeNetwork != null) {
                // connected to the internet
                result = when (activeNetwork.type) {
                    ConnectivityManager.TYPE_WIFI,
                    ConnectivityManager.TYPE_MOBILE,
                    ConnectivityManager.TYPE_VPN -> true
                    else -> false
                }
            }
        }
        return result
    }




    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, EndlessService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            startService(it)
        }
    }
}