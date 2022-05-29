package com.example.mqttkotlinsample.data

import android.widget.Button
import android.widget.TextView
import com.example.mqttkotlinsample.COM_challengeHandler
import com.example.mqttkotlinsample.COM_mainActivity
import com.example.mqttkotlinsample.COM_notification
import com.example.mqttkotlinsample.R
import com.example.mqttkotlinsample.fragments.ClientFragment
import com.example.mqttkotlinsample.mqtt.Message
import kotlinx.coroutines.delay
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ChallengeHandler {

    //var challenges: Queue<Challenge> = LinkedList<Challenge>()
     var challenges: HashMap<Int, Challenge> = HashMap<Int, Challenge>()
    var IsEdit = false
    suspend fun update(){

//        while (true) {
//            if(messages.size>0){
//                val outMes: Message = messages.firstElement()
//                status = outMes.LoginStatus
//                if (status ==null) delayToError -= 1
//                else if (status.equals("cancel") ) return false
//                else if (status.equals("confirm") ) return true
//            }
//            else delayToError -= 1
//
//            if (delayToError <= 0) throw Exception("Error: waiting")
//            delay(500)  //Thread.sleep(500)
//        }
    }

     fun Handler( challengeID: Int,
                  numbers: String, time: String,deliveryStatus: String,
                  orderStatuses: HashMap<Int, OrderStatus> = HashMap<Int, OrderStatus>(),
                  isDelivered: Boolean = true){

        var challenge =  Challenge(
            challengeID,
            numbers,time,deliveryStatus,
            orderStatuses,
            isDelivered
        )
        if(this.challenges.get(  challenge.ChallengeID!!) == null) {
            challenges.put(challenge.ChallengeID!!,challenge)
           if(!challenge.IsDelivered && challenge.IsCompliteDelivery()) COM_notification!!.sentNotification("Заказ №"+numbers,"Заказ прибыл в ППУ")
            COM_mainActivity!!.supportFragmentManager.beginTransaction().replace(
                R.id.nav_fragment,
                ClientFragment()
            ).commit()
        }
    }


    fun IsAvailable (): Boolean {
        return challenges.size>0
    }


    inner class Challenge(challengeID: Int,
                          numbers: String,time: String,
                          deliveryStatus: String,
                          orderStatuses: HashMap<Int, OrderStatus>,
                          isDelivered: Boolean){

        var OrderStatuses: HashMap<Int, OrderStatus> = orderStatuses

        var ChallengeID: Int =challengeID
        var Numbers: String = numbers
        var Time: String = time
        var DeliveryStatus: String = deliveryStatus
        var IsDelivered: Boolean  = isDelivered

        fun IsCompliteDelivery():Boolean{
            // [OrderStatuses.values.size]
            var filteredValuesMap = OrderStatuses.filterValues{  it.IsCompliteStatus ==0}
            if (filteredValuesMap.size>0) return true
            return false
        }
    }
}


