package br.com.tattobr.samples.mrnom;

import android.os.Bundle;

import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGame;
import br.com.tattobr.samples.mrnom.screens.LoadingScreen;
import br.com.tattobr.samples.mrnom.utils.AssetsUtil;

public class MrNomGame extends AndroidGame {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AssetsUtil.GRAPHICS_RESOLUTION = 2.6f;
        AssetsUtil.GRAPHICS_SUFIX = "_hd";
        super.onCreate(savedInstanceState);
    }

    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }

    @Override
    protected int getGameWidth() {
        return (int) (480 * AssetsUtil.GRAPHICS_RESOLUTION);
    }

    @Override
    protected int getGameHeight() {
        return (int) (320 * AssetsUtil.GRAPHICS_RESOLUTION);
    }
}
