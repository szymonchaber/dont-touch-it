package pl.szymonchaber.donttouchit.screenblocking

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

internal class BlockingManager(context: Context, private val listener: OnShouldBlockListener) : SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val lightSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    var isBlocking = false

    fun startBlocking() {
        isBlocking = true
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stopBlocking() {
        isBlocking = false
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        //nop
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.values[0] < BLOCKING_PROXIMITY) {
            listener.blockScreen()
        } else {
            listener.unblockScreen()
        }
    }

    companion object {

        const val BLOCKING_PROXIMITY = 8
    }
}

interface OnShouldBlockListener {

    fun blockScreen()
    fun unblockScreen()
}