package br.com.tattobr.gametests.activities;

import br.com.tattobr.gametests.screens.AnimationScreen;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

public class AnimationGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new AnimationScreen(this);
    }
}
