package com.killetom.mqtt.command.helper.lib.action

interface IMqttTopicAction {

    @Throws(Exception::class)
    fun subscribeMqttTopic(topic: Array<String>, qos: IntArray?)

    @Throws(Exception::class)
    fun subscribeMqttTopic(
        topic: String,
        qos: Int,
        success:(()->Unit),
        fail:((errMessage:String)->Unit)
    )

    @Throws(Exception::class)
    fun subscribeMqttTopic(topic: String, qos: Int)

    @Throws(Exception::class)
    fun unsubscribeMqttTopic(topic: Array<String>)

    @Throws(Exception::class)
    fun unsubscribeMqttTopic(topic: String?)


}