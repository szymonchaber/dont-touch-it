package pl.szymonchaber.donttouchit.screenblocking.blockingsignals

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

class LightBlockingSignal(context: Context, shouldBlockListener: OnShouldBlockListener) : BlockingSignal(
        shouldBlockListener), SensorEventListener {

    private val sensorManager: SensorManager = context.getSystemService(
            Context.SENSOR_SERVICE) as SensorManager

    private val lightSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    override fun startBlocking() {
        sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun stopBlocking() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.values[0] < BLOCKING_PROXIMITY) {
            shouldBlockListener.blockScreen()
        } else {
            shouldBlockListener.unblockScreen()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        //nop
    }

    companion object {

        const val BLOCKING_PROXIMITY = 8
    }
}