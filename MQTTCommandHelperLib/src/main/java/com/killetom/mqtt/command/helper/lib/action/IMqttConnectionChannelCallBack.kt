package com.killetom.mqtt.command.helper.lib.action

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken

interface IMqttConnectionChannelCallBack {

    fun connectionCompleteActionCall(status:Boolean,errorMessage:String?)

    fun loseConnectionActionCall(t:Throwable)

    fun deliveryCompleteCall( token: IMqttDeliveryToken){

    }
}