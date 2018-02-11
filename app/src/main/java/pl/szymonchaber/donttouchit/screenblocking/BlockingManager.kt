package pl.szymonchaber.donttouchit.screenblocking

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import pl.szymonchaber.donttouchit.screenblocking.OverlayService.BLOCKING_PROXIMITY

internal class BlockingManager(private val overlayService: OverlayService) : SensorEventListener {

    private val sensorManager: SensorManager = overlayService.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    fun startBlocking() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopBlocking() {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //nop
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.values[0] < BLOCKING_PROXIMITY) {
            overlayService.blockScreen()
        } else {
            overlayService.unblockScreen()
        }
    }
}

interface OnShouldBlockListener {

    fun blockScreen()
    fun unblockScreen()
}