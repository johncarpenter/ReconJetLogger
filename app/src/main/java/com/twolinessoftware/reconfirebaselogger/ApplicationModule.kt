package com.twolinessoftware.reconfirebaselogger

import android.app.Application
import android.app.NotificationManager
import android.arch.persistence.room.Room
import android.content.Context
import android.location.LocationManager
import com.reconinstruments.os.HUDOS
import com.reconinstruments.os.metrics.HUDMetricsManager
import com.twolinessoftware.reconfirebaselogger.model.Database
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AndroidModule(private val application: Application) {

    /**
     * Allow the application context to be injected but require that it be annotated with [ ][ForApplication] to explicitly differentiate it from an activity context.
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
    @Singleton
    fun provideHudMetricsMananger(): HUDMetricsManager {
        return HUDOS.getHUDService(HUDOS.HUD_METRICS_SERVICE) as HUDMetricsManager
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