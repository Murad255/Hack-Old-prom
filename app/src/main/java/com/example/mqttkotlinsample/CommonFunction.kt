package com.example.mqttkotlinsample

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.mqttkotlinsample.data.ChallengeHandler
import com.example.mqttkotlinsample.data.OrderStatus
import com.example.mqttkotlinsample.fragments.ClientFragment
import com.example.mqttkotlinsample.fragments.OrderStatusFragment
import com.example.mqttkotlinsample.mqtt.*
import com.example.mqttkotlinsample.services.*
import kotlinx.coroutines.delay
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttToken
import java.util.HashMap

var COM_MqttClient: MQTTClient ?= null
var COM_context: Context?=null
var COM_mainActivity: MainActivity?=null
var COM_userlogin: String = ""
var COM_notification: Notification?= null
val COM_challengeHandler: ChallengeHandler = ChallengeHandler()

var COM_orderStatus:HashMap<Int, OrderStatus>?= null


fun COM_MqttBegin(context: Context?){
    COM_MqttClient = MQTTClient(context)
    COM_MqttClient!!.connect(MQTT_USERNAME,MQTT_PWD)
    COM_context=context

    var orderStatus:HashMap<Int, OrderStatus> = hashMapOf(
        2 to  OrderStatus(2,"7598245","14:39","Доствлен в ППУ №7","Корпус Б, Этаж 7"),
        3 to OrderStatus(3,"5234523","10:35","Ожидание лифта № 1043","Корпус Б, Этаж 1"),
        4 to OrderStatus(4,"6324624","10:30","Ожидание двери № 1095","Корпус Б, Этаж 1"),
        5 to OrderStatus(5,"2346243","10:27","Ожидание двери № 1099-3","Корпус Б, Этаж 1"),
        6 to OrderStatus(6,"3642434","10:24","Ожидание двери № 1099-2","Корпус Б, Этаж 1" ),
        7 to OrderStatus(7,"3462436","10:20","Начало движения","Аптека, Этаж 1")
    )
    var orderStatusNotComplited:HashMap<Int, OrderStatus> = hashMapOf(
        4 to OrderStatus(4,"7598245","10:30","Ожидание двери № 1095","Корпус Б, Этаж 1"),
        5 to OrderStatus(5,"5234523","10:27","Ожидание двери № 1099-3","Корпус Б, Этаж 1"),
        6 to OrderStatus(6,"5234523","10:24","Ожидание двери № 1099-2","Корпус Б, Этаж 1" ),
        7 to OrderStatus(7,"5234523","10:20","Начало движения","Аптека, Этаж 1")
    )

    val noButtonText = "Нет свободных товаров"
    COM_orderStatus = orderStatus

    COM_challengeHandler.Handler(1,"Медные листы и плиты М1","510310622","В процессе доставки", orderStatus,false)
    COM_challengeHandler.Handler(2,"Нержавеющие квадраты AISI 304","39163106","В процессе доставки", orderStatus,false)
    COM_challengeHandler.Handler(3,"Нержавеющие круги 12Х18Н10Т","31064202",noButtonText)
    COM_challengeHandler.Handler(4,"Алюминиевые квадраты Д16Т","41042022",noButtonText)
    COM_challengeHandler.Handler(5,"Медные прутки М1т","5042022",noButtonText)
    COM_challengeHandler.Handler(6,"Нержавеющие полосы AISI 304","71043106","В процессе доставки", orderStatusNotComplited,false)
    COM_challengeHandler.Handler(7,"Латунные листы ЛС59-1","91031062","В процессе доставки", orderStatus,false)



}

fun COM_Autorization(login: String, password: String): Boolean{
    if (!COM_MqttClient!!.isSubscribe) {
        COM_MqttClient!!.subscribe(IN_WATCHER,QOS)
    } else {
        Log.d("MQTTClient", "Failed to subscribe, no server connected")
    }
    var loginStr  ="<Module><UserLogin>"+login+"</UserLogin><UserPwd>"+password+"</UserPwd></Module>"
    COM_MqttClient!!.publish(OUT_WATCHER,loginStr)

    return false//COM_MqttClient!!.waitingLoginStatusСonfirmed()
}

fun COM_StartMove(id:Int){
    var loginStr  ="<OrderID>"+id.toString()+"</OrderID><DeliveryStatus>0</DeliveryStatus>"
    COM_MqttClient!!.publishData(loginStr)
    COM_challengeHandler.challenges[id]!!.IsDelivered=true
    COM_challengeHandler.challenges[id]!!.DeliveryStatus="Доставлено"
    COM_challengeHandler.IsEdit = true
    COM_mainActivity!!.supportFragmentManager.beginTransaction().replace(
        R.id.nav_fragment,
        ClientFragment()
    ).commit()
}

fun COM_actionOnService(action: Actions) {
    if (getServiceState(COM_context!!.applicationContext) == ServiceState.STOPPED && action == Actions.STOP) return
    Intent(COM_context, EndlessService::class.java).also {
        it.action = action.name
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("Service","Starting the service in >=26 Mode")
            COM_context!!.startForegroundService(it)
            return
        }
        Log.d("Service","Starting the service in < 26 Mode")
        COM_context!!.startService(it)
    }
}

fun COM_ServiceTask() {
    Log.d("Service","Service Task")
}