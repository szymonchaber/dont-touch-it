package pl.szymonchaber.donttouchit.screenblocking.blockingsignals

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager

class RotationBlockingSignal(context: Context, shouldBlockListener: OnShouldBlockListener) : SensorBlockingSignal(context,
        shouldBlockListener) {

    private val rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)!!

    override fun startBlocking() {
        sensorManager.registerListener(this, rotationVectorSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun stopBlocking() {
        sensorManager.unregisterListener(this, rotationVectorSensor)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val rotationMatrix = FloatArray(16)
        SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values)
        val remappedRotationMatrix = FloatArray(16)
        SensorManager.remapCoordinateSystem(rotationMatrix,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z,
                remappedRotationMatrix)
        // Convert to orientations
        val orientations = FloatArray(3)
        SensorManager.getOrientation(remappedRotationMatrix, orientations)
        for (i in 0..2) {
            orientations[i] = Math.toDegrees(orientations[i].toDouble()).toFloat()
        }

        if (Math.abs(orientations[2]) > 120) {
            shouldBlockListener.blockScreen()
        } else if (Math.abs(orientations[2]) < 60) {
            shouldBlockListener.unblockScreen()
        }
    }
}