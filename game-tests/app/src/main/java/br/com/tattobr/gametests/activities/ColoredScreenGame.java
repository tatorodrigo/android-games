package br.com.tattobr.gametests.activities;

import br.com.tattobr.gametests.screens.ColoredScreen;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

public class ColoredScreenGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new ColoredScreen(this);
    }
}
