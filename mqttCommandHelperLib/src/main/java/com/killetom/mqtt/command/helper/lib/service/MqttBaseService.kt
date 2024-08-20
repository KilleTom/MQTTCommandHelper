package com.killetom.mqtt.command.helper.lib.service

import com.killetom.mqtt.command.helper.lib.proxy.IMqttMessageBaseProxy
import com.killetom.mqtt.command.helper.lib.action.IMqttCommandProxyAction
import com.killetom.mqtt.command.helper.lib.proxy.MQTTCommandBaseProxy
import com.killetom.mqtt.command.helper.lib.setting.ClientData

abstract class MqttBaseService {

    private val proxy by lazy { MQTTCommandBaseProxy() }

    private val messageProxy by lazy { messageInitProxy()  }

    protected abstract fun messageInitProxy(): IMqttMessageBaseProxy

    @kotlin.jvm.Throws
    open fun initService(settingData: ClientData) {

        initClient(settingData)
    }

    @kotlin.jvm.Throws
    protected open fun initClient(settingData: ClientData){
        getMQTTCommandProxyAction().commitClientSetting(settingData)
    }

    //this method must be run on SyncThread or Coordination don‘t run on Android MainThread
    open fun runStartService(){
        startServiceRunOnThread {
            getMQTTCommandProxyAction().connectionAction()
        }
    }

    protected abstract fun startServiceRunOnThread(stareServiceUnit: ()->Unit )

    protected open fun getMQTTCommandProxyAction(): IMqttCommandProxyAction = proxy

    protected open fun getMqttMessageProxy(): IMqttMessageBaseProxy = messageProxy

    //this method close service and close
    open fun closeService(){
        getMQTTCommandProxyAction().dissConnectionAction()
        destroy()
    }

    abstract fun destroy()

}