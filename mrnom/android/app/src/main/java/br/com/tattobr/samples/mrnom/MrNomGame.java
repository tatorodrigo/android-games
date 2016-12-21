package br.com.tattobr.samples.mrnom;

import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGame;
import br.com.tattobr.samples.mrnom.screens.LoadingScreen;

public class MrNomGame extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new LoadingScreen(this);
    }
}
