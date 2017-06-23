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
        var datapoint = DataPoint()
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

        var datapoint = addDataPoint()

        gpxWriter.addPoint(datapoint)

        clearDataPoint()

    }

    private fun clearDataPoint(){
        cadence = null
        hr = null
        location = null
    }
}
