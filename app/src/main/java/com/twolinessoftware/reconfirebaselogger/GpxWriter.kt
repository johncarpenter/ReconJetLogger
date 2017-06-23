package com.twolinessoftware.reconfirebaselogger

import android.content.ContentValues.TAG
import com.twolinessoftware.reconfirebaselogger.model.DataPoint
import timber.log.Timber
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by John on 2017-06-22.
 */
class GpxWriter(){

    var activityBuilder : StringBuilder

    init{
        activityBuilder = StringBuilder()
    }

    fun initActivity(){
       // Dump existing?
        activityBuilder = StringBuilder()
        writeHeader("GpxLogger")

    }

    fun addPoint(dataPoint: DataPoint){
        val df = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")

        activityBuilder.append("<trkpt lat=\"$dataPoint.latitude\" lon=\"$dataPoint.longitude\">")
        activityBuilder.append("<time>$df.format(Date(dataPoint.timestamp))</time>")
        activityBuilder.append("<extensions>")

        if(dataPoint.cadence != -1) activityBuilder.append("<cadence>$dataPoint.cadence</cadence>")
        if(dataPoint.hr != -1) activityBuilder.append("<hr>$dataPoint.hr</hr>")

        activityBuilder.append("</extensions></trkpt>")

    }

    fun finalizeActivity(){
        writeFooter()

        Timber.i("$activityBuilder.toString()")
    }

    private fun writeHeader(name:String){
        activityBuilder?.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?><gpx xmlns=\"http://www.topografix.com/GPX/1/1\" creator=\"MapSource 6.15.5\" version=\"1.1\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"  xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\"><trk>\n")
        activityBuilder?.append("<name>$name</name><trkseg>\n")
    }

    private fun writeFooter(){
      activityBuilder?.append("</trkseg></trk></gpx>")
    }


    private fun writePath(file: File) {

        try {
            val writer = FileWriter(file, false)
            writer.append(activityBuilder.toString())
            writer.flush()
            writer.close()
            Timber.i(TAG, "Saved file $file.name")
        } catch (e: IOException) {
            Timber.e( "Error Writting Path", e)
        }

    }

}