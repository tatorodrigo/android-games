package br.com.tattobr.samples.framework.gl;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import br.com.tattobr.samples.framework.GLGraphics;

public class Vertices {
    private final GLGraphics glGraphics;
    private final boolean hasColor;
    private final boolean hasTexCoords;
    private final int vertexSize;
    private final FloatBuffer vertices;
    private final ShortBuffer indexes;

    public Vertices(GLGraphics glGraphics, int maxVertices, int maxIndexes, boolean hasColor, boolean hasTexCoords) {
        this.glGraphics = glGraphics;
        this.hasColor = hasColor;
        this.hasTexCoords = hasTexCoords;
        this.vertexSize = (2 + (hasColor ? 4 : 0) + (hasTexCoords ? 2 : 0)) * Float.SIZE / 8;

        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(maxVertices * vertexSize);
        byteBuffer.order(ByteOrder.nativeOrder());

        vertices = byteBuffer.asFloatBuffer();

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
        this.vertices.put(vertices, offset, length);
        this.vertices.flip();
    }

    public void setIndexes(short[] indexes, int offset, int length) {
        this.indexes.clear();
        this.indexes.put(indexes, offset, length);
        this.indexes.flip();
    }
}
