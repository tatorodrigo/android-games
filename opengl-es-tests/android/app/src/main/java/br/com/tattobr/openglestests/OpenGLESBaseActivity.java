package br.com.tattobr.openglestests;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tattobr on 06/01/2017.
 */

public abstract class OpenGLESBaseActivity extends Activity implements GLSurfaceView.Renderer {
    protected GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        glSurfaceView = new GLSurfaceView(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            glSurfaceView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        glSurfaceView.setRenderer(this);

        setContentView(glSurfaceView);
    }

    protected void setupGLSurfaceView(GLSurfaceView glSurfaceView) {
        //glSurfaceView.setEGLContextClientVersion(2);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }
}
