package br.com.tattobr.samples.framework.gl;

public class TextureRegion {
    public final float u1;
    public final float v1;
    public final float u2;
    public final float v2;
    public final Texture texture;

    public TextureRegion(Texture texture, float x, float y, float width, float height) {
        u1 = x / texture.getWidth();
        v1 = y / texture.getHeight();
        u2 = u1 + width / texture.getWidth();
        v2 = v1 + height / texture.getHeight();
        this.texture = texture;
    }
}
