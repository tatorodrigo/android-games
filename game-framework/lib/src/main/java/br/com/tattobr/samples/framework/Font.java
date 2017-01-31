package br.com.tattobr.samples.framework;

import br.com.tattobr.samples.framework.gl.SpriteBatcher;

public interface Font {
    void drawText(SpriteBatcher batcher, String text, float x, float y);

    void drawText(SpriteBatcher batcher, String text, float x, float y, float scaleX, float scaleY);
}
