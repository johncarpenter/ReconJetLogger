package com.twolinessoftware.reconfirebaselogger

import android.app.Application
import android.location.LocationManager
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by John on 2017-06-16.
 */
class BaseApplication : Application() {

    companion object {
        //platformStatic allow access it from java code
        @JvmStatic lateinit var graph: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        graph = DaggerApplicationComponent.builder().androidModule(AndroidModule(this)).build()
        graph.inject(this)

        initLogging()

    }

    private fun initLogging() {
        Timber.plant(Timber.DebugTree())
    }
}