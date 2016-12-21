package br.com.tattobr.samples.mrnom.screens;

import java.util.List;

import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Graphics;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.mrnom.utils.AssetsUtil;
import br.com.tattobr.samples.mrnom.utils.SettingsUtil;

public class HelpScreen extends Screen {
    public HelpScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> eventList = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();

        Graphics graphics = game.getGraphics();
        int width = graphics.getWidth();
        int height = graphics.getHeight();

        for (Input.TouchEvent touchEvent : eventList) {
            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                if (touchEvent.x > width - AssetsUtil.BUTTONS_SIZE && touchEvent.y > height - AssetsUtil.BUTTONS_SIZE) {
                    game.setScreen(new HelpScreen2(game));
                    if(SettingsUtil.soundEnabled) {
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

        graphics.drawPixmap(AssetsUtil.background, 0, 0);
        graphics.drawPixmap(AssetsUtil.help1, width / 2 - AssetsUtil.help1.getWidth() / 2, AssetsUtil.HELP_Y);
        graphics.drawPixmap(AssetsUtil.buttons, width - AssetsUtil.BUTTONS_SIZE, height - AssetsUtil.BUTTONS_SIZE, 0, AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE, AssetsUtil.BUTTONS_SIZE);
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

    @Override
    public boolean onBackPressed() {
        game.setScreen(new MainMenuScreen(game));
        return true;
    }
}
