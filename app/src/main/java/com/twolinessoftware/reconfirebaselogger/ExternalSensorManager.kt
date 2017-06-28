/*
 * Copyright 2017 2Lines Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.twolinessoftware.reconfirebaselogger

import com.reconinstruments.os.hardware.extsensor.ExternalSensorConnection
import com.reconinstruments.os.hardware.extsensor.ExternalSensorConnectionParams
import com.reconinstruments.os.hardware.extsensor.ExternalSensorListener
import com.reconinstruments.os.hardware.extsensor.HUDExternalSensorManager
import com.reconinstruments.os.metrics.HUDMetricsManager
import timber.log.Timber
import javax.inject.Inject


class ExternalSensorManager(externalSensorManager: HUDExternalSensorManager) : ExternalSensorListener{

    var externalSensorManager: HUDExternalSensorManager


    init{
        this.externalSensorManager = externalSensorManager
    }

    var type : ExternalSensorConnectionParams.ExternalSensorNetworkType = ExternalSensorConnectionParams.ExternalSensorNetworkType.ANT
    var savedConnections : List<ExternalSensorConnectionParams>? = null

    fun connect(){
        externalSensorManager.registerListener(this)

        connectSensors()
    }

    fun disconnect(){
        if(savedConnections != null)
            savedConnections!!
                    .filter { externalSensorManager.isSensorConnected(it.uid) }
                    .forEach { externalSensorManager.disconnectSensor(it.uid) }


    }

    private fun connectSensors(){

        type = externalSensorManager.hudNetworkType
        savedConnections = externalSensorManager.getSavedSensorConnectionParams(type)

        if(savedConnections != null)
            savedConnections!!.forEach { externalSensorManager.connectSensor(it.uid) }


    }

    override fun onSensorDisconnected(p0: ExternalSensorConnection?) {
        Timber.v("Sensor Disconnected ${p0?.sensorConnectionParams?.name}")
    }

    override fun onSensorConnectFailure(p0: ExternalSensorConnection?) {
        Timber.v("Sensor Failure ${p0?.sensorConnectionParams?.name}")
    }

    override fun onSensorConnected(p0: ExternalSensorConnection?) {
        Timber.v("Sensor Connected ${p0?.sensorConnectionParams?.name}")
    }


}