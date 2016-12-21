package br.com.tattobr.samples.mrnom.screens;

import java.util.List;

import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Graphics;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Pixmap;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.mrnom.controller.World;
import br.com.tattobr.samples.mrnom.model.Snake;
import br.com.tattobr.samples.mrnom.model.SnakePart;
import br.com.tattobr.samples.mrnom.model.Stain;
import br.com.tattobr.samples.mrnom.utils.AssetsUtil;
import br.com.tattobr.samples.mrnom.utils.SettingsUtil;
import br.com.tattobr.samples.mrnom.utils.TextUtil;

public class GameScreen extends Screen {
    enum GameState {
        Ready, Running, Paused, GameOver
    }

    private GameState gameState;
    private World world;
    private int oldScore;
    private String score;

    public GameScreen(Game game) {
        super(game);

        oldScore = 0;
        score = "0";
        gameState = GameState.Ready;
        world = new World();
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        if (GameState.Ready.equals(gameState)) {
            updateGameStateReady(touchEvents);
        } else if (GameState.Running.equals(gameState)) {
            updateGameStateRunning(touchEvents, deltaTime);
        } else if (GameState.Paused.equals(gameState)) {
            updateGameStatePaused(touchEvents);
        } else if (GameState.GameOver.equals(gameState)) {
            updateGameStateGameOver(touchEvents);
        }
    }

    private void updateGameStateReady(List<Input.TouchEvent> touchEvents) {
        if (touchEvents.size() > 0) {
            gameState = GameState.Running;
        }
    }

    private void updateGameStateRunning(List<Input.TouchEvent> touchEvents, float deltaTime) {
        Graphics graphics = game.getGraphics();
        int width = graphics.getWidth();
        int height = graphics.getHeight();
        int buttonsSize = AssetsUtil.BUTTONS_SIZE;

        Input.TouchEvent touchEvent;
        int length = touchEvents.size();
        for (int i = 0; i < length; i++) {
            touchEvent = touchEvents.get(i);
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                if (touchEvent.x < buttonsSize && touchEvent.y < buttonsSize) {
                    gameState = GameState.Paused;
                    if (SettingsUtil.soundEnabled) {
                        AssetsUtil.click.play(1);
                    }
                    return;
                } else if (touchEvent.x < buttonsSize && touchEvent.y > height - buttonsSize) {
                    world.snake.turnLeft();
                    world.advanceSnake();
                    if (SettingsUtil.soundEnabled) {
                        AssetsUtil.click.play(1);
                    }
                    return;
                } else if (touchEvent.x > width - buttonsSize && touchEvent.y > height - buttonsSize) {
                    world.snake.turnRight();
                    world.advanceSnake();
                    if (SettingsUtil.soundEnabled) {
                        AssetsUtil.click.play(1);
                    }
                    return;
                }
            }
        }
        world.update(deltaTime);
        if (world.gameOver) {
            gameState = GameState.GameOver;
            if (SettingsUtil.soundEnabled) {
                AssetsUtil.bitten.play(1);
            }
        }

