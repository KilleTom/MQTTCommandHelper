package com.killetom.mqtt.command.helper.lib.exception

class MQTTCommandException(override val message:String) :Exception(message) {
}

class MQTTCommandInitException(override val message:String) :Exception(message) {
}

class MQTTCommandConnectionException(override val message:String) :Exception(message) {
}