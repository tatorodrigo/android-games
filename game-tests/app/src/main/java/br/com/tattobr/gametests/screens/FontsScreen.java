package br.com.tattobr.gametests.screens;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.Font;
import br.com.tattobr.samples.framework.GLScreen;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.gl.Camera2D;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.samples.framework.gl.Texture;
import br.com.tattobr.samples.framework.impl.BMFont;

public class FontsScreen extends GLScreen {
    private Texture fontTexture;
    private Font font;
    private Camera2D camera2D;
    private String text;
    private SpriteBatcher spriteBatcher;
    private float textScaleX;
    private float textScaleY;

    public FontsScreen(Game game) {
        super(game);

        spriteBatcher = new SpriteBatcher(graphics, 100);
        camera2D = new Camera2D(graphics, 3.2f, 4.8f);
        textScaleX = 3.2f / graphics.getWidth();
        textScaleY = 4.8f / graphics.getHeight();
        text = "Hello World!";
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {
        GL10 gl10 = graphics.getGl();

        gl10.glClearColor(0, 0, 1, 1);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera2D.setViewportAndMatrices();

        gl10.glEnable(GL10.GL_TEXTURE_2D);
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        spriteBatcher.beginBatch(fontTexture);
        font.drawText(spriteBatcher, text, 1, 1, textScaleX, textScaleY);
        spriteBatcher.endBatch();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        fontTexture = new Texture(glGame, "8bit.png");
        font = new BMFont(game, "8bit.fnt", fontTexture, 0, 0);
    }

    @Override
    public void dispose() {

    }
}
