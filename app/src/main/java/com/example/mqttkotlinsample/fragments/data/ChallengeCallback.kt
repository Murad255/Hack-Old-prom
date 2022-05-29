package com.example.mqttkotlinsample.fragments.data

import androidx.recyclerview.widget.DiffUtil
import com.example.mqttkotlinsample.data.ChallengeHandler

class ChallengeCallback(private  val oldList: List<ChallengeHandler.Challenge>,
                        private  val newList: List<ChallengeHandler.Challenge>)
    :DiffUtil.Callback()
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
        if ( (oldList[oldItemPosition].ChallengeID==oldList[newItemPosition].ChallengeID) && (oldList[oldItemPosition].IsDelivered==oldList[newItemPosition].IsDelivered))
            return true

        return false
    }

}