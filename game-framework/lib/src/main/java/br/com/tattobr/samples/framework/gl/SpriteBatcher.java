package br.com.tattobr.samples.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.GLGraphics;
import br.com.tattobr.samples.framework.math.Vector2;

public class SpriteBatcher {
    private final float[] verticesBuffer;
    private int bufferIndex;
    private final Vertices vertices;
    private int numSprites;

    public SpriteBatcher(GLGraphics graphics, int maxSprites) {
        verticesBuffer = new float[maxSprites * 4 * 4];
        vertices = new Vertices(graphics, maxSprites * 4, maxSprites * 6, false, true);
        bufferIndex = 0;
        numSprites = 0;
        int length = maxSprites * 6;
        short[] indices = new short[length];
        for (int i = 0, j = 0; i < length; i += 6, j += 4) {
            indices[i] = (short) j;
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 2);
            indices[i + 3] = (short) (j + 2);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) j;
        }
        vertices.setIndexes(indices, 0, length);
    }

    public void beginBatch(Texture texture) {
        texture.bind();
        bufferIndex = 0;
        numSprites = 0;
    }

    public void endBatch() {
        vertices.setVertices(verticesBuffer, 0, bufferIndex);
        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
        vertices.unbind();
    }

    public void drawSprite(float x, float y, float width, float height, TextureRegion region) {
        final float halfWidth = width / 2;
        final float halfHeight = height / 2;
        final float x1 = x - halfWidth;
        final float y1 = y - halfHeight;
        final float x2 = x + halfWidth;
        final float y2 = y + halfHeight;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }

    public void drawSprite(float x, float y, float width, float height, float angle, TextureRegion region) {
        final float halfWidth = width / 2;
        final float halfHeight = height / 2;

        final float radians = angle * Vector2.TO_RADIANS;
        final float cos = (float) Math.cos(radians);
        final float sin = (float) Math.sin(radians);

        float x1 = -halfWidth * cos - (-halfHeight) * sin;
        float y1 = -halfWidth * sin + (-halfHeight) * cos;
        float x2 = halfWidth * cos - (-halfHeight) * sin;
        float y2 = halfWidth * sin + (-halfHeight) * cos;
        float x3 = halfWidth * cos - halfHeight * sin;
        float y3 = halfWidth * sin + halfHeight * cos;
        float x4 = -halfWidth * cos - halfHeight * sin;
        float y4 = -halfWidth * sin + halfHeight * cos;

        x1 += x;
        y1 += y;
        x2 += x;
        y2 += y;
        x3 += x;
        y3 += y;
        x4 += x;
        y4 += y;

        verticesBuffer[bufferIndex++] = x1;
        verticesBuffer[bufferIndex++] = y1;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x2;
        verticesBuffer[bufferIndex++] = y2;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v2;

        verticesBuffer[bufferIndex++] = x3;
        verticesBuffer[bufferIndex++] = y3;
        verticesBuffer[bufferIndex++] = region.u2;
        verticesBuffer[bufferIndex++] = region.v1;

        verticesBuffer[bufferIndex++] = x4;
        verticesBuffer[bufferIndex++] = y4;
        verticesBuffer[bufferIndex++] = region.u1;
        verticesBuffer[bufferIndex++] = region.v1;

        numSprites++;
    }
}
