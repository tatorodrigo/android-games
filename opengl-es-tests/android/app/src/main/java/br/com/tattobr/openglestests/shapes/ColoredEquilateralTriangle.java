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
    private float r, g, b, a;
    private float aspectRatio;

    public ColoredEquilateralTriangle(float base, float height, int color) {
        vertex = new float[18];
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(vertex.length * Float.SIZE / 8);
        byteBuffer.order(ByteOrder.nativeOrder());

        floatBuffer = byteBuffer.asFloatBuffer();

        set(base, height, 1f, color);
    }

    private void set(float base, float height, float aspectRatio, int color) {
        float a = (float) ((color >> 24) & 0xFF) / 255;
        float r = (float) ((color >> 16) & 0xFF) / 255;
        float g = (float) ((color >> 8) & 0xFF) / 255;
        float b = (float) (color & 0xFF) / 255;

        set(base, height, aspectRatio, r, g, b, a);
    }

    private void set(float base, float height, float aspectRatio, float r, float g, float b, float a) {
        this.base = base;
        this.height = height;
        this.aspectRatio = aspectRatio;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;

        vertex[0] = 0;
        vertex[1] = 0;
        vertex[2] = r;
        vertex[3] = g;
        vertex[4] = b;
        vertex[5] = a;
        vertex[6] = aspectRatio * base;
        vertex[7] = 0;
        vertex[8] = r;
        vertex[9] = g;
        vertex[10] = b;
        vertex[11] = a;
        vertex[12] = aspectRatio * base / 2;
        vertex[13] = aspectRatio * height;
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

        set(base, height, aspectRatio, r, g, b, a);
    }

    public void setSize(float base, float height) {
        set(base, height, aspectRatio, r, g, b, a);
    }

    public void setAspectRatio(float aspectRatio) {
        set(base, height, aspectRatio, r, g, b, a);
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
