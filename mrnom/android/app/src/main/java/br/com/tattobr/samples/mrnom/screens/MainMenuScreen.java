package br.com.tattobr.samples.mrnom.screens;

import java.util.List;

import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Graphics;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.mrnom.utils.AssetsUtil;
import br.com.tattobr.samples.mrnom.utils.SettingsUtil;

public class MainMenuScreen extends Screen {
    public MainMenuScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        Graphics graphics = game.getGraphics();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        int midWidth = graphics.getWidth() / 2;
        int height = graphics.getHeight();
        int mainMenuWidth = AssetsUtil.mainMenu.getWidth();
        int midMainMenuWidth = mainMenuWidth / 2;
        int mainMenuX = midWidth - midMainMenuWidth;

        for (Input.TouchEvent touchEvent : touchEvents) {
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                if (inBounds(touchEvent, 0, height - AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE)) {
                    SettingsUtil.soundEnabled = !SettingsUtil.soundEnabled;
                    if (SettingsUtil.soundEnabled) {
                        AssetsUtil.click.play(1);
                    }
                    break;
                } else if (inBounds(touchEvent, mainMenuX, AssetsUtil.MAIN_MENU_OPTIONS_Y, mainMenuWidth, AssetsUtil.MAIN_MENU_OPTIONS_HEIGHT)) {
                    game.setScreen(new GameScreen(game));
                    if (SettingsUtil.soundEnabled) {
                        AssetsUtil.click.play(1);
                    }
                    break;
                } else if (inBounds(touchEvent, mainMenuX, AssetsUtil.MAIN_MENU_OPTIONS_Y + AssetsUtil.MAIN_MENU_OPTIONS_HEIGHT, mainMenuWidth, AssetsUtil.MAIN_MENU_OPTIONS_HEIGHT)) {
                    game.setScreen(new HighScoreScreen(game));
                    if (SettingsUtil.soundEnabled) {
                        AssetsUtil.click.play(1);
                    }
                    break;
                } else if (inBounds(touchEvent, mainMenuX, AssetsUtil.MAIN_MENU_OPTIONS_Y + AssetsUtil.MAIN_MENU_OPTIONS_HEIGHT * 2, mainMenuWidth, AssetsUtil.MAIN_MENU_OPTIONS_HEIGHT)) {
                    game.setScreen(new HelpScreen(game));
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
        int height = graphics.getHeight();
        int width = graphics.getWidth();
        int midWitdh = width / 2;

        graphics.drawPixmap(AssetsUtil.background, 0, 0);
        graphics.drawPixmap(AssetsUtil.logo, midWitdh - AssetsUtil.logo.getWidth() / 2, AssetsUtil.MAIN_MENU_LOGO_Y);
        graphics.drawPixmap(AssetsUtil.mainMenu, midWitdh - AssetsUtil.mainMenu.getWidth() / 2, AssetsUtil.MAIN_MENU_OPTIONS_Y);

        if (SettingsUtil.soundEnabled) {
            graphics.drawPixmap(AssetsUtil.buttons, 0, height - AssetsUtil.BUTTONS_SIZE, 0, 0, AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE);
        } else {
            graphics.drawPixmap(AssetsUtil.buttons, 0, height - AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE, 0, AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE);
        }
    }

    @Override
    public void pause() {
        SettingsUtil.save(game.getFileIO());
    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    private boolean inBounds(Input.TouchEvent event, int x, int y, int width, int height) {
        return event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1;
    }
}
