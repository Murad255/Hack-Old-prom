package com.example.mqttkotlinsample.fragments.data

import androidx.recyclerview.widget.DiffUtil
import com.example.mqttkotlinsample.data.OrderStatus

class OrderStatusCallback (private  val oldList: List<OrderStatus>,
                           private  val newList: List<OrderStatus>)
    : DiffUtil.Callback()
{
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItemPosition==newItemPosition
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].OrderStatusID==oldList[newItemPosition].OrderStatusID
    }

}