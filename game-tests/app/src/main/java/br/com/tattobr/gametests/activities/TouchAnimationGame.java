package br.com.tattobr.gametests.activities;

import br.com.tattobr.gametests.screens.TouchAnimationScreen;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

public class TouchAnimationGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new TouchAnimationScreen(this);
    }
}
