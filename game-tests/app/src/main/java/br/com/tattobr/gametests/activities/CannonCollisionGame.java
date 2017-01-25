package br.com.tattobr.gametests.activities;

import br.com.tattobr.gametests.screens.CannonCollisionScreen;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

public class CannonCollisionGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new CannonCollisionScreen(this);
    }
}
