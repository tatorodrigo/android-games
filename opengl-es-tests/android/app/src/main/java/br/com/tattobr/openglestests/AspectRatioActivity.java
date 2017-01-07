package br.com.tattobr.openglestests;

import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.openglestests.shapes.ColoredEquilateralTriangle;
import br.com.tattobr.openglestests.utils.FPSUtil;

/**
 * Created by tattobr on 07/01/2017.
 */

public class AspectRatioActivity extends OpenGLESBaseActivity {
    private final String TAG = AspectRatioActivity.class.getSimpleName();
    private ColoredEquilateralTriangle triangle;

    private float aspectRatio;
    private float worldWidth;
    private float worldHeight;

    private FPSUtil fpsUtil;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        if (triangle == null) {
            triangle = new ColoredEquilateralTriangle(100, 100, 0xFFC6FF00);
        }

        if (fpsUtil == null) {
            fpsUtil = new FPSUtil();
        } else {
            fpsUtil.reset();
        }

        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        aspectRatio = (float) width / height;

        final float desiredWorldWidth = 480;
        final float desiredWorldHeight = 320;
        final float sourceAspectRatio = desiredWorldWidth / desiredWorldHeight;

        final float triangleAspectRatio = aspectRatio / sourceAspectRatio;

        if (aspectRatio > 1) {
            worldWidth = desiredWorldWidth * aspectRatio;
            worldHeight = desiredWorldHeight;
        } else {
            worldWidth = desiredWorldWidth;
            worldHeight = desiredWorldHeight / aspectRatio;
        }

        triangle.setAspectRatio(triangleAspectRatio);
        triangle.bindColorAndPoint(gl10);

        gl10.glViewport(0, 0, width, height);
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glOrthof(0, worldWidth, 0, worldHeight, 1, -1);

        gl10.glMatrixMode(GL10.GL_MODELVIEW);

        Log.d(TAG, "World: " + worldWidth + ", " + worldHeight);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //gl10.glTranslatef(worldWidth - 10, 0, 0);
        triangle.draw(gl10);
        fpsUtil.logFPS();
    }
}
