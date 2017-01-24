package br.com.tattobr.gametests.activities;

import br.com.tattobr.gametests.screens.CannonGravityScreen;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

public class CannonGravityGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new CannonGravityScreen(this);
    }
}
