package br.com.tattobr.superjumper.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.GLScreen;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.gl.Animation;
import br.com.tattobr.samples.framework.gl.Camera2D;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.samples.framework.gl.TextureRegion;
import br.com.tattobr.samples.framework.math.OverlapTester;
import br.com.tattobr.samples.framework.math.Rectangle;
import br.com.tattobr.samples.framework.math.Vector2;
import br.com.tattobr.superjumper.utils.AssetsUtil;
import br.com.tattobr.superjumper.utils.DimensionUtil;

public abstract class HelpBaseScreen extends GLScreen {
    private final SpriteBatcher spriteBatcher;
    private final Camera2D camera2D;
    private final Vector2 touchPosition;
    private final Rectangle nextScreenBounds;

    public HelpBaseScreen(Game game) {
        super(game);

        spriteBatcher = new SpriteBatcher(graphics, 200);
        camera2D = new Camera2D(graphics, DimensionUtil.GAME_WIDTH, DimensionUtil.GAME_HEIGHT);
        touchPosition = new Vector2();
        nextScreenBounds = new Rectangle(
                DimensionUtil.GAME_WIDTH - DimensionUtil.BUTTON_WIDTH, 0,
                DimensionUtil.BUTTON_WIDTH, DimensionUtil.BUTTON_HEIGHT
        );
    }

    public abstract Screen getNextScreen();

    public abstract void presentItems(SpriteBatcher spriteBatcher, float deltaTime);

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

                if (OverlapTester.pointInRectangle(nextScreenBounds, touchPosition)) {
                    AssetsUtil.playSound(AssetsUtil.clickSound);
                    game.setScreen(getNextScreen());
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
                AssetsUtil.platform.width * .5f + DimensionUtil.GAME_WIDTH * .1f, DimensionUtil.GAME_HEIGHT * .33f,
                AssetsUtil.platform.width, AssetsUtil.platform.height,
                AssetsUtil.platform
        );

        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .85f - AssetsUtil.platform.width * .5f, DimensionUtil.GAME_HEIGHT * .25f,
                AssetsUtil.platform.width, AssetsUtil.platform.height,
                AssetsUtil.platform
        );

        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .5f, DimensionUtil.GAME_HEIGHT * .48f,
                AssetsUtil.platform.width, AssetsUtil.platform.height,
                AssetsUtil.platform
        );

        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .9f - AssetsUtil.platform.width * .5f, DimensionUtil.GAME_HEIGHT * .75f,
                AssetsUtil.platform.width, AssetsUtil.platform.height,
                AssetsUtil.platform
        );

        spriteBatcher.drawSprite(
                AssetsUtil.platform.width * .5f + DimensionUtil.GAME_WIDTH * .15f, DimensionUtil.GAME_HEIGHT * .8f,
                AssetsUtil.platform.width, AssetsUtil.platform.height,
                AssetsUtil.platform
        );

        TextureRegion bob = AssetsUtil.bobJump.getKeyFrame(0, Animation.ANIMATION_NONLOOPING);
        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .5f, DimensionUtil.GAME_HEIGHT * .48f + bob.height * 2f,
                bob.width, bob.height,
                bob
        );

        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH - AssetsUtil.arrow.width * .5f, AssetsUtil.arrow.height * .5f,
                -AssetsUtil.arrow.width, AssetsUtil.arrow.height,
                AssetsUtil.arrow
        );

        presentItems(spriteBatcher, deltaTime);
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
