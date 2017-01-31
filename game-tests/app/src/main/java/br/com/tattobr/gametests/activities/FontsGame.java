package br.com.tattobr.gametests.activities;

import br.com.tattobr.gametests.screens.FontsScreen;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

public class FontsGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new FontsScreen(this);
    }
}