        if (oldScore != world.score) {
            oldScore = world.score;
            score = String.valueOf(oldScore);
            if (SettingsUtil.soundEnabled) {
                AssetsUtil.eat.play(1);
            }
        }
    }

    private void updateGameStatePaused(List<Input.TouchEvent> touchEvents) {
        Graphics graphics = game.getGraphics();
        int buttonWidth = AssetsUtil.pause.getWidth();
        int buttonHeight = AssetsUtil.pause.getHeight() / 2;
        int startX = graphics.getWidth() / 2 - buttonWidth - 2;
        int startY = AssetsUtil.GAME_OVER_PAUSE_Y;

        Input.TouchEvent touchEvent;
        int length = touchEvents.size();
        for (int i = 0; i < length; i++) {
            touchEvent = touchEvents.get(i);
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                if (touchEvent.x > startX && touchEvent.x < startX + buttonWidth &&
                        touchEvent.y > startY && touchEvent.y < startY + buttonHeight) {
                    gameState = GameState.Running;
                    if (SettingsUtil.soundEnabled) {
                        AssetsUtil.click.play(1);
                    }
                    return;
                } else if (touchEvent.x > startX && touchEvent.x < startX + buttonWidth &&
                        touchEvent.y > startY + buttonHeight && touchEvent.y < startY + buttonHeight * 2) {
                    game.setScreen(new MainMenuScreen(game));
                    if (SettingsUtil.soundEnabled) {
                        AssetsUtil.click.play(1);
                    }
                    return;
                }
            }
        }
    }

    private void updateGameStateGameOver(List<Input.TouchEvent> touchEvents) {
        if (touchEvents.size() > 0) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics graphics = game.getGraphics();

        graphics.drawPixmap(AssetsUtil.background, 0, 0);
        drawWorld(world);
        if (GameState.Ready.equals(gameState)) {
            drawGameStateReadyUI();
        } else if (GameState.Running.equals(gameState)) {
            drawGameStateRunningUI();
        } else if (GameState.Paused.equals(gameState)) {
            drawGameStatePausedUI();
        } else if (GameState.GameOver.equals(gameState)) {
            drawGameStateGameOverUI();
        }

        TextUtil.drawText(graphics, score,
                graphics.getWidth() / 2 - score.length() * AssetsUtil.NUMBERS_SIZE / 2,
                graphics.getHeight() - AssetsUtil.GAME_SCORE_Y_DECREMENT);
    }

    private void drawWorld(World world) {
        Graphics graphics = game.getGraphics();

        Stain stain = world.stain;
        if (stain.type == Stain.TYPE_1) {
            graphics.drawPixmap(AssetsUtil.stain1, stain.x * AssetsUtil.GAME_CELL_SIZE, stain.y * AssetsUtil.GAME_CELL_SIZE);
        } else if (stain.type == Stain.TYPE_2) {
            graphics.drawPixmap(AssetsUtil.stain2, stain.x * AssetsUtil.GAME_CELL_SIZE, stain.y * AssetsUtil.GAME_CELL_SIZE);
        } else if (stain.type == Stain.TYPE_3) {
            graphics.drawPixmap(AssetsUtil.stain3, stain.x * AssetsUtil.GAME_CELL_SIZE, stain.y * AssetsUtil.GAME_CELL_SIZE);
        }

        Snake snake = world.snake;
        List<SnakePart> snakeParts = snake.parts;
        int length = snakeParts.size();
        SnakePart snakePart;

        for (int i = 1; i < length; i++) {
            snakePart = snakeParts.get(i);
            graphics.drawPixmap(AssetsUtil.tail, snakePart.x * AssetsUtil.GAME_CELL_SIZE, snakePart.y * AssetsUtil.GAME_CELL_SIZE);
        }

        snakePart = snakeParts.get(0);
        Pixmap head;
        if (snake.direction == Snake.UP) {
            head = AssetsUtil.headUp;
        } else if (snake.direction == Snake.LEFT) {
            head = AssetsUtil.headLeft;
        } else if (snake.direction == Snake.DOWN) {
            head = AssetsUtil.headDown;
        } else {
            head = AssetsUtil.headRight;
        }

        int diffX = (head.getWidth() - AssetsUtil.GAME_CELL_SIZE) / 2;
        int diffY = (head.getHeight() - AssetsUtil.GAME_CELL_SIZE) / 2;
        graphics.drawPixmap(head, snakePart.x * AssetsUtil.GAME_CELL_SIZE - diffX, snakePart.y * AssetsUtil.GAME_CELL_SIZE - diffY);
        //graphics.drawPixmap(head, snakePart.x * AssetsUtil.GAME_CELL_SIZE, snakePart.y * AssetsUtil.GAME_CELL_SIZE);
    }

    private void drawGameStateReadyUI() {
        Graphics graphics = game.getGraphics();
        int width = graphics.getWidth();
        int height = graphics.getHeight();
        int midWidth = width / 2;

        graphics.drawPixmap(AssetsUtil.ready, midWidth - AssetsUtil.ready.getWidth() / 2, AssetsUtil.GAME_READY_Y);
        graphics.drawLine(0, height - AssetsUtil.BUTTONS_SIZE, width, height - AssetsUtil.BUTTONS_SIZE, 0xFF000000);
    }

    private void drawGameStateRunningUI() {
        Graphics graphics = game.getGraphics();
        int width = graphics.getWidth();
        int height = graphics.getHeight();
        int buttonsSize = AssetsUtil.BUTTONS_SIZE;

        graphics.drawPixmap(AssetsUtil.buttons, 0, 0, buttonsSize, buttonsSize * 2, buttonsSize, buttonsSize);
        graphics.drawPixmap(AssetsUtil.buttons, 0, height - buttonsSize, buttonsSize, buttonsSize, buttonsSize, buttonsSize);
        graphics.drawPixmap(AssetsUtil.buttons, width - buttonsSize, height - buttonsSize, 0, buttonsSize, buttonsSize, buttonsSize);
        graphics.drawLine(0, height - buttonsSize, width, height - buttonsSize, 0xFF000000);
    }

    private void drawGameStatePausedUI() {
        Graphics graphics = game.getGraphics();
        int width = graphics.getWidth();
        int height = graphics.getHeight();
        int buttonsSize = AssetsUtil.BUTTONS_SIZE;

        graphics.drawPixmap(AssetsUtil.pause, width / 2 - AssetsUtil.pause.getWidth() / 2, AssetsUtil.GAME_OVER_PAUSE_Y);
        graphics.drawLine(0, height - buttonsSize, width, height - buttonsSize, 0xFF000000);
    }

    private void drawGameStateGameOverUI() {
        Graphics graphics = game.getGraphics();
        int width = graphics.getWidth();
        int height = graphics.getHeight();
        int buttonsSize = AssetsUtil.BUTTONS_SIZE;

        graphics.drawPixmap(AssetsUtil.gameOver, width / 2 - AssetsUtil.pause.getWidth() / 2, AssetsUtil.GAME_OVER_PAUSE_Y);
        graphics.drawLine(0, height - buttonsSize, width, height - buttonsSize, 0xFF000000);
    }

    @Override
    public void pause() {
        if (GameState.Running.equals(gameState)) {
            gameState = GameState.Paused;
        }

        if (world.gameOver) {
            SettingsUtil.addScore(world.score);
            SettingsUtil.save(game.getFileIO());
        }
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean onBackPressed() {
        game.setScreen(new MainMenuScreen(game));
        return true;
    }
}
