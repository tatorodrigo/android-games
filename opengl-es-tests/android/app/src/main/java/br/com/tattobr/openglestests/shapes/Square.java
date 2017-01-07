package br.com.tattobr.openglestests.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tattobr on 06/01/2017.
 */

public class Square {
    private static final float coords[] = {
            0.0f, 0.0f,
            1.0f, 0.0f,
            1.0f, 1.0f,
            0.0f, 1.0f
    };
    private static FloatBuffer floatBuffer;

    private static final short indexes[] = {
            0, 1, 2, 2, 3, 0
    };
    private static ShortBuffer shortBuffer;

    public Square() {
        ByteBuffer byteBuffer;
        if (floatBuffer == null) {
            byteBuffer = ByteBuffer.allocateDirect(coords.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());

            floatBuffer = byteBuffer.asFloatBuffer();
            floatBuffer.put(coords);
            floatBuffer.flip();
        }

        if (shortBuffer == null) {
            byteBuffer = ByteBuffer.allocateDirect(indexes.length * 2);
            byteBuffer.order(ByteOrder.nativeOrder());

            shortBuffer = byteBuffer.asShortBuffer();
            shortBuffer.put(indexes);
            shortBuffer.flip();
        }
    }

    public void draw(GL10 gl10) {

    }
}
