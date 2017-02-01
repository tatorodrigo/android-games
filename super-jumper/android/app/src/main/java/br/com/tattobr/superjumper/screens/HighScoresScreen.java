package br.com.tattobr.superjumper.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.Font;
import br.com.tattobr.samples.framework.GLScreen;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.gl.Camera2D;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.samples.framework.math.OverlapTester;
import br.com.tattobr.samples.framework.math.Rectangle;
import br.com.tattobr.samples.framework.math.Vector2;
import br.com.tattobr.superjumper.utils.AssetsUtil;
import br.com.tattobr.superjumper.utils.DimensionUtil;
import br.com.tattobr.superjumper.utils.SettingsUtil;

public class HighScoresScreen extends GLScreen {
    private Camera2D camera2D;
    private SpriteBatcher spriteBatcher;
    private Vector2 touchPosition;
    private Rectangle backBounds;
    private String[] highScores;
    private int maxScoreLength;

    public HighScoresScreen(Game game) {
        super(game);

        camera2D = new Camera2D(graphics, DimensionUtil.GAME_WIDTH, DimensionUtil.GAME_HEIGHT);
        spriteBatcher = new SpriteBatcher(graphics, 100);
        touchPosition = new Vector2();
        backBounds = new Rectangle(0, 0, DimensionUtil.BUTTON_WIDTH, DimensionUtil.BUTTON_HEIGHT);
        maxScoreLength = -1;

        highScores = new String[5];
        for (int i = 0; i < 5; i++) {
            highScores[i] = (i + 1) + ". " + SettingsUtil.highscores[i];
            maxScoreLength = Math.max(maxScoreLength, highScores[i].length());
        }
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int size = touchEvents.size();
        Input.TouchEvent touchEvent;

        for (int i = 0; i < size; i++) {
            touchEvent = touchEvents.get(i);
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                camera2D.touchWorld(touchPosition.set(touchEvent.x, touchEvent.y));

                if (OverlapTester.pointInRectangle(backBounds, touchPosition)) {
                    AssetsUtil.playSound(AssetsUtil.clickSound);
                    game.setScreen(new MainScreen(game));
                    return;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl10 = graphics.getGl();

        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera2D.setViewportAndMatrices();

        gl10.glEnable(GL10.GL_TEXTURE_2D);
        spriteBatcher.beginBatch(AssetsUtil.background);
        spriteBatcher.drawSprite(
                AssetsUtil.backgroundRegion.width * .5f, AssetsUtil.backgroundRegion.height * .5f,
                AssetsUtil.backgroundRegion.width, AssetsUtil.backgroundRegion.height,
                AssetsUtil.backgroundRegion
        );
        spriteBatcher.endBatch();

        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        spriteBatcher.beginBatch(AssetsUtil.items);
        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .5f, DimensionUtil.GAME_HEIGHT * .8f,
                AssetsUtil.highScores.width, AssetsUtil.highScores.height,
                AssetsUtil.highScores
        );

        spriteBatcher.drawSprite(
                DimensionUtil.BUTTON_WIDTH * .5f, DimensionUtil.BUTTON_HEIGHT * .8f,
                AssetsUtil.arrow.width, AssetsUtil.arrow.height,
                AssetsUtil.arrow
        );

        Font font = AssetsUtil.font;
        float startY = DimensionUtil.GAME_HEIGHT * .8f - AssetsUtil.highScores.height * 2;
        float startX = DimensionUtil.GAME_WIDTH * .5f - font.getBiggestCharWidth() * maxScoreLength * .25f;
        for (int i = 0; i < 5; i++) {
            font.drawText(spriteBatcher,
                    highScores[i],
                    startX, startY
            );

            startY -= font.getBiggestCharHeight() * 1.2f;
        }
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
