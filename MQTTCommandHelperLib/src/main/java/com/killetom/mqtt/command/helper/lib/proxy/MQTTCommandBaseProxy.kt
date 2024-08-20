package com.killetom.mqtt.command.helper.lib.proxy

import com.killetom.mqtt.command.helper.lib.action.IMqttCommandProxyAction
import com.killetom.mqtt.command.helper.lib.action.IMqttConnectionChannelCallBack
import com.killetom.mqtt.command.helper.lib.action.IMqttMessageArrivedActionCall
import com.killetom.mqtt.command.helper.lib.exception.MQTTCommandConnectionException
import com.killetom.mqtt.command.helper.lib.exception.MQTTCommandInitException
import com.killetom.mqtt.command.helper.lib.setting.ClientData
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import java.util.concurrent.locks.ReadWriteLock
import java.util.concurrent.locks.ReentrantReadWriteLock

open class MQTTCommandBaseProxy : IMqttCommandProxyAction {

    private var messageArrivedActionCall: IMqttMessageArrivedActionCall? = null

    private var client: IMqttAsyncClient? = null
    private var options: MqttConnectOptions? = null

    private var clientData: ClientData? = null
    private var channelCallBack: IMqttConnectionChannelCallBack? = null
    private var initStatus: Boolean = false

    private val lock: ReadWriteLock = ReentrantReadWriteLock()
    private val readLock = lock.readLock()
    private val writeLock = lock.writeLock()


    override fun getClient(): IMqttAsyncClient? {
        return client
    }

    override fun getClientOptions(): MqttConnectOptions? {
        return options
    }

    override fun setConnectionChannelCallBack(callBack: IMqttConnectionChannelCallBack) {
        channelCallBack = callBack
    }

    override fun getConnectionChannelCallBack(): IMqttConnectionChannelCallBack? {
        return channelCallBack
    }

    override fun setMessageArrivedActionCall(call: IMqttMessageArrivedActionCall?) {
        messageArrivedActionCall = call
    }

    override fun requireMessageArrivedActionCall(): IMqttMessageArrivedActionCall? {
        return messageArrivedActionCall
    }

    override fun initClient(data: ClientData) {

        readLock.lock()

        if (initStatus) {
            readLock.unlock()
            throw MQTTCommandInitException("Client allReady init")
        }
        readLock.unlock()

        writeLock.lock()

        val currentOptions = getClientOptions() ?: MqttConnectOptions()
        currentOptions.isCleanSession = data.clearCacheStatus
        currentOptions.isAutomaticReconnect = data.autoReteryConnectStatus
        initOtherOptionSetting(currentOptions)

        val userName = data.userName
        val password = data.password

        if (userName.isNotEmpty() && password.isNotEmpty()) {
            currentOptions.password = password.toCharArray()
            currentOptions.userName = userName
        }

        options = currentOptions

        clientData = data

        initStatus = true

        writeLock.unlock()

    }

    protected open fun getMqttClientPersistence(): MqttClientPersistence {
        return MemoryPersistence()
//        return MqttDefaultFilePersistence(System.getProperty("java.io.tmpdir"))
    }


    protected open fun initOtherOptionSetting(options: MqttConnectOptions) {

    }

    override fun connectionAction() {

//        Log.i("connectionAction","System.time${System.currentTimeMillis()}")
        println("connectionAction:${System.currentTimeMillis()}")
        val currentOptions = getClientOptions()
            ?: throw MQTTCommandConnectionException("MqttConnectOptions was null")

        val lastClient = getClient()

        if (lastClient != null)
            throw MQTTCommandConnectionException("MqttClient already build to connected")

        val currentClientData =
            clientData ?: throw MQTTCommandConnectionException("ClientData was null")

        val url = currentClientData.servicerUrl.ifEmpty {
            throw MQTTCommandConnectionException("ClientData was null")
        }

        val clientId = currentClientData.clientId

        val clientPersistence = getMqttClientPersistence()
        val currentClient = MqttAsyncClient(url, clientId,clientPersistence)

        currentClient.setCallback(object : MqttCallbackExtended {

            override fun connectionLost(cause: Throwable?) {
                cause?.printStackTrace()
                loseConnectionAction(
                    cause ?: MQTTCommandConnectionException("connectionLost unknown")
                )
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                println("messageArrived-topic$topic,message$message")
                topic?.let { currentTopic ->
                    onMessageArrivedAction(currentTopic, message)
                }
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                println("deliveryComplete")
                token?.let { deliveryCompleteAction(it) }
            }

            override fun connectComplete(reconnect: Boolean, serverURI: String?) {
                println("connectComplete")
                connectionCompleteAction()
            }

        })

        clientConnectionAction(currentClient, currentOptions)

        client = currentClient

    }

    protected open fun clientConnectionAction(
        currentClient: IMqttAsyncClient,
        currentOptions: MqttConnectOptions
    ) {
        currentClient.connect(currentOptions, null, object : IMqttActionListener {

            override fun onSuccess(asyncActionToken: IMqttToken?) {
                connectionCompleteAction()
            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                loseConnectionAction(
                    exception ?: MQTTCommandConnectionException("connectionLost unknown")
                )
            }

        })
    }


    override fun dissConnectionAction() {
        try {
            getClient()?.disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            getClient()?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        options = null
        client = null
    }

    override fun getConnectionStatus(): Boolean = getClient()?.isConnected ?: false

    override fun commitClientSetting(data: ClientData) {
        initClient(data)
    }

}