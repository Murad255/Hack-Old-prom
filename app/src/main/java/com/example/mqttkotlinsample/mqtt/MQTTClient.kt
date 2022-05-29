package com.example.mqttkotlinsample.mqtt

import android.content.Context
import android.util.Log
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.*

import java.lang.Exception
import java.util.*
import kotlin.collections.HashMap

import com.example.mqttkotlinsample.COM_challengeHandler
import com.example.mqttkotlinsample.COM_orderStatus
import com.example.mqttkotlinsample.COM_userlogin
import com.example.mqttkotlinsample.data.ChallengeHandler
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat


class MQTTClient(context: Context?) {

    private val rnds = (1000..1000000).random()
    private val usrContext = context
    private var mqttClient = MqttAndroidClient(context, MQTT_SERVER_URI, "user"+rnds.toString())
    private var mqttClientLocal = MqttAndroidClient(context, MQTT_LOCAL_SERVER_URI, "user"+rnds.toString())

    var messages:Queue<Message> = LinkedList<Message>()
    var pastMessage: Message? = null

    var  isSubscribe: Boolean = false
    var receivedMessages :HashMap<String,String> = HashMap<String,String>() //hashMapOf()

    private val defaultCbConnect = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(LOG_TAG, "Connection success")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(LOG_TAG, "Connection failure: ${exception.toString()}")
        }
    }


    private val defaultCbClient = object : MqttCallback {
        override fun messageArrived(topic: String?, message: MqttMessage?) {

            val msg = Message(message.toString(), topic)
           // messages.push(msg)
            pastMessage = msg
            if (msg.isModule()) {
                if (msg.name.equals( COM_userlogin)) {

                    if (msg.Request != null) {
                        if (msg.Request.equals("status")) {
                            var sentStr =
                                "<Module><Name>" + COM_userlogin + "</Name><Status>0</Status></Module>"
                            publish(OUT_WATCHER, sentStr)
                        }

                    }
                    messages.add(msg)
                    if (msg.OrderID != null && msg.OrderID != -1) {
                        var info = "11.04.2022"
                        val currentDate: String = SimpleDateFormat("dd.M.yyyy").format(Date())
                        val currentTime: String = SimpleDateFormat("HH:mm:ss").format(Date())
//                        if (msg.TypeGateway.equals("lift")){
//                            info+= "ожидание лифта № " +msg.Number.toString()
//                        }
//                        else{
//                            info+= "Ожидание двери № " +msg.Number.toString()
//                        }

                        COM_challengeHandler.Handler(
                            msg.OrderID,
                    //        "Заказ №"+msg.ChallengeNumbers.toString(),
                            msg.ChallengeNumbers.toString(),
                            currentDate,"В процессе доставки", COM_orderStatus!!,false
                        )


                    }
                }
            }
//            if (msg.isLoginStatusReport()) {
//                val dbDataXml: String = DataConnectorHandler.onMessageReceived(msg)
//                if (dbDataXml != null) IotModules.Publish(
//                    Message.GenerateToDevice(msg.getBdType(), dbDataXml),
//                    //inDevices.toString() + "/" + msg.name
//                )
//            }
            Log.d(LOG_TAG, "Receive message: ${message.toString()} from topic: $topic")
        }

        override fun connectionLost(cause: Throwable?) {
            Log.d(LOG_TAG, "Connection lost ${cause.toString()}")
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            Log.d(LOG_TAG, "Delivery completed")
        }
    }
    private val defaultCbSubscribe = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(LOG_TAG, "Subscribed to topic")
            isSubscribe = true
            //  Toast.makeText(COM_context, msg, Toast.LENGTH_SHORT).show()
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(LOG_TAG, "Failed to subscribe: $IN_WATCHER")
        }
    }


    private val defaultCbUnsubscribe = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(LOG_TAG, "Unsubscribed to topic")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(LOG_TAG, "Failed to unsubscribe topic")
        }
    }
    private val defaultCbPublish = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(LOG_TAG, "Message published to topic")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(LOG_TAG, "Failed to publish message to topic")
        }
    }
    private val defaultCbDisconnect = object : IMqttActionListener {
        override fun onSuccess(asyncActionToken: IMqttToken?) {
            Log.d(LOG_TAG, "Disconnected")
        }

        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
            Log.d(LOG_TAG, "Failed to disconnect")
        }
    }

    fun connect(username:   String               = "",
                password:   String               = "",
                cbConnect:  IMqttActionListener  = defaultCbConnect,
                cbClient:   MqttCallback         = defaultCbClient) {

        mqttClient.setCallback(cbClient)
        mqttClientLocal.setCallback(cbClient)

        val options = MqttConnectOptions()
        options.userName = username
        options.password = password.toCharArray()

        try {
            mqttClient.connect(options, null, cbConnect)
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.d(LOG_TAG, "connect to MqttException")

        }
        try {
            mqttClientLocal.connect(options, null, cbConnect)
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.d(LOG_TAG, "connect to MqttException")

        }
    }

    fun isConnected(): Boolean {
        return mqttClient.isConnected || mqttClientLocal.isConnected
    }

    fun subscribe(topic:        String       = IN_WATCHER,
                  qos:          Int                 = QOS,
                  cbSubscribe:  IMqttActionListener = defaultCbSubscribe) {
        try {
            mqttClient.subscribe(topic, qos, usrContext, cbSubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.d(LOG_TAG, "subscribe to MqttException")
        }

        try {
            mqttClientLocal.subscribe(topic, qos, usrContext, cbSubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.d(LOG_TAG, "subscribe to MqttException")
        }
    }

    fun unsubscribe(topic:          String,
                    cbUnsubscribe:  IMqttActionListener = defaultCbUnsubscribe) {
        try {
            mqttClient.unsubscribe(topic, null, cbUnsubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
        try {
            mqttClientLocal.unsubscribe(topic, null, cbUnsubscribe)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic:      String,
                msg:        String,
                qos:        Int                 = QOS,
                retained:   Boolean             = false,
                cbPublish:  IMqttActionListener = defaultCbPublish) {
        val message = MqttMessage()
        message.payload = msg.toByteArray()
        message.qos = qos
        message.isRetained = retained
        try {
            mqttClient.publish(topic, message, null, cbPublish)
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.d(LOG_TAG, "publish to global MqttException")
        }
        try {
            mqttClientLocal.publish(topic, message, null, cbPublish)
        } catch (e: MqttException) {
            e.printStackTrace()
            Log.d(LOG_TAG, "publish to local MqttException")

        }
    }

    fun publishData(msg: String) {

        var sentMes = "<Module><Name>"+ COM_userlogin+"</Name><ModuleType>GatewayOpener</ModuleType>"+
                "<Data>"+msg+"</Data></Module>"
        publish(OUT_WATCHER,sentMes)

    }

    fun disconnect(cbDisconnect: IMqttActionListener = defaultCbDisconnect ) {
        try {
            mqttClient.disconnect(null, cbDisconnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
        try {
            mqttClientLocal.disconnect(null, cbDisconnect)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    ///проверить отправку статуса
    fun statusСonfirmed(name: String?): Int {
        if(messages.size>0) {
            val outMes: Message = messages.poll()
            return outMes.status
        }
        return -1
    }

    suspend fun waitingLoginStatusСonfirmed(): Boolean {
        var status: String
        var statusCounter = 0
        var delayToError = 100
        //  InDevicesData(ModuleType.all, name, "<Request>status</Request>")
        while (true) {
            if(messages.size>0){
                val outMes: Message = messages.poll()
                status = outMes.LoginStatus
                if (status ==null) delayToError -= 1
                else if (status.equals("cancel") ) return false
                else if (status.equals("confirm") ) return true
            }
            else delayToError -= 1

            if (delayToError <= 0) throw Exception("Error: waiting")
            delay(500)  //Thread.sleep(500)
        }
    }}