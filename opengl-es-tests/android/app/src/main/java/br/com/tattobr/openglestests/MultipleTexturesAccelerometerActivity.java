package br.com.tattobr.openglestests;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Surface;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.openglestests.utils.Texture;

/**
 * Created by tattobr on 07/01/2017.
 */

public class MultipleTexturesAccelerometerActivity extends MultipleTexturesActivity implements SensorEventListener {
    private final String TAG = MultipleTexturesAccelerometerActivity.class.getSimpleName();
    private SensorManager sensorManager;
    private Sensor accelerometer;

    private float mSensorX;
    private float mSensorY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final Display display = getWindowManager().getDefaultDisplay();
        switch (display.getRotation()) {
            case Surface.ROTATION_0:
                mSensorX = sensorEvent.values[0];
                mSensorY = sensorEvent.values[1];
                break;
            case Surface.ROTATION_90:
                mSensorX = -sensorEvent.values[1];
                mSensorY = sensorEvent.values[0];
                break;
            case Surface.ROTATION_180:
                mSensorX = -sensorEvent.values[0];
                mSensorY = -sensorEvent.values[1];
                break;
            case Surface.ROTATION_270:
                mSensorX = sensorEvent.values[1];
                mSensorY = -sensorEvent.values[0];
        }
        Log.d(TAG, "onSensorChanged: " + mSensorX + ", " + mSensorY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    protected void drawTexture(GL10 gl10, Texture texture, float scale, float x, float y) {
        final float rotationX = 180 * mSensorX / 10;
        float translationScaled = .5f * scale;
        gl10.glLoadIdentity();
        gl10.glTranslatef(x + translationScaled, y, 0);
        gl10.glRotatef(rotationX, 0, 1, 0);
        gl10.glTranslatef(-translationScaled, 0, 0);
        gl10.glScalef(scale, scale, 0);
        texture.bind(gl10);
        gl10.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, shortBuffer);
        texture.unbind(gl10);
    }
}
