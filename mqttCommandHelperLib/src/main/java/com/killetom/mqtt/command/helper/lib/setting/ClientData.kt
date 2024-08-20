package com.killetom.mqtt.command.helper.lib.setting

open class ClientData(
    val clientId: String,
    val servicerUrl: String,
    val userName: String,
    val password: String,
    val clearCacheStatus: Boolean = true,
    val autoReteryConnectStatus: Boolean = true
) {


}