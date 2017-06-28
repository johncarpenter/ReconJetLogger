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


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.reconinstruments.app.activity.HUDActivityStatus
import timber.log.Timber

class ActivityStatusReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent?) {
            val type = intent?.getIntExtra(HUDActivityStatus.ACTIVITY_TYPE, HUDActivityStatus.TYPE_UNKNOWN)
            val status = intent?.getIntExtra(HUDActivityStatus.ACTIVITY_STATUS, HUDActivityStatus.STATUS_UNKNOWN)

            Timber.i("Received Status "+getStatusString(status)+ " Type:"+getTypeString(type))

            val intentService = Intent(context, LocationTrackingService::class.java)

            if(status == HUDActivityStatus.STATUS_ONGOING && !LocationTrackingService.isRunning){
                context.startService(intentService)

                LocationTrackingService.Status = LocationTrackingService.Status.RUNNING
                Timber.i("Starting Tracking Service")
            }else if(status == HUDActivityStatus.STATUS_PAUSED && LocationTrackingService.isRunning){
               // context.stopService(intentService)
                LocationTrackingService.Status = LocationTrackingService.Status.PAUSED
                Timber.i("Pausing Tracking Service")
            }else if(status == HUDActivityStatus.STATUS_NOACTIVITY && LocationTrackingService.isRunning){
                context.stopService(intentService)
                LocationTrackingService.Status = LocationTrackingService.Status.STOPPED
                Timber.i("Stopping Tracking Service(end)")
            }
        }

        private fun getStatusString(status: Int?): String {
            when (status) {
                HUDActivityStatus.STATUS_NOACTIVITY -> return "NO-ACTIVITY"
                HUDActivityStatus.STATUS_ONGOING -> return "ON-GOING"
                HUDActivityStatus.STATUS_PAUSED -> return "PAUSED"
                else -> return "UNKNOWN"
            }
        }

        private fun getTypeString(type: Int?): String {
            when (type) {
                HUDActivityStatus.TYPE_CYCLING -> return "CYCLING"
                HUDActivityStatus.TYPE_RUNNING -> return "RUNNING"
                HUDActivityStatus.TYPE_SKI -> return "SKIING"
                else -> return "UNKNOWN"
            }
        }



}