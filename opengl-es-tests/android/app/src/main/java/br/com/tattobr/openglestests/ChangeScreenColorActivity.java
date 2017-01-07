package br.com.tattobr.openglestests;

import android.os.Bundle;
import android.util.Log;

import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.openglestests.utils.FPSUtil;

/**
 * Created by tattobr on 06/01/2017.
 */

public class ChangeScreenColorActivity extends OpenGLESBaseActivity {
    private Random random;
    private FPSUtil fpsUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        random = new Random();
        fpsUtil = new FPSUtil();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d(this.getClass().getSimpleName(), gl10.glGetString(GL10.GL_EXTENSIONS));
        Log.d(this.getClass().getSimpleName(), gl10.glGetString(GL10.GL_VERSION));
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        gl10.glClearColor(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        //GLES20.glClearColor(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
        //GLES20.glClear(GL10.GL_COLOR_BUFFER_BIT);
        fpsUtil.logFPS();
    }
}
