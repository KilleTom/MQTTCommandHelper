package com.killetom.mqtt.command.helper

import com.killetom.mqtt.command.helper.lib.setting.ClientData
import org.junit.Assert.*
import org.junit.Test
import java.net.URI

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val vURI = URI("tcp://test.mosquitto.org:8083")
        println("vURI.scheme:${vURI.scheme}")
    }

    @Test
    fun sss(){
        try{

            SimplerMqttService.instance.initService(ClientData("8083", "tcp://broker-cn.emqx.io:1883", "xxx", "xxx"))
            SimplerMqttService.instance.runStartService()

        }catch (t:Throwable){
            t.printStackTrace()
        }

        while (true){

        }
    }
}