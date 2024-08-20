package com.killetom.mqtt.command.helper.lib.action

import com.killetom.mqtt.command.helper.lib.setting.ClientData
import org.eclipse.paho.client.mqttv3.IMqttAsyncClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions

interface IMqttCommandProxyAction :IMqttConnectionAction {

    @kotlin.jvm.Throws
    fun initClient(data: ClientData)

    fun getClient():IMqttAsyncClient?

    fun getClientOptions(): MqttConnectOptions?
}