package br.com.tattobr.gametests.activities;

import br.com.tattobr.gametests.screens.CannonCollisionTextureAtlasBatchScreen;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

public class CannonCollisionTextureAtlasBatchGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new CannonCollisionTextureAtlasBatchScreen(this);
    }
}
