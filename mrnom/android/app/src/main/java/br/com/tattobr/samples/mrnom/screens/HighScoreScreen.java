package br.com.tattobr.samples.mrnom.screens;

import java.util.List;

import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Graphics;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.mrnom.utils.AssetsUtil;
import br.com.tattobr.samples.mrnom.utils.SettingsUtil;

public class HighScoreScreen extends Screen {
    private final String[] lines = new String[5];

    public HighScoreScreen(Game game) {
        super(game);

        for (int i = 0; i < 5; i++) {
            lines[i] = (i + 1) + ". " + SettingsUtil.highscores[i];
        }
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> eventList = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        Graphics graphics = game.getGraphics();
        int height = graphics.getHeight();

        for (Input.TouchEvent touchEvent : eventList) {
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                if (touchEvent.x < AssetsUtil.BUTTONS_SIZE && touchEvent.y > height - AssetsUtil.BUTTONS_SIZE) {
                    game.setScreen(new MainMenuScreen(game));
                    if (SettingsUtil.soundEnabled) {
                        AssetsUtil.click.play(1);
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        Graphics graphics = game.getGraphics();
        int width = graphics.getWidth();
        int height = graphics.getHeight();
        int midWidth = width / 2;

        graphics.drawPixmap(AssetsUtil.background, 0, 0);
        graphics.drawPixmap(AssetsUtil.mainMenu, midWidth - AssetsUtil.mainMenu.getWidth() / 2, AssetsUtil.HIGH_SCORE_Y, 0, AssetsUtil.MAIN_MENU_OPTIONS_HEIGHT, AssetsUtil.mainMenu.getWidth(), AssetsUtil.MAIN_MENU_OPTIONS_HEIGHT);
        graphics.drawPixmap(AssetsUtil.buttons, 0, height - AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE);

        int x = AssetsUtil.HIGH_SCORE_LINE_X;
        int y = AssetsUtil.HIGH_SCORE_LINE_Y;
        for (int i = 0; i < 5; i++) {
            drawText(graphics, lines[i], x, y);

            y += AssetsUtil.HIGH_SCORE_LINE_SPACING;
        }
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

    private void drawText(Graphics graphics, String line, int x, int y) {
        int length = line.length();
        char character;
        for (int i = 0; i < length; i++) {
            character = line.charAt(i);

            if (character == ' ') {
                x += AssetsUtil.NUMBERS_SIZE;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 10 * AssetsUtil.NUMBERS_SIZE;
                srcWidth = AssetsUtil.DOT_SIZE;
            } else {
                srcX = (character - '0') * AssetsUtil.NUMBERS_SIZE;
                srcWidth = AssetsUtil.NUMBERS_SIZE;
            }
            graphics.drawPixmap(AssetsUtil.numbers, x, y, srcX, 0, srcWidth, AssetsUtil.numbers.getHeight());
            x += srcWidth;
        }
    }
}
