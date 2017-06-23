package com.twolinessoftware.reconfirebaselogger

/**
 * Created by John on 2017-06-16.
 */
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AndroidModule::class))
interface ApplicationComponent {
    fun inject(application: BaseApplication)

    fun inject(service: LocationTrackingService)



}