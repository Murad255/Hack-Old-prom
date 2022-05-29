package com.example.mqttkotlinsample.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mqttkotlinsample.COM_StartMove
import com.example.mqttkotlinsample.COM_challengeHandler
import com.example.mqttkotlinsample.R
import com.example.mqttkotlinsample.fragments.data.OrderStatusRecyclerViewAdapter


class OrderStatusFragment(orderId:Int) : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager?=null
    private var adapter: OrderStatusRecyclerViewAdapter?=null
    var size = 0
    var OrderId:Int = orderId

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutManager = LinearLayoutManager(context)
        adapter = OrderStatusRecyclerViewAdapter(ArrayList(COM_challengeHandler.challenges[OrderId]!!.OrderStatuses.values))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chalenge_info: TextView = view.findViewById<TextView>(R.id.tv_chalenge_info)
        val chalenge_numbers: TextView = view.findViewById<TextView>(R.id.tv_chalenge_numbers)
        val box_counts: TextView = view.findViewById<TextView>(R.id.tv_box_counts)

        chalenge_info.text = COM_challengeHandler.challenges[OrderId]!!.Numbers
        chalenge_numbers.text = COM_challengeHandler.challenges[OrderId]!!.ChallengeID.toString()
        box_counts.text = COM_challengeHandler.challenges[OrderId]!!.OrderStatuses.size.toString()+" шт"

        val recyclerView: RecyclerView = view.findViewById<RecyclerView>(R.id.order_status_list)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        }
}