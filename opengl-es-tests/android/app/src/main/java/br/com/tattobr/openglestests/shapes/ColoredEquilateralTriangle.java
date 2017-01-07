package br.com.tattobr.openglestests.shapes;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tattobr on 06/01/2017.
 */

public class ColoredEquilateralTriangle {
    private FloatBuffer floatBuffer;
    private float base;
    private float height;
    private float[] vertex;

    public ColoredEquilateralTriangle(float base, float height, int color) {
        vertex = new float[18];
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertex.length * Float.SIZE / 8);
        byteBuffer.order(ByteOrder.nativeOrder());

        floatBuffer = byteBuffer.asFloatBuffer();
        this.base = base;
        this.height = height;


        setColor(color);
    }

    public void setColor(float r, float g, float b, float a) {
        vertex[0] = 0;
        vertex[1] = 0;
        vertex[2] = r;
        vertex[3] = g;
        vertex[4] = b;
        vertex[5] = a;
        vertex[6] = base;
        vertex[7] = 0;
        vertex[8] = r;
        vertex[9] = g;
        vertex[10] = b;
        vertex[11] = a;
        vertex[12] = base / 2;
        vertex[13] = height;
        vertex[14] = r;
        vertex[15] = g;
        vertex[16] = b;
        vertex[17] = a;

        floatBuffer.position(0);
        floatBuffer.put(vertex);
        floatBuffer.flip();
    }

    public void setColor(int color) {
        float a = (float) ((color >> 24) & 0xFF) / 255;
        float r = (float) ((color >> 16) & 0xFF) / 255;
        float g = (float) ((color >> 8) & 0xFF) / 255;
        float b = (float) (color & 0xFF) / 255;

        setColor(r, g, b, a);
    }

    public void bindColorAndPoint(GL10 gl10) {
        floatBuffer.position(0);
        gl10.glVertexPointer(2, GL10.GL_FLOAT, 24, floatBuffer);
        floatBuffer.position(2);
        gl10.glColorPointer(4, GL10.GL_FLOAT, 24, floatBuffer);
    }

    public void draw(GL10 gl10) {
        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
    }
}
