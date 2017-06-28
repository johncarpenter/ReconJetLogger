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
