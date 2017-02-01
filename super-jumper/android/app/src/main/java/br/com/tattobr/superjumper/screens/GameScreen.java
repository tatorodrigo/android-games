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
import br.com.tattobr.samples.framework.utils.FPSUtil;
import br.com.tattobr.superjumper.game.World;
import br.com.tattobr.superjumper.game.WorldRenderer;
import br.com.tattobr.superjumper.utils.AssetsUtil;
import br.com.tattobr.superjumper.utils.DimensionUtil;
import br.com.tattobr.superjumper.utils.SettingsUtil;

public class GameScreen extends GLScreen {
    private final int GAME_READY = 0;
    private final int GAME_RUNNING = 1;
    private final int GAME_PAUSED = 2;
    private final int GAME_LEVEL_END = 3;
    private final int GAME_GAME_OVER = 4;

    private FPSUtil fpsUtil;
    private final SpriteBatcher spriteBatcher;
    private final Camera2D camera2D;
    private final Vector2 touchPosition;
    private final Rectangle pauseBounds;
    private final Rectangle resumeBounds;
    private final Rectangle quitBounds;

    private final String nextLevelText1;
    private final String nextLevelText2;

    private int state;
    private World world;
    private World.WorldListener worldListener;
    private WorldRenderer worldRenderer;
    private String score;
    private int lastScore;

    public GameScreen(Game game) {
        super(game);

        score = "Score: 0";
        lastScore = 0;
        fpsUtil = new FPSUtil();
        spriteBatcher = new SpriteBatcher(graphics, 1000);
        camera2D = new Camera2D(graphics, DimensionUtil.GAME_WIDTH, DimensionUtil.GAME_HEIGHT);
        touchPosition = new Vector2();
        pauseBounds = new Rectangle(
                DimensionUtil.GAME_WIDTH - DimensionUtil.BUTTON_WIDTH,
                DimensionUtil.GAME_HEIGHT - DimensionUtil.BUTTON_HEIGHT,
                DimensionUtil.BUTTON_WIDTH, DimensionUtil.BUTTON_HEIGHT
        );
        resumeBounds = new Rectangle(
                DimensionUtil.GAME_WIDTH * .5f - DimensionUtil.RESUME_QUIT_MENU_WIDTH * .5f,
                DimensionUtil.GAME_HEIGHT * .5f,
                DimensionUtil.RESUME_QUIT_MENU_ITEM_WIDTH, DimensionUtil.RESUME_QUIT_MENU_ITEM_HEIGHT
        );
        quitBounds = new Rectangle(
                DimensionUtil.GAME_WIDTH * .5f - DimensionUtil.RESUME_QUIT_MENU_WIDTH * .5f,
                DimensionUtil.GAME_HEIGHT * .5f - DimensionUtil.RESUME_QUIT_MENU_HEIGHT * .5f,
                DimensionUtil.RESUME_QUIT_MENU_ITEM_WIDTH, DimensionUtil.RESUME_QUIT_MENU_ITEM_HEIGHT
        );

        nextLevelText1 = "The princess is...";
        nextLevelText2 = "in another castle!";

        state = GAME_READY;

        worldListener = new World.WorldListener() {
            @Override
            public void jump() {
                AssetsUtil.playSound(AssetsUtil.jumpSound);
            }

            @Override
            public void highJump() {
                AssetsUtil.playSound(AssetsUtil.highJumpSound);
            }

            @Override
            public void hit() {
                AssetsUtil.playSound(AssetsUtil.hitSound);
            }

            @Override
            public void coin() {
                AssetsUtil.playSound(AssetsUtil.coinSound);
            }
        };
        world = new World(worldListener);
        worldRenderer = new WorldRenderer(graphics, spriteBatcher, world);
    }

    @Override
    public void update(float deltaTime) {
        if (deltaTime > .1f) {
            deltaTime = .1f;
        }

        switch (state) {
            case GAME_READY:
                updateGameReady(deltaTime);
                break;
            case GAME_RUNNING:
                updateGameRunning(deltaTime);
                break;
            case GAME_PAUSED:
                updateGamePaused(deltaTime);
                break;
            case GAME_LEVEL_END:
                updateGameLevelEnd(deltaTime);
                break;
            case GAME_GAME_OVER:
                updateGameOver(deltaTime);
                break;
        }
    }

    private void updateGameReady(float deltaTime) {
        game.getInput().getKeyEvents();
        if (game.getInput().getTouchEvents().size() > 0) {
            state = GAME_RUNNING;
        }
    }

    private void updateGameRunning(float deltaTime) {
        game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int size = touchEvents.size();
        Input.TouchEvent touchEvent;
        for (int i = 0; i < size; i++) {
            touchEvent = touchEvents.get(i);
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                camera2D.touchWorld(touchPosition.set(touchEvent.x, touchEvent.y));
                if (OverlapTester.pointInRectangle(pauseBounds, touchPosition)) {
                    AssetsUtil.playSound(AssetsUtil.clickSound);
                    state = GAME_PAUSED;
                    return;
                }
            }
        }

        world.update(deltaTime, game.getInput().getAccelX());

        if (lastScore != world.score) {
            lastScore = world.score;
            score = "Score: " + lastScore;
        }

        if (world.state == World.STATE_NEXT_LEVEL) {
            state = GAME_LEVEL_END;
        }

