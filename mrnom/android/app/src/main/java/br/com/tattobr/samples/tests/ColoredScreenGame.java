package br.com.tattobr.samples.tests;

import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

/**
 * Created by tattobr on 07/01/2017.
 */

public class ColoredScreenGame extends AndroidGLGame {
    @Override
    public Screen getStartScreen() {
        return new ColoredScreen(this);
    }
}
