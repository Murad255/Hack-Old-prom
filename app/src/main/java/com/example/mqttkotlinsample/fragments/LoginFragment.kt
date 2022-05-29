package com.example.mqttkotlinsample.fragments

import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

import androidx.fragment.app.Fragment
import com.example.mqttkotlinsample.*
import com.example.mqttkotlinsample.mqtt.IN_WATCHER
import com.example.mqttkotlinsample.mqtt.LOG_TAG
import com.example.mqttkotlinsample.mqtt.OUT_WATCHER
import com.example.mqttkotlinsample.mqtt.QOS
import com.example.mqttkotlinsample.services.Actions
import com.example.mqttkotlinsample.services.Notification
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import kotlin.concurrent.thread

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class LoginFragment (private val crtNav: () -> Unit) : Fragment() {

   // private var _binding: FragmentLoginBinding? = null

    private var userName: EditText?= null
    private var userPass: EditText?= null
    private var errorMesssage: TextView?= null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        COM_MqttBegin(context)
        //COM_actionOnService(Actions.START)

        userName = view.findViewById<EditText>(R.id.edittext_username)
        userPass = view.findViewById<EditText>(R.id.edittext_password)
        errorMesssage = view.findViewById<TextView>(R.id.tv_report)

        val intent = Intent(this.requireContext(), MainActivity::class.java)
        intent.apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(this.requireContext(), 0, intent, 0)


        view.findViewById<Button>(R.id.button_connect).setOnClickListener{

            var name: String = view.findViewById<EditText>(R.id.edittext_username).text.toString()
            var pass: String = view.findViewById<EditText>(R.id.edittext_password).text.toString()
            name =  name.trim()
            pass = pass.trim()
            COM_userlogin =   name

                CoroutineScope(Dispatchers.IO).launch  {
                    COM_MqttClient!!.subscribe(IN_WATCHER, QOS,
                        object : IMqttActionListener {
                            override fun onSuccess(asyncActionToken: IMqttToken?) {
                                Log.d(LOG_TAG, "Subscribed to topic")
                                COM_MqttClient!!.isSubscribe = true
                                var loginStr =
                                    "<Module><UserLogin>" + name + "</UserLogin><UserPwd>" + pass + "</UserPwd></Module>"
                                COM_MqttClient!!.publish(OUT_WATCHER, loginStr)

                            }

                            override fun onFailure(
                                asyncActionToken: IMqttToken?,
                                exception: Throwable?
                            ) {
                                Log.d(LOG_TAG, "Failed to subscribe: $IN_WATCHER")
                            }
                        })
                }



            CoroutineScope(Dispatchers.Main).launch {

                if (COM_MqttClient!!.waitingLoginStatusСonfirmed()) {
                    COM_userlogin = name
                    replaceFragment()
                } else {
                    notAutorization()
                }

            }

        }

        view.findViewById<ImageButton>(R.id.imageButton).setOnClickListener{

            COM_userlogin =   "custom"
            CoroutineScope(Dispatchers.IO).launch  {
                COM_MqttClient!!.subscribe(IN_WATCHER, QOS,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            Log.d(LOG_TAG, "Subscribed to topic")
                            COM_MqttClient!!.isSubscribe = true
                        }

                        override fun onFailure(
                            asyncActionToken: IMqttToken?,
                            exception: Throwable?
                        ) {
                            Log.d(LOG_TAG, "Failed to subscribe: $IN_WATCHER")
                        }
                    })
            }

          //  COM_notification!!.sentNotification("Умная больница","Программа запущена")
            replaceFragment()
        }



    }


    fun notAutorization(){
        errorMesssage!!.text = "неверные данные"
    }

    fun replaceFragment(){
        crtNav()
        onDestroy()
        requireFragmentManager().beginTransaction().remove(this).commit()
        // findNavController().navigate(R.id.action_ConnectFragment_to_ClientFragment, mqttCredentialsBundle)

    }
