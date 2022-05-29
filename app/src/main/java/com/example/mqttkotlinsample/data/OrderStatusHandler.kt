package com.example.mqttkotlinsample.data

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.mqttkotlinsample.COM_context
import com.example.mqttkotlinsample.COM_notification
import com.example.mqttkotlinsample.R
import java.util.HashMap

//class OrderStatusHandler {
//    var orderStatuses: HashMap<Int, OrderStatus> = HashMap<Int, OrderStatus>()
//
//    fun Handler( orderStatusID: Int,data: String,time: String, status: String,place: String,
//                 isCompliteStatus: Int =   1)
//    {
//        var orderStatus =  OrderStatus(orderStatusID, data, time, status, place,isCompliteStatus)
//
//        if(this.orderStatuses.get(  orderStatus.OrderStatusID!!) == null) {
//            orderStatuses.put(orderStatus.OrderStatusID!!,orderStatus)
//            //COM_notification!!.sentNotification(numbers,"Заказ прибыл в ППУ")
//        }
//    }
//
//}