package br.com.tattobr.samples.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.GLGraphics;

public class Vertices {
    private final GLGraphics glGraphics;
    private final boolean hasColor;
    private final boolean hasTexCoords;
    private final int vertexSize;
    private final IntBuffer vertices;
    private final ShortBuffer indexes;
    private final int []tmpBuffer;

    public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndexes, boolean hasColor, boolean hasTexCoords) {
        this.glGraphics = glGraphics;
        this.hasColor = hasColor;
        this.hasTexCoords = hasTexCoords;
        this.vertexSize = (2 + (hasColor ? 4 : 0) + (hasTexCoords ? 2 : 0)) * Float.SIZE / 8;
        tmpBuffer = new int[maxVertices * vertexSize / 4];

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertices = byteBuffer.asIntBuffer();

        if (maxIndexes > 0) {
            byteBuffer = ByteBuffer.allocateDirect(maxIndexes * Short.SIZE / 8);
            byteBuffer.order(ByteOrder.nativeOrder());
            indexes = byteBuffer.asShortBuffer();
        } else {
            indexes = null;
        }
    }

    public void setVertices(float[] vertices, int offset, int length) {
        this.vertices.clear();
        int size = offset + length;
        for(int i = 0; i < size; i++) {
            tmpBuffer[i] = Float.floatToRawIntBits(vertices[i]);
        }
        this.vertices.put(tmpBuffer, offset, length);
        this.vertices.flip();
    }

    public void setIndexes(short[] indexes, int offset, int length) {
        this.indexes.clear();
        this.indexes.put(indexes, offset, length);
        this.indexes.flip();
    }

    public void bind() {
        GL10 gl = glGraphics.getGl();
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        vertices.position(0);
        gl.glVertexPointer(2, GL10.GL_FLOAT, vertexSize, vertices);

        if (hasColor) {
            gl.glEnableClientState(GL10.GL_COLOR_ARRAY);
            vertices.position(2);
            gl.glColorPointer(4, GL10.GL_FLOAT, vertexSize, vertices);
        }

        if (hasTexCoords) {
            gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
            vertices.position(hasColor ? 6 : 2);
            gl.glTexCoordPointer(2, GL10.GL_FLOAT, vertexSize, vertices);
        }
    }

    public void draw(int primitiveType, int offset, int numVertices) {
        GL10 gl = glGraphics.getGl();

        if (indexes != null) {
            indexes.position(offset);
            gl.glDrawElements(primitiveType, numVertices, GL10.GL_UNSIGNED_SHORT, indexes);
        } else {
            gl.glDrawArrays(primitiveType, offset, numVertices);
        }
    }

    public void unbind() {
        GL10 gl = glGraphics.getGl();

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);

        if (hasColor) {
            gl.glDisableClientState(GL10.GL_COLOR_ARRAY);
        }

        if (hasTexCoords) {
            gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        }
    }
}