        if (world.state == World.STATE_GAME_OVER) {
            state = GAME_GAME_OVER;
            if (lastScore > SettingsUtil.highscores[4]) {
                score = "New high score: " + lastScore;
            } else {
                score = "Score: " + lastScore;
            }
            SettingsUtil.addScore(lastScore);
            SettingsUtil.save(game.getFileIO());
        }
    }

    private void updateGamePaused(float deltaTime) {
        game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int size = touchEvents.size();
        Input.TouchEvent touchEvent;
        for (int i = 0; i < size; i++) {
            touchEvent = touchEvents.get(i);
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                camera2D.touchWorld(touchPosition.set(touchEvent.x, touchEvent.y));
                if (OverlapTester.pointInRectangle(resumeBounds, touchPosition)) {
                    AssetsUtil.playSound(AssetsUtil.clickSound);
                    state = GAME_RUNNING;
                    return;
                } else if (OverlapTester.pointInRectangle(quitBounds, touchPosition)) {
                    AssetsUtil.playSound(AssetsUtil.clickSound);
                    game.setScreen(new MainScreen(game));
                    return;
                }
            }
        }
    }

    private void updateGameLevelEnd(float deltaTime) {
        game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int size = touchEvents.size();
        Input.TouchEvent touchEvent;
        for (int i = 0; i < size; i++) {
            touchEvent = touchEvents.get(i);
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                AssetsUtil.playSound(AssetsUtil.clickSound);
                world = new World(worldListener);
                worldRenderer = new WorldRenderer(graphics, spriteBatcher, world);
                world.score = lastScore;
                state = GAME_READY;
                return;
            }
        }
    }

    private void updateGameOver(float deltaTime) {
        game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int size = touchEvents.size();
        Input.TouchEvent touchEvent;
        for (int i = 0; i < size; i++) {
            touchEvent = touchEvents.get(i);
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                AssetsUtil.playSound(AssetsUtil.clickSound);
                game.setScreen(new MainScreen(game));
                return;
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl10 = graphics.getGl();

        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);

        gl10.glEnable(GL10.GL_TEXTURE_2D);
        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

        worldRenderer.render();

        camera2D.setViewportAndMatrices();
        spriteBatcher.beginBatch(AssetsUtil.items);
        switch (state) {
            case GAME_READY:
                presentGameReady(deltaTime);
                break;
            case GAME_RUNNING:
                presentGameRunning(deltaTime);
                break;
            case GAME_PAUSED:
                presentGamePaused(deltaTime);
                break;
            case GAME_LEVEL_END:
                presentGameLevelEnd(deltaTime);
                break;
            case GAME_GAME_OVER:
                presentGameOver(deltaTime);
                break;
        }
        spriteBatcher.endBatch();

        gl10.glDisable(GL10.GL_BLEND);
        gl10.glDisable(GL10.GL_TEXTURE_2D);

        fpsUtil.logFPS();
    }

    private void presentGameReady(float deltaTime) {
        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .5f, DimensionUtil.GAME_HEIGHT * .5f,
                AssetsUtil.ready.width, AssetsUtil.ready.height,
                AssetsUtil.ready
        );
    }

    private void presentGameRunning(float deltaTime) {
        Font font = AssetsUtil.font;
        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH - DimensionUtil.BUTTON_WIDTH * .5f, DimensionUtil.GAME_HEIGHT - DimensionUtil.BUTTON_HEIGHT * .5f,
                DimensionUtil.BUTTON_WIDTH, DimensionUtil.BUTTON_HEIGHT,
                AssetsUtil.pause
        );
        font.drawText(spriteBatcher, score, font.getBiggestCharWidth(), DimensionUtil.GAME_HEIGHT - font.getBiggestCharHeight());
    }

    private void presentGamePaused(float deltaTime) {
        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .5f, DimensionUtil.GAME_HEIGHT * .5f,
                AssetsUtil.pauseMenu.width, AssetsUtil.pauseMenu.height,
                AssetsUtil.pauseMenu
        );
    }

    private void presentGameLevelEnd(float deltaTime) {
        Font font = AssetsUtil.font;

        font.drawText(
                spriteBatcher, nextLevelText1,
                DimensionUtil.GAME_WIDTH * .5f - nextLevelText1.length() * font.getBiggestCharWidth() * .25f,
                DimensionUtil.GAME_HEIGHT * .5f
        );

        font.drawText(
                spriteBatcher, nextLevelText2,
                DimensionUtil.GAME_WIDTH * .5f - nextLevelText2.length() * font.getBiggestCharWidth() * .25f,
                DimensionUtil.GAME_HEIGHT * .4f
        );
    }

    private void presentGameOver(float deltaTime) {
        Font font = AssetsUtil.font;
        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .5f, DimensionUtil.GAME_HEIGHT * .5f,
                AssetsUtil.gameOver.width, AssetsUtil.gameOver.height,
                AssetsUtil.gameOver
        );

        font.drawText(
                spriteBatcher, score,
                DimensionUtil.GAME_WIDTH * .5f - score.length() * font.getBiggestCharWidth() * .25f,
                DimensionUtil.GAME_HEIGHT * .25f
        );
    }

    @Override
    public void pause() {
        if (state == GAME_RUNNING) {
            state = GAME_PAUSED;
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
