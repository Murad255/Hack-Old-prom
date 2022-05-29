package com.example.mqttkotlinsample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.mqttkotlinsample.COM_mainActivity
import com.example.mqttkotlinsample.R
import com.example.mqttkotlinsample.mqtt.MQTT_PWD_KEY
import com.example.mqttkotlinsample.mqtt.MQTT_USERNAME_KEY



class ConfirmSelectionFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.confirm_button).setOnClickListener {
            // findNavController().navigate(R.id.action_confirmSelectionFragment_to_ClientFragment)
            COM_mainActivity!!.supportFragmentManager.beginTransaction().replace(
                R.id.nav_fragment,
                ClientFragment()
            ).commit()
        }
    }

}