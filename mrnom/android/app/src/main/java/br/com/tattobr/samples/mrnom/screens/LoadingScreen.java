package br.com.tattobr.samples.mrnom.screens;

import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.mrnom.utils.AssetsUtil;
import br.com.tattobr.samples.mrnom.utils.SettingsUtil;

public class LoadingScreen extends Screen {
    public LoadingScreen(Game game) {
        super(game);
    }

    @Override
    public void update(float deltaTime) {
        AssetsUtil.initDefaultValues();
        AssetsUtil.loadGraphics(game.getGraphics());
        AssetsUtil.loadAudio(game.getAudio());
        SettingsUtil.load(game.getFileIO());
        game.setScreen(new MainMenuScreen(game));
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
