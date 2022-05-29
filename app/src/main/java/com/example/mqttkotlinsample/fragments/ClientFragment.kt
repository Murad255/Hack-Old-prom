package com.example.mqttkotlinsample.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf

import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mqttkotlinsample.*
import com.example.mqttkotlinsample.data.ChallengeHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

import com.example.mqttkotlinsample.fragments.data.ChallengeRecyclerViewAdapter


class ClientFragment : Fragment() {


    private var layoutManager: RecyclerView.LayoutManager?=null
    private var adapter: ChallengeRecyclerViewAdapter?=null
    var size = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var map = HashMap<Int, String>();

        layoutManager = LinearLayoutManager(context)
        adapter = ChallengeRecyclerViewAdapter(ArrayList(COM_challengeHandler.challenges.values),{x:Int->COM_StartMove(x)},{x:Int->openStatusFragment(x)})

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_client, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.challenge_list)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        /////////отправить///////
//        view.findViewById<Button>(R.id.button_StartProcess).setOnClickListener{
//            view.findNavController().navigate(R.id.action_ClientFragment_to_confirmSelectionFragment)
//            childFragmentManager.beginTransaction().replace(R.id.nav_fragment,ConfirmSelectionFragment()).commit()
//
//        }
//
//        view.findViewById<Button>(R.id.button_StartProcess2).setOnClickListener{
//            view.findNavController().navigate(R.id.action_ClientFragment_to_confirmSelectionFragment)
//        }
//
        val scope: ClientFragment = this

        CoroutineScope(Dispatchers.IO).launch {

            while (true){
                delay(400)
                //if (COM_challengeHandler!!.challenges.size!=size || COM_challengeHandler.IsEdit) {
                    COM_challengeHandler.IsEdit=false
                    scope.getActivity()?.runOnUiThread(java.lang.Runnable {
                        updateList()
                    })

              //  }
            }

        }


    }



    fun updateList(){
        size=COM_challengeHandler!!.challenges.size
        var newList: MutableList<ChallengeHandler.Challenge> = mutableListOf()

        for (chal in COM_challengeHandler.challenges.values){
            newList.add(chal)
        }
        adapter!!.updateItem(newList)
    }


    fun openStatusFragment(id:Int){

        COM_mainActivity!!.supportFragmentManager.beginTransaction().replace(
            R.id.nav_fragment,
            OrderStatusFragment(id)
        ).commit()
      // Navigation.findNavController().navigate(R.layout.fragment_confirm_selection)
    }
 var iterator:Boolean = false
    override fun  onResume(){
        super.onResume()
        if(iterator)
            COM_mainActivity!!.supportFragmentManager.beginTransaction().replace(
            R.id.nav_fragment,
            ClientFragment()
        ).commit()
        iterator = true
    }

}

