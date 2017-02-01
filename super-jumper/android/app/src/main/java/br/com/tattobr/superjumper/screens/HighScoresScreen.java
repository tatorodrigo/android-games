package br.com.tattobr.superjumper.screens;

import br.com.tattobr.samples.framework.GLScreen;
import br.com.tattobr.samples.framework.Game;

public class HighScoresScreen extends GLScreen {
    public HighScoresScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
    }

    @Override
    public void present(float deltaTime) {

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
