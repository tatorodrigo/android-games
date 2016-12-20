package br.com.tattobr.samples.mrnom.framework.impl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.util.List;

public class AndroidAccelerometerHandler implements SensorEventListener {
    private float accelX;
    private float accelY;
    private float accelZ;

    public AndroidAccelerometerHandler(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        List<Sensor> accelerometerList = sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerList.size() > 0) {
            sensorManager.registerListener(this, accelerometerList.get(0), SensorManager.SENSOR_DELAY_GAME);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        accelX = sensorEvent.values[0];
        accelY = sensorEvent.values[1];
        accelZ = sensorEvent.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    public float getAccelX() {
        return accelX;
    }

    public float getAccelY() {
        return accelY;
    }

    public float getAccelZ() {
        return accelZ;
    }
}
