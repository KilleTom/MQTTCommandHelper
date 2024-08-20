# MQTTCommandHelper
this lib make Mqtt can be easy use
get lib version by use this web link： [![](https://jitpack.io/v/KilleTom/MQTTCommandHelper.svg)](https://jitpack.io/#KilleTom/MQTTCommandHelper)

## How to use

### dependency
make sure this libary can be dependency by used this link to [![](https://jitpack.io/v/KilleTom/MQTTCommandHelper.svg)](https://jitpack.io/#KilleTom/MQTTCommandHelper)

### init MqttService

make your service class must be extend MqttBaseService like this simple


```kotlin

class SimplerMqttService : MqttBaseService {

}

```

#### important function and object

1. your must creat ClientData by Service init

   ```kotlin
   class ExampleUnitTest {
   

    @Test
    fun sss(){
        try{
            //look like this build 的 example ClientData object to make SimplerMqttService can be initService
            SimplerMqttService.instance.initService(ClientData("8083", "tcp://broker-cn.emqx.io:1883", "xxx", "xxx"))
            SimplerMqttService.instance.runStartService()

        }catch (t:Throwable){
            t.printStackTrace()
        }

        while (true){

        }
     }
    }
  
   ```
   
2. MqttBaseService how to startConnection on thread
   
   ```kotlin
   class SimplerMqttService : MqttBaseService {
   
    // MQTT connecting run on thread by this funtion，if you don't want run other thread just like this example
   // ps: MQTT connection may be block this current thread so don't run on ui thread
    @override fun startServiceRunOnThread(stareServiceUnit: () -> Unit) {
      
        stareServiceUnit.invoke()
      
       }
    }
  
   ```
3. IMqttMessageBaseProxy how to use and init
   - IMqttMessageBaseProxy this proxy use to send message or recive MqttService
   - IMqttMessageBaseProxy this proxy use be subTopic or unsubTopic
   - Init and how to used by example SimpleMessageProxy object
  
     
     
