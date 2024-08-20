package com.killetom.mqtt.command.helper.lib.action

import org.eclipse.paho.client.mqttv3.MqttMessage

interface IMqttMessageArrivedActionCall {

    fun onMessageArrivedActionCall(topic: String, message: MqttMessage?)
}