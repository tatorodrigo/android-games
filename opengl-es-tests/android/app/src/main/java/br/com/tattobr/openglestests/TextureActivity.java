package br.com.tattobr.openglestests;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.openglestests.utils.FPSUtil;

/**
 * Created by tattobr on 07/01/2017.
 */

public class TextureActivity extends OpenGLESBaseActivity {
    private ShortBuffer shortBuffer;
    private FPSUtil fpsUtil;
    private int textureId;
    private GL10 gl10;
    private float rotation;

    private int generateTexture(GL10 gl10) {
        int[] textureIds = new int[1];
        gl10.glGenTextures(1, textureIds, 0);
        return textureIds[0];
    }

    private Bitmap loadBitmap(Context context, String name) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getAssets().open(name));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        this.gl10 = gl10;

        rotation = 0;
        fpsUtil = new FPSUtil();

        textureId = generateTexture(gl10);
        //Bitmap bitmap = loadBitmap(this, "bob-128x128-rgb888.png");
        Bitmap bitmap = loadBitmap(this, "bob-128x128.png");

        gl10.glEnable(GL10.GL_TEXTURE_2D);

        gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
        GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);

        bitmap.recycle();

        float[] vertex = new float[]{
                0, 0, 0, 1,
                1, 0, 1, 1,
                1, 1, 1, 0,
                0, 1, 0, 0
        };

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertex.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(vertex);
        floatBuffer.flip();

        short[] indexes = new short[]{
                0, 1, 2, 2, 3, 0
        };
        byteBuffer = ByteBuffer.allocateDirect(indexes.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());

        shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(indexes);
        shortBuffer.flip();

        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl10.glVertexPointer(2, GL10.GL_FLOAT, 16, floatBuffer);
        floatBuffer.position(2);
        gl10.glTexCoordPointer(2, GL10.GL_FLOAT, 16, floatBuffer);

        gl10.glMatrixMode(GL10.GL_MODELVIEW);

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        this.gl10 = gl10;
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        rotation += 2;
        if (rotation > 360) {
            rotation = 0;
        }
        gl10.glLoadIdentity();
        gl10.glRotatef(rotation, 0, 1, 0);
        gl10.glTranslatef(-.5f, 0, 0);
        //gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
        gl10.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, shortBuffer);

        fpsUtil.logFPS();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isFinishing()) {
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, 0);
            gl10.glDeleteTextures(1, new int[]{textureId}, 0);
        }
    }
}
