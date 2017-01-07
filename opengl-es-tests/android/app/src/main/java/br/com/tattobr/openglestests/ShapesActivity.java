package br.com.tattobr.openglestests;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.openglestests.shapes.Square;
import br.com.tattobr.openglestests.shapes.Triangle;

/**
 * Created by tattobr on 06/01/2017.
 */

public class ShapesActivity extends OpenGLESBaseActivity {
    private Triangle triangle;
    private Square square;

    @Override
    protected void setupGLSurfaceView(GLSurfaceView glSurfaceView) {
        //glSurfaceView.setEGLContextClientVersion(2);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        triangle = new Triangle();
        square = new Square();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        //triangle.drawGL20();

        triangle.draw(gl10);
        //square.draw(gl10);
    }
}
