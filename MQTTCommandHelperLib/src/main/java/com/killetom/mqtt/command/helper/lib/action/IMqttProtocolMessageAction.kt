package com.killetom.mqtt.command.helper.lib.action

import org.eclipse.paho.client.mqttv3.MqttMessage

interface IMqttProtocolMessageAction {

    fun getDefaultQos(): Int = 0

    @kotlin.jvm.Throws
    fun justSendMessage(
        topic: String,
        message: String,
        qos: Int,
        retained: Boolean = false
    ) {

        val mqttMessage = MqttMessage()

        if (qos in 0..2) {
            mqttMessage.qos = qos
        } else {
            mqttMessage.qos = getDefaultQos()
        }
        mqttMessage.payload = message.toByteArray()
        mqttMessage.isRetained = retained
    }


    @kotlin.jvm.Throws
    fun justSendMessage(topic: String, message: MqttMessage)

    @kotlin.jvm.Throws
    fun sendMessage(
        topic: String,
        message: String,
        qos: Int,
        successAction: (() -> Unit),
        failAction: ((Throwable) -> Unit),
        retained: Boolean = false
    ) {

        val mqttMessage = MqttMessage()

        if (qos in 0..2) {
            mqttMessage.qos = qos
        } else {
            mqttMessage.qos = getDefaultQos()
        }
        mqttMessage.payload = message.toByteArray()
        mqttMessage.isRetained = retained

        sendMessage(topic, mqttMessage, successAction, failAction)
    }

    @kotlin.jvm.Throws
    fun sendMessage(
        topic: String, message: MqttMessage, successAction: (() -> Unit),
        failAction: ((Throwable) -> Unit),
    )
}