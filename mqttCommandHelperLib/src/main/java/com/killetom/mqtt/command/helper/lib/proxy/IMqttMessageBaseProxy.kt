package com.killetom.mqtt.command.helper.lib.proxy

import com.killetom.mqtt.command.helper.lib.action.IMqttMessageArrivedActionCall
import com.killetom.mqtt.command.helper.lib.action.IMqttProtocolMessageAction
import com.killetom.mqtt.command.helper.lib.action.IMqttTopicAction
import com.killetom.mqtt.command.helper.lib.exception.MQTTCommandException
import org.eclipse.paho.client.mqttv3.*

abstract class IMqttMessageBaseProxy(protected val requestClient:(()-> IMqttAsyncClient?)?) : IMqttProtocolMessageAction, IMqttTopicAction,
    IMqttMessageArrivedActionCall {

    override fun justSendMessage(topic: String, message: MqttMessage) {
        val client = requestClient?.invoke()?:null

        client?.let {
            it.publish(topic, message)
        }
    }

    override fun sendMessage(
        topic: String, message: MqttMessage, successAction: (() -> Unit),
        failAction: ((Throwable) -> Unit)) {
        val client = requestClient?.invoke()?:null

        client?.publish(topic, message,null,object : IMqttActionListener{

            override fun onSuccess(asyncActionToken: IMqttToken?) {
                successAction.invoke()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                failAction.invoke(exception?: MQTTCommandException("unknown"))
            }
        })
    }

}