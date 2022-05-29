package com.example.mqttkotlinsample.fragments.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mqttkotlinsample.COM_context
import com.example.mqttkotlinsample.R
import com.example.mqttkotlinsample.data.OrderStatus


class OrderStatusRecyclerViewAdapter(private val orderStatuses: MutableList<OrderStatus>): RecyclerView.Adapter<OrderStatusRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.status_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.DataText.text = orderStatuses[position].Data
     //   holder.TimeText.text = orderStatuses[position].Time
       // holder.StatusText.text = orderStatuses[position].Status
       // holder.PlaceText.text = orderStatuses[position].Place
        var color: Int  = getColor(
            COM_context!!,
            R.color.colorPrimary)
        if (orderStatuses[position].IsCompliteStatus == 0)
            color  = getColor(
                COM_context!!,
                R.color.colorPrimaryRed)
        holder.DataText.setTextColor(color)
       // holder.TimeText.setTextColor(color)
       // holder.StatusText.setTextColor(color)
       // holder.PlaceText.setTextColor(color)

    }

    override fun getItemCount(): Int {
        return orderStatuses.size
    }

    fun insertItem(newList:List<OrderStatus>){
        val diffUilCallback = OrderStatusCallback(orderStatuses,newList)
        val diffResult: DiffUtil.DiffResult= DiffUtil.calculateDiff(diffUilCallback)

        orderStatuses.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    public fun updateItem(newList:List<OrderStatus>){
        val diffUilCallback = OrderStatusCallback(orderStatuses,newList)
        val diffResult: DiffUtil.DiffResult= DiffUtil.calculateDiff(diffUilCallback)

        orderStatuses.clear()
        orderStatuses.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val DataText: TextView = itemView.findViewById(R.id.tv_data)
        //val TimeText: TextView = itemView.findViewById(R.id.tv_time)
      //  val StatusText: TextView = itemView.findViewById(R.id.tv_status)
       // val PlaceText: TextView = itemView.findViewById(R.id.tv_place)

        override fun toString(): String {
            return  " '" + DataText + "'"
        }
    }

}