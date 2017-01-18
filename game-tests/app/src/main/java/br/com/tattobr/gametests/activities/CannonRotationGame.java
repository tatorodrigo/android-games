package br.com.tattobr.gametests.activities;

import br.com.tattobr.gametests.screens.CannonRotationScreen;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

public class CannonRotationGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new CannonRotationScreen(this, 4.8f, 3.2f);
    }
}
