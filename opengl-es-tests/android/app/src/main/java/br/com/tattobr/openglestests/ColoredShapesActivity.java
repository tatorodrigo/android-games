package br.com.tattobr.openglestests;

import android.graphics.Color;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.openglestests.model.MoveColoredBody;
import br.com.tattobr.openglestests.shapes.ColoredEquilateralTriangle;
import br.com.tattobr.openglestests.utils.FPSUtil;

public class ColoredShapesActivity extends OpenGLESBaseActivity {
    private final String TAG = ColoredShapesActivity.class.getSimpleName();
    private final int MAX_TRIANGLES = 100;
    private final float TRIANGLE_SIZE = 20;

    private ColoredEquilateralTriangle triangle;
    private MoveColoredBody[] bodies;

    private FPSUtil fpsUtil;
    private long startTime;

    private boolean coloredTrianglesEnabled;

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d(TAG, "onSurfaceCreated");

        coloredTrianglesEnabled = true;

        bodies = new MoveColoredBody[MAX_TRIANGLES];
        for (int i = 0; i < MAX_TRIANGLES; i++) {
            bodies[i] = new MoveColoredBody(480, 320, TRIANGLE_SIZE, TRIANGLE_SIZE);
        }
        fpsUtil = new FPSUtil();
        triangle = new ColoredEquilateralTriangle(TRIANGLE_SIZE, TRIANGLE_SIZE, Color.RED);
        startTime = System.nanoTime();

        gl10.glClearColor(0, 0, 0, 1);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_COLOR_ARRAY);

        if (!coloredTrianglesEnabled) {
            triangle.setColor(0x99FFFFFF);
            triangle.bindColorAndPoint(gl10);
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d(TAG, "onSurfaceChanged " + width + ", " + height);

        gl10.glViewport(0, 0, width, height);
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glOrthof(0, 480, 0, 320, 1, -1);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        long now = System.nanoTime();
        float deltaTime = (float) (now - startTime) / 1000000000;
        for (int i = 0; i < MAX_TRIANGLES; i++) {
            bodies[i].update(deltaTime);
        }
        startTime = System.nanoTime();

        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl10.glMatrixMode(GL10.GL_MODELVIEW);

        MoveColoredBody moveColoredBody;
        for (int i = 0; i < MAX_TRIANGLES; i++) {
            moveColoredBody = bodies[i];

            if (coloredTrianglesEnabled) {
                triangle.setColor(moveColoredBody.getColor());
                triangle.bindColorAndPoint(gl10);
            }

            gl10.glLoadIdentity();
            gl10.glTranslatef(moveColoredBody.getX(), moveColoredBody.getY(), 0);

            triangle.draw(gl10);
        }
        fpsUtil.logFPS();
    }
}