//    override fun onResume() {
//        super.onResume()
//        if (!COM_MqttClient!!.isConnected()) COM_MqttClient!!.connect()
//
//        if (!COM_MqttClient!!.isSubscribe) {
//            COM_MqttClient!!.subscribe(IN_WATCHER, QOS)
//        } else {
//            Log.d("MQTTClient", "Failed to subscribe, no server connected")
//        }
//    }
//


}


/*
* package com.example.mqttkotlinsample.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.mqttkotlinsample.COM_Autorization
import com.example.mqttkotlinsample.COM_MqttBegin
import com.example.mqttkotlinsample.R
import com.example.mqttkotlinsample.databinding.FragmentLoginBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class LoginFragment(private val crtNav: () -> Unit) : Fragment() {

    private val hideHandler = Handler()

    @Suppress("InlinedApi")
    private val hidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        val flags =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        activity?.window?.decorView?.systemUiVisibility = flags
        (activity as? AppCompatActivity)?.supportActionBar?.hide()
    }
    private val showPart2Runnable = Runnable {
        // Delayed display of UI elements
        fullscreenContentControls?.visibility = View.VISIBLE
    }
    private var visible: Boolean = false
    private val hideRunnable = Runnable { hide() }

    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val delayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    private var dummyButton: Button? = null
    private var fullscreenContent: View? = null
    private var fullscreenContentControls: View? = null

    private var _binding: FragmentLoginBinding? = null

    private var userName: EditText?= null
    private var userPass: EditText?= null
    private var errorMesssage: TextView?= null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
       COM_MqttBegin(context)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        visible = true

        //dummyButton = binding.dummyButton
        fullscreenContent = binding.fullscreenContent
       // fullscreenContentControls = binding.fullscreenContentControls
        // Set up the user interaction to manually show or hide the system UI.
        fullscreenContent?.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        dummyButton?.setOnTouchListener(delayHideTouchListener)

        userName = view.findViewById<EditText>(R.id.edittext_username)
        userPass = view.findViewById<EditText>(R.id.edittext_password)
        errorMesssage = view.findViewById<TextView>(R.id.tv_report)

        view.findViewById<Button>(R.id.button_connect).setOnClickListener{

            var name: String = view.findViewById<EditText>(R.id.edittext_username).text.toString()
            var pass: String = view.findViewById<EditText>(R.id.edittext_password).text.toString()

            if(COM_Autorization(name,pass)){
                crtNav()
                onDestroy()
                requireFragmentManager().beginTransaction().remove(this).commit()

            }
            else{
                view.findViewById<TextView>(R.id.tv_report).text = "неверные данные"
            }

        }

    }

    override fun onResume() {
        super.onResume()
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100)
    }

    override fun onPause() {
        super.onPause()
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

        // Clear the systemUiVisibility flag
        activity?.window?.decorView?.systemUiVisibility = 0
        show()
    }

    override fun onDestroy() {
        super.onDestroy()
        dummyButton = null
        fullscreenContent = null
        fullscreenContentControls = null
    }

    private fun toggle() {
        if (visible) {
            hide()
        } else {
            show()
        }
    }

    private fun hide() {
        // Hide UI first
        fullscreenContentControls?.visibility = View.GONE
        visible = false

        // Schedule a runnable to remove the status and navigation bar after a delay
        hideHandler.removeCallbacks(showPart2Runnable)
        hideHandler.postDelayed(hidePart2Runnable, UI_ANIMATION_DELAY.toLong())
    }

    @Suppress("InlinedApi")
    private fun show() {
        // Show the system bar
        fullscreenContent?.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        visible = true

        // Schedule a runnable to display UI elements after a delay
        hideHandler.removeCallbacks(hidePart2Runnable)
        hideHandler.postDelayed(showPart2Runnable, UI_ANIMATION_DELAY.toLong())
        (activity as? AppCompatActivity)?.supportActionBar?.show()
    }

    /**
     * Schedules a call to hide() in [delayMillis], canceling any
     * previously scheduled calls.
     */
    private fun delayedHide(delayMillis: Int) {
        hideHandler.removeCallbacks(hideRunnable)
        hideHandler.postDelayed(hideRunnable, delayMillis.toLong())
    }

    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private const val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private const val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private const val UI_ANIMATION_DELAY = 300
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}
*
* */