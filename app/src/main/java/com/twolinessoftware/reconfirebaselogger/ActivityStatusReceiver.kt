package com.twolinessoftware.reconfirebaselogger

/**
 * Created by John on 2017-06-16.
 */
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

            var intentService = Intent(context, LocationTrackingService::class.java)

            if(status == HUDActivityStatus.STATUS_ONGOING && !LocationTrackingService.isRunning){
                context.startService(intentService)
                Timber.i("Starting Tracking Service")
            }else if(LocationTrackingService.isRunning){
                context.stopService(intentService)
                Timber.i("Stopping Tracking Service")
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