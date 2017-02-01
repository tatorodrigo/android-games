package br.com.tattobr.superjumper.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

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

public class MainScreen extends GLScreen {
    private final float mainMenuCenterY;

    private Vector2 touchPosition;
    private Rectangle playBounds;
    private Rectangle highScoresBounds;
    private Rectangle helpBounds;
    private Rectangle soundBounds;

    private Camera2D camera2D;
    private SpriteBatcher spriteBatcher;

    public MainScreen(Game game) {
        super(game);

        mainMenuCenterY = 400;

        final float mainMenuStartX = DimensionUtil.GAME_WIDTH * .5f - DimensionUtil.MAIN_MENU_WIDTH * .5f;
        final float mainMenuStartY = mainMenuCenterY - DimensionUtil.MAIN_MENU_HEIGHT * .5f;
        final float mainMenuItemHeight = DimensionUtil.MAIN_MENU_ITEM_HEIGHT;
        touchPosition = new Vector2();
        playBounds = new Rectangle(
                mainMenuStartX, mainMenuStartY + mainMenuItemHeight * 2f,
                DimensionUtil.MAIN_MENU_ITEM_WIDTH, mainMenuItemHeight
        );
        highScoresBounds = new Rectangle(
                mainMenuStartX, mainMenuStartY + mainMenuItemHeight,
                DimensionUtil.MAIN_MENU_ITEM_WIDTH, mainMenuItemHeight
        );
        helpBounds = new Rectangle(
                mainMenuStartX, mainMenuStartY,
                DimensionUtil.MAIN_MENU_ITEM_WIDTH, mainMenuItemHeight
        );
        soundBounds = new Rectangle(
                0, 0, DimensionUtil.BUTTON_WIDTH, DimensionUtil.BUTTON_HEIGHT
        );

        camera2D = new Camera2D(
                glGame.getGLGraphics(),
                DimensionUtil.GAME_WIDTH,
                DimensionUtil.GAME_HEIGHT
        );
        spriteBatcher = new SpriteBatcher(graphics, 3);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int size = touchEvents.size();
        Input.TouchEvent touchEvent;
        game.getInput().getKeyEvents();

        for (int i = 0; i < size; i++) {
            touchEvent = touchEvents.get(i);
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                camera2D.touchWorld(touchPosition.set(touchEvent.x, touchEvent.y));

                if (OverlapTester.pointInRectangle(playBounds, touchPosition)) {
                    AssetsUtil.playSound(AssetsUtil.clickSound);
                    //game.setScreen();
                    return;
                } else if (OverlapTester.pointInRectangle(highScoresBounds, touchPosition)) {
                    AssetsUtil.playSound(AssetsUtil.clickSound);
                    game.setScreen(new HighScoresScreen(game));
                    return;
                } else if (OverlapTester.pointInRectangle(helpBounds, touchPosition)) {
                    AssetsUtil.playSound(AssetsUtil.clickSound);
                    game.setScreen(new HelpScreen(game));
                    return;
                } else if (OverlapTester.pointInRectangle(soundBounds, touchPosition)) {
                    AssetsUtil.playSound(AssetsUtil.clickSound);
                    SettingsUtil.soundEnabled = !SettingsUtil.soundEnabled;
                    AssetsUtil.playMusic();
                }
            }
        }
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
                gameHalfWidth, mainMenuCenterY,
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
