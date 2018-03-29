package pl.szymonchaber.donttouchthis.screenblocking.blockingsignals

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager

class ProximityBlockingSignal(context: Context, shouldBlockListener: OnShouldBlockListener) : SensorBlockingSignal(context,
        shouldBlockListener) {

    private val proximitySensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

    override fun startBlocking() {
        sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun stopBlocking() {
        sensorManager.unregisterListener(this, proximitySensor)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.values[0] < BLOCKING_PROXIMITY) {
            shouldBlockListener.blockScreen()
        } else {
            shouldBlockListener.unblockScreen()
        }
    }

    companion object {

        const val BLOCKING_PROXIMITY = 8
    }
}