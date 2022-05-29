package com.example.mqttkotlinsample.mqtt

const val MQTT_USERNAME_KEY     = "MQTT_USERNAME"
const val MQTT_PWD_KEY          = "MQTT_PWD"

//const val MQTT_SERVER_URI       = "tcp://172.16.2.25:1883"
const val MQTT_SERVER_URI       = "tcp://mqtt.eclipseprojects.io:1883"
const val MQTT_LOCAL_SERVER_URI       = "tcp://172.16.2.25:1883"

const val MQTT_USERNAME         = "user"
const val MQTT_PWD              = "quantumsystems"

const val IN_DEVICES         = "userM/devices/in"
const val OUT_DEVICES        = "userM/devices/out"

const val IN_WATCHER = "userM/devices/in" //"userM/watcher/in"
const val OUT_WATCHER = "userM/devices/out" //"userM/watcher/out"

const val QOS = 1
const val UID = 0

var LOG_TAG  = "MQTTClient"
