package pl.szymonchaber.donttouchthis.screenblocking.blockingsignals

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEventListener
import android.hardware.SensorManager

abstract class SensorBlockingSignal(context: Context, shouldBlockListener: OnShouldBlockListener) : BlockingSignal(
        shouldBlockListener), SensorEventListener {

    protected val sensorManager: SensorManager = context.getSystemService(
            Context.SENSOR_SERVICE) as SensorManager

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        //nop
    }
}