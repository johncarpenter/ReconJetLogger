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

import android.content.ContentValues.TAG
import android.content.Context.MODE_APPEND
import android.os.Environment
import com.twolinessoftware.reconfirebaselogger.model.DataPoint
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.*
import java.text.SimpleDateFormat
import java.util.*



class GpxWriter(){

    val DATA_DIR = "/GPXLogger"
    val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",Locale.US)

    var activityBuilder : StringBuilder

    var storageDirectory : File
    var file : File? = null

    init{

        storageDirectory = checkStorageDirectory()

        activityBuilder = StringBuilder()
    }

    fun initActivity(){
       // Dump existing?
        activityBuilder = StringBuilder()
        writeHeader("GpxLogger")

        file = File(storageDirectory,DATE_FORMAT.format(Date())+".gpx")
    }

    fun addPoint(dataPoint: DataPoint){


        activityBuilder.append("<trkpt lat=\"${dataPoint.latitude}\" lon=\"${dataPoint.longitude}\">")
        activityBuilder.append("<time>${DATE_FORMAT.format(Date(dataPoint.timestamp))}</time>")
        activityBuilder.append("<extensions><gpxtpx:TrackPointExtension>")

        if(dataPoint.cadence != -1) activityBuilder.append("<gpxtpx:cadence>${dataPoint.cadence}</gpxtpx:cadence>")
        if(dataPoint.hr != -1) activityBuilder.append("<gpxtpx:hr>${dataPoint.hr}</gpxtpx:hr>")

        activityBuilder.append("</gpxtpx:TrackPointExtension></extensions></trkpt>")

        if(activityBuilder.length > 1024)
            queueWrites()

    }

    fun finalizeActivity(){
        writeFooter()
        queueWrites()
    }

    private fun checkStorageDirectory(): File {

        val sdCard = Environment.getExternalStorageDirectory()
        val directory = File(sdCard?.absolutePath+DATA_DIR)

        if(!directory.exists()) directory.mkdirs()

        return directory
    }

    fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            return true
        }
        Timber.e("External storage directory not available")
        return false
    }


    private fun writeHeader(name:String){
        activityBuilder.append("<gpx version=\"1.1\" creator=\"Exported from Strava\" xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd http://www.garmin.com/xmlschemas/GpxExtensions/v3 http://www.garmin.com/xmlschemas/GpxExtensionsv3.xsd http://www.garmin.com/xmlschemas/TrackPointExtension/v1 http://www.garmin.com/xmlschemas/TrackPointExtensionv1.xsd\" xmlns=\"http://www.topografix.com/GPX/1/1\" xmlns:gpxtpx=\"http://www.garmin.com/xmlschemas/TrackPointExtension/v1\" xmlns:gpxx=\"http://www.garmin.com/xmlschemas/GpxExtensions/v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">")
        activityBuilder.append("<trk>\n")
        activityBuilder.append("<name>$name</name><trkseg>\n")
    }

    private fun writeFooter(){
      activityBuilder.append("</trkseg></trk></gpx>")
    }


    private fun queueWrites(){
        val data = activityBuilder.toString()
        activityBuilder = StringBuilder()
        Single.fromCallable { writeToDisk(data) }
                .subscribeOn(Schedulers.newThread())
                .subscribe()
    }

    private fun writeToDisk(data: String) {
        Timber.v("Queueing writes to disk")

        try {
            val fOS = FileOutputStream(file, true)

            val writer = OutputStreamWriter(fOS)
            writer.write(data)
            writer.close()

        } catch (e: IOException) {
            Timber.e( "Error Writting Path", e)
        }

    }

}