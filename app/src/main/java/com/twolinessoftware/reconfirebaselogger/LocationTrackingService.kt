package com.twolinessoftware.reconfirebaselogger
import android.app.Notification
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.reconinstruments.os.metrics.HUDMetricsID
import com.reconinstruments.os.metrics.HUDMetricsManager
import com.reconinstruments.os.metrics.MetricsValueChangedListener
import timber.log.Timber
import javax.inject.Inject

class LocationTrackingService : Service(), MetricsValueChangedListener, LocationListener {

    @Inject
    lateinit var locationManager: LocationManager

    @Inject
    lateinit var notificationMananger: NotificationManager

    @Inject
    lateinit var metricsManager: HUDMetricsManager

    @Inject
    lateinit var dataFilterManager : DataFilterManager

    override fun onBind(intent: Intent?) = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Timber.i("Starting LocationService")
        return START_STICKY

    }

    override fun onCreate() {
        BaseApplication.graph.inject(this)
        isRunning = true
        createNotification("Recording")

        try {
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, DISTANCE, this)
        } catch (e: SecurityException) {
            Timber.e("Fail to request location update", e)
        } catch (e: IllegalArgumentException) {
            Timber.e("GPS provider does not exist", e)
        }

        metricsManager.registerMetricsListener(this,HUDMetricsID.HEART_RATE)
        metricsManager.registerMetricsListener(this,HUDMetricsID.CADENCE_EXT)
        metricsManager.registerMetricsListener(this,HUDMetricsID.SPEED_CADENCE_CADENCE)

        dataFilterManager.startActivity()

        super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
        createNotification("Stopping Recording")

        dataFilterManager.finishActivity()

        try {
            locationManager?.removeUpdates(this)
        } catch (e: Exception) {
            Timber.e("Unable to remove location listener $e.message")
        }

        metricsManager.unregisterMetricsListener(this,HUDMetricsID.HEART_RATE)
        metricsManager.unregisterMetricsListener(this,HUDMetricsID.CADENCE_EXT)
        metricsManager.unregisterMetricsListener(this,HUDMetricsID.SPEED_CADENCE_CADENCE)

    }

    private fun createNotification(text: String) {
        val notification = Notification.Builder(applicationContext)
                .setContentTitle("Gpx Activity Logger")
                .setSmallIcon(R.drawable.icon_checkmark)
                .setContentText(text)
                .build()
        notificationMananger?.notify(0, notification)
    }

    override fun onMetricsValueChanged(metricID: Int, value: Float, changeTime: Long, isValid: Boolean) {
        dataFilterManager.addMetric(metricID,value,changeTime)
    }

    override fun onLocationChanged(location: Location?) {
        if(location != null) dataFilterManager.addLocation(location)
    }

    override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderEnabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onProviderDisabled(p0: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {

        @JvmStatic var isRunning = false

        val INTERVAL = 1000.toLong() // In milliseconds
        val DISTANCE = 0.toFloat() // In meters

    }

}