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

import android.location.Location
import com.reconinstruments.os.metrics.HUDMetricsID
import com.twolinessoftware.reconfirebaselogger.model.DataPoint
import com.twolinessoftware.reconfirebaselogger.model.Database
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber


class DataFilterManager(internal var database: Database){

    var cadence:Int? = null
    var hr:Int? = null
    var speed:Float? = null
    var location:Location? = null
    var gpxWriter : GpxWriter

    init{
        gpxWriter = GpxWriter()
    }



    fun addMetric(metricId:Int, value: Float, time: Long){
        when(metricId){
            HUDMetricsID.CADENCE_EXT, HUDMetricsID.SPEED_CADENCE_CADENCE -> cadence = value.toInt()
            HUDMetricsID.HEART_RATE -> hr = value.toInt()
            HUDMetricsID.SPEED_CADENCE_SPEED, HUDMetricsID.SPEED_3D -> speed = value
        }
    }

    fun addLocation(newLocation:Location){
        location = newLocation
        writeDataPoint()
    }

    fun startActivity(){
        gpxWriter.initActivity()
    }

    fun finishActivity(){
        gpxWriter.finalizeActivity()
    }


    private fun addDataPoint(): DataPoint {
        val datapoint = DataPoint()
        datapoint.cadence = if(cadence != null) cadence!! else -1
        datapoint.hr = if(hr != null) hr!! else -1
        datapoint.latitude = location?.latitude!!
        datapoint.longitude = location?.longitude!!
        datapoint.timestamp = System.currentTimeMillis()

        Single.fromCallable {
            database.dataPointDao().insert(datapoint)
        }.subscribeOn(Schedulers.newThread()).subscribe()

        return datapoint
    }

    private fun writeDataPoint(){

        val datapoint = addDataPoint()

        gpxWriter.addPoint(datapoint)

        clearDataPoint()

    }

    private fun clearDataPoint(){
        cadence = null
        hr = null
        location = null
    }
}
