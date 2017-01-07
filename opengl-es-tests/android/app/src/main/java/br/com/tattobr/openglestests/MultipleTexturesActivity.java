package br.com.tattobr.openglestests;

import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.openglestests.utils.FPSUtil;
import br.com.tattobr.openglestests.utils.Texture;

/**
 * Created by tattobr on 07/01/2017.
 */

public class MultipleTexturesActivity extends OpenGLESBaseActivity {
    private final String TAG = MultipleTexturesActivity.class.getSimpleName();

    protected FPSUtil fpsUtil;

    protected GL10 gl10;
    protected Texture bob128;
    protected Texture bob128RGB888;
    protected Texture bob32;
    protected Texture bob32RGB888;

    protected ShortBuffer shortBuffer;

    private void loadTextures(GL10 gl10) {
        if (bob128 == null) {
            bob128 = new Texture(this, gl10, "bob-128x128.png");
        } else {
            bob128.reload(this, gl10);
        }

        if (bob128RGB888 == null) {
            bob128RGB888 = new Texture(this, gl10, "bob-128x128-rgb888.png");
        } else {
            bob128RGB888.reload(this, gl10);
        }

        if (bob32 == null) {
            bob32 = new Texture(this, gl10, "bob-32x32.png");
        } else {
            bob32.reload(this, gl10);
        }

        if (bob32RGB888 == null) {
            bob32RGB888 = new Texture(this, gl10, "bob-32x32-rgb888.png");
        } else {
            bob32RGB888.reload(this, gl10);
        }
    }

    private void generateAndBindCoords(GL10 gl10) {
        float[] vertex = new float[]{
                0, 0, 0, 1,
                1, 0, 1, 1,
                1, 1, 1, 0,
                0, 1, 0, 0
        };
        int floatByteSize = Float.SIZE / 8;
        int shortByteSize = Short.SIZE / 8;
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertex.length * floatByteSize);
        byteBuffer.order(ByteOrder.nativeOrder());

        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(vertex);
        floatBuffer.flip();

        short[] indexes = new short[]{
                0, 1, 2, 2, 3, 0
        };
        byteBuffer = ByteBuffer.allocateDirect(indexes.length * shortByteSize);
        byteBuffer.order(ByteOrder.nativeOrder());

        shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(indexes);
        shortBuffer.flip();

        gl10.glVertexPointer(2, GL10.GL_FLOAT, 4 * floatByteSize, floatBuffer);
        floatBuffer.position(2);
        gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 4 * floatByteSize, floatBuffer);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        Log.d(TAG, "onSurfaceCreated");

        this.gl10 = gl10;

        if (fpsUtil == null) {
            fpsUtil = new FPSUtil();
        } else {
            fpsUtil.reset();
        }

        loadTextures(gl10);
        generateAndBindCoords(gl10);

        gl10.glEnable(GL10.GL_TEXTURE_2D);
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d(TAG, "onSurfaceCreated");

        this.gl10 = gl10;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        drawTexture(gl10, bob128, .4f, -1f, 0f);
        drawTexture(gl10, bob128RGB888, .4f, -.5f, 0f);
        drawTexture(gl10, bob32, .4f, 0f, 0f);
        drawTexture(gl10, bob32RGB888, .4f, .5f, 0f);
        fpsUtil.logFPS();
    }

    protected void drawTexture(GL10 gl10, Texture texture, float scale, float x, float y) {
        gl10.glLoadIdentity();
        gl10.glTranslatef(x, y, 0);
        gl10.glScalef(scale, scale, 0);
        texture.bind(gl10);
        gl10.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, shortBuffer);
        texture.unbind(gl10);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing()) {
            bob128.dispose(gl10);
            bob128RGB888.dispose(gl10);
            bob32.dispose(gl10);
            bob32RGB888.dispose(gl10);

            bob128 = null;
            bob128RGB888 = null;
            bob32 = null;
            bob32RGB888 = null;
        }
    }
}
