package io.github.yass97.lib

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.getSystemService
import kotlin.math.sqrt

internal class ShakeDetector(
    private val context: Context,
    private val onShake: () -> Unit
): SensorEventListener {
    private var manager: SensorManager? = null
    private var accelerometer: Sensor? = null

    private var lastShakeTime = 0L

    fun start() {
        manager = context.getSystemService<SensorManager>()
        accelerometer = manager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        accelerometer?.also {
            manager?.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]

        val acceleration = sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH

        val now = System.currentTimeMillis()
        if (acceleration > 12 && now - lastShakeTime > 1000) {
            lastShakeTime = now
            onShake()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit
}