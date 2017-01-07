package br.com.tattobr.openglestests.shapes;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tattobr on 06/01/2017.
 */

public class Triangle {
    private static final float coords[] = {
            0.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            0.5f, 1.0f, 0.0f
    };
    private final int COORDS_PER_VERTEX = 3;
    private final int vertexCount = coords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private static final float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    private static FloatBuffer floatBuffer;
    private static int mProgram = -1;

    public Triangle() {
        if (floatBuffer == null) {
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(coords.length * 4);
            byteBuffer.order(ByteOrder.nativeOrder());

            floatBuffer = byteBuffer.asFloatBuffer();
            floatBuffer.put(coords);
            floatBuffer.flip();
        }
    }

    public void draw(GL10 gl10) {
        gl10.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl10.glVertexPointer(COORDS_PER_VERTEX, GL10.GL_FLOAT, 0, floatBuffer);
        gl10.glDrawArrays(GL10.GL_TRIANGLES, 0, 3);
    }

    public void drawGL20() {
        if (mProgram == -1) {
            compileAndLinkShaders();
        }

        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // get handle to vertex shader's vPosition member
        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, floatBuffer);

        // get handle to fragment shader's vColor member
        int mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    private void compileAndLinkShaders() {
        int vertexShader = ShaderLoader.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = ShaderLoader.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);
        GLES20.glAttachShader(mProgram, fragmentShader);
        GLES20.glLinkProgram(mProgram);
    }
}
