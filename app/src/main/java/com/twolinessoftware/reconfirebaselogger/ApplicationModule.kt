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

import android.app.Application
import android.app.NotificationManager
import android.arch.persistence.room.Room
import android.content.Context
import android.location.LocationManager
import com.reconinstruments.os.HUDOS
import com.reconinstruments.os.hardware.extsensor.HUDExternalSensorManager
import com.reconinstruments.os.metrics.HUDMetricsManager
import com.twolinessoftware.reconfirebaselogger.model.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val application: Application) {

    /**
     * Allow the application context to be injected but require that it be annotated with [ ][ForApplicationScope] to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ForApplicationScope
    fun provideApplicationContext(): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideNotificationManager(): NotificationManager {
        return application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    @Provides
    @Singleton
    fun provideLocationManager(): LocationManager {
        return application.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @Provides
    fun provideHudMetricsMananger(): HUDMetricsManager {
        return HUDOS.getHUDService(HUDOS.HUD_METRICS_SERVICE) as HUDMetricsManager
    }

    @Provides
    fun provideHudExternalSensorMananger(): HUDExternalSensorManager {
        return HUDOS.getHUDService(HUDOS.HUD_EXTERNAL_SENSOR_SERVICE) as HUDExternalSensorManager
    }

    @Provides
    @Singleton
    fun provideExternalSensorManager(hudExternalSensorManager: HUDExternalSensorManager): ExternalSensorManager{
        return ExternalSensorManager(hudExternalSensorManager)
    }


    @Provides
    @Singleton
    fun provideDatabase(): Database{
        return Room.databaseBuilder(application,Database::class.java,"datadb").build()
    }

    @Provides
    @Singleton
    fun provideDataFilterManager(database: Database): DataFilterManager{
        return DataFilterManager(database)
    }

}