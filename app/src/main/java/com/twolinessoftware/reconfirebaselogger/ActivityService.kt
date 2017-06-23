package com.twolinessoftware.reconfirebaselogger

import android.app.IntentService
import android.app.Notification
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

import com.reconinstruments.app.activity.HUDActivityStatus

class ActivityService : IntentService("ActivityService") {
    private var mReceiver: BroadcastReceiver? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mReceiver = ActivityStatusReceiver()

        registerReceiver(mReceiver, IntentFilter(HUDActivityStatus.ACTIVITY_INTENT_FILTER))

        ActivityService.isRunning = true

        // We want this service to continue running until it is explicitly stopped, so return sticky.
        return START_STICKY
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        ActivityService.isRunning = false
        super.onDestroy()
    }

    override fun onHandleIntent(intent: Intent?) {}

    companion object {
        @JvmStatic var isRunning = false
    }
}
