package br.com.tattobr.gametests.activities;

import br.com.tattobr.gametests.screens.CannonCollisionCameraScreen;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

public class CannonCollisionCameraGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new CannonCollisionCameraScreen(this);
    }
}
