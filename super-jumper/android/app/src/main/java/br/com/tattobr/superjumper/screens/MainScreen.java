package br.com.tattobr.superjumper.screens;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.GLScreen;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.gl.Camera2D;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.superjumper.utils.AssetsUtil;
import br.com.tattobr.superjumper.utils.DimensionUtil;
import br.com.tattobr.superjumper.utils.SettingsUtil;

public class MainScreen extends GLScreen {
    private Camera2D camera2D;
    private SpriteBatcher spriteBatcher;

    public MainScreen(Game game) {
        super(game);

        camera2D = new Camera2D(
                glGame.getGLGraphics(),
                DimensionUtil.GAME_WIDTH,
                DimensionUtil.GAME_HEIGHT
        );
        spriteBatcher = new SpriteBatcher(graphics, 5);
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        game.getInput().getTouchEvents();
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl10 = graphics.getGl();
        final float gameWidth = DimensionUtil.GAME_WIDTH;
        final float gameHeight = DimensionUtil.GAME_HEIGHT;
        final float gameHalfWidth = gameWidth * .5f;
        final float gameHalfHeight = gameHeight * .5f;

        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera2D.setViewportAndMatrices();

        gl10.glEnable(GL10.GL_TEXTURE_2D);

        spriteBatcher.beginBatch(AssetsUtil.background);
        spriteBatcher.drawSprite(
                gameHalfWidth, gameHalfHeight, gameWidth, gameHeight,
                AssetsUtil.backgroundRegion
        );
        spriteBatcher.endBatch();

        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        spriteBatcher.beginBatch(AssetsUtil.items);
        spriteBatcher.drawSprite(
                gameHalfWidth, gameHeight - AssetsUtil.logo.height * .5f - 20f,
                AssetsUtil.logo.width, AssetsUtil.logo.height,
                AssetsUtil.logo
        );
        spriteBatcher.drawSprite(
                gameHalfWidth, 400,
                AssetsUtil.mainMenu.width, AssetsUtil.mainMenu.height,
                AssetsUtil.mainMenu
        );
        spriteBatcher.drawSprite(
                AssetsUtil.soundOn.width * .5f, AssetsUtil.soundOn.height * .5f,
                AssetsUtil.soundOn.width, AssetsUtil.soundOn.height,
                SettingsUtil.soundEnabled ? AssetsUtil.soundOn : AssetsUtil.soundOff
        );
        spriteBatcher.endBatch();

        gl10.glDisable(GL10.GL_BLEND);
        gl10.glDisable(GL10.GL_TEXTURE_2D);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
