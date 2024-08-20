package com.killetom.mqtt.command.helper

import com.killetom.mqtt.command.helper.lib.proxy.IMqttMessageBaseProxy
import com.killetom.mqtt.command.helper.lib.action.IMqttConnectionChannelCallBack
import com.killetom.mqtt.command.helper.lib.service.MqttBaseService
import org.eclipse.paho.client.mqttv3.*

class SimplerMqttService : MqttBaseService {

    private constructor(){
        getMQTTCommandProxyAction().setConnectionChannelCallBack(object :IMqttConnectionChannelCallBack{
            override fun connectionCompleteActionCall(status: Boolean, errorMessage: String?) {
               println("IMqttConnectionChannelCallBack-status$status errorMessage:${errorMessage}")

                if (status){
                    getMqttMessageProxy().subscribeMqttTopic("testtopic",0)

                    getMqttMessageProxy().sendMessage("testtopic","hello",1, successAction = {
                        println("send hello sucess")
                    }, failAction = {
                        println("send hellow fail ${it.message}")
                    })
                }
            }

            override fun loseConnectionActionCall(t: Throwable) {
                t.printStackTrace()
            }

        })
    }

    private val messageProxy by lazy { SimpleMessageProxy { getMQTTCommandProxyAction().getClient() } }

    override fun messageInitProxy(): IMqttMessageBaseProxy {
        return messageProxy
    }

    override fun startServiceRunOnThread(stareServiceUnit: () -> Unit) {
//        Thread{
            stareServiceUnit.invoke()
//        }.start()
    }

    override fun destroy() {


    }

    companion object{
        val instance by lazy { SimplerMqttService() }
    }

    class SimpleMessageProxy(requestClient: (() -> IMqttAsyncClient?)?) :
        IMqttMessageBaseProxy(requestClient) {

        override fun onMessageArrivedActionCall(topic: String, message: MqttMessage?) {
            println("SimpleBaseProxy-topic:$topic\n message:$message")
        }

        override fun sendMessage(
            topic: String,
            message: MqttMessage,
            successAction: () -> Unit,
            failAction: (Throwable) -> Unit
        ) {
            super.sendMessage(topic, message, successAction, failAction)
        }

        override fun subscribeMqttTopic(topic: Array<String>, qos: IntArray?) {

        }

        override fun justSendMessage(topic: String, message: MqttMessage) {
            super.justSendMessage(topic, message)
        }

        override fun subscribeMqttTopic(
            topic: String,
            qos: Int,
            success: () -> Unit,
            fail: (errMessage: String) -> Unit
        ) {
            requestClient?.invoke()?.subscribe(topic, qos, this::onMessageArrivedActionCall)
        }

        override fun subscribeMqttTopic(topic: String, qos: Int) {
            requestClient?.invoke()?.subscribe(topic, qos)
        }

        override fun unsubscribeMqttTopic(topic: Array<String>) {
            requestClient?.invoke()?.unsubscribe(topic)
        }

        override fun unsubscribeMqttTopic(topic: String?) {
            requestClient?.invoke()?.unsubscribe(topic)
        }


    }
}