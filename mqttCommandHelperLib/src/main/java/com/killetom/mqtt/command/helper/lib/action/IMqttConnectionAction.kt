package com.killetom.mqtt.command.helper.lib.action

import com.killetom.mqtt.command.helper.lib.setting.ClientData
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttMessage

interface IMqttConnectionAction {

    fun connectionAction()

    fun dissConnectionAction()

    fun connectionCompleteAction(){
        getConnectionChannelCallBack()?.connectionCompleteActionCall(true,null)
    }

    fun loseConnectionAction(t:Throwable){
        getConnectionChannelCallBack()?.loseConnectionActionCall(t)
    }

    fun onMessageArrivedAction(topic: String, message: MqttMessage?){
        requireMessageArrivedActionCall()?.onMessageArrivedActionCall(topic,message)
    }

    fun deliveryCompleteAction( token: IMqttDeliveryToken){
        getConnectionChannelCallBack()?.deliveryCompleteCall(token)
    }

    fun getConnectionStatus(): Boolean

    fun commitClientSetting(data: ClientData)

    fun setConnectionChannelCallBack(callBack: IMqttConnectionChannelCallBack)

    fun getConnectionChannelCallBack():IMqttConnectionChannelCallBack?

    fun requireMessageArrivedActionCall():IMqttMessageArrivedActionCall?

    fun setMessageArrivedActionCall(call:IMqttMessageArrivedActionCall?)
}