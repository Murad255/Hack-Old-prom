package com.example.mqttkotlinsample.fragments.data

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mqttkotlinsample.COM_context
import com.example.mqttkotlinsample.R
import com.example.mqttkotlinsample.data.ChallengeHandler




class ChallengeRecyclerViewAdapter(private val challenges: MutableList<ChallengeHandler.Challenge>,  val publishId: (Int)->Unit, val publishStatusId: (Int)->Unit  ): RecyclerView.Adapter<ChallengeRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder( LayoutInflater.from(parent.context).inflate(R.layout.challenge_item,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.numbersText.text = challenges[position].Numbers
        holder.dataText.text = challenges[position].Time
        holder.statusText.text = challenges[position].DeliveryStatus

        if (!challenges[position].IsDelivered) {
//            holder.confirmButton.setOnClickListener {
//                publishId(challenges[position].ChallengeID!!)
//            }
            holder.statusButton.setOnClickListener {
                publishStatusId(challenges[position].ChallengeID!!)
            }

//            if(!challenges[position].IsCompliteDelivery()){
//                holder.confirmButton.isEnabled = false
//                holder.confirmButton.isClickable = false
//                holder.confirmButton.setTextColor(ContextCompat.getColor(COM_context!!, R.color.white))
//                holder.confirmButton.setBackgroundColor(ContextCompat.getColor(COM_context!!, R.color.gray))
//            }

        }
        else {
           // markButtonDisable(holder.confirmButton)
            markButtonDisable(holder.statusButton)
            holder.statusText.setTextColor(ContextCompat.getColor(COM_context!!,R.color.colorRedButton))

        }
    }

    fun markButtonDisable(button: Button) {

        button.visibility = View.GONE
//        button?.isEnabled = false
//        button?.setTextColor(ContextCompat.getColor(COM_context!!, R.color.white))
//        button?.setBackgroundColor(ContextCompat.getColor(COM_context!!, R.color.black))
    }

    override fun getItemCount(): Int {
        return challenges.size
    }

    fun insertItem(newList:List<ChallengeHandler.Challenge>){
        val diffUilCallback = ChallengeCallback(challenges,newList)
        val diffResult: DiffUtil.DiffResult= DiffUtil.calculateDiff(diffUilCallback)

        challenges.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    public fun updateItem(newList:List<ChallengeHandler.Challenge>){
        val diffUilCallback = ChallengeCallback(challenges,newList)
        val diffResult: DiffUtil.DiffResult= DiffUtil.calculateDiff(diffUilCallback)

        challenges.clear()
        challenges.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val numbersText: TextView = itemView.findViewById(R.id.tv_chalenge_numbers)
        val dataText: TextView = itemView.findViewById(R.id.tv_chalenge_data)
        val statusText: TextView = itemView.findViewById(R.id.tv_status)
        //val confirmButton: Button = itemView.findViewById(R.id.bt_chalenge_start_move)
        val statusButton: Button = itemView.findViewById(R.id.bt_status)

        override fun toString(): String {
            return  " '" + numbersText + "'"
        }
    }

}