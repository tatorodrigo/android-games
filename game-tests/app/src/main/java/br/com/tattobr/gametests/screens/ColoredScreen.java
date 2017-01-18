package br.com.tattobr.gametests.screens;

import java.util.Random;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.GLGraphics;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;

/**
 * Created by tattobr on 07/01/2017.
 */

public class ColoredScreen extends Screen {
    private GLGraphics graphics;
    private Random random;

    public ColoredScreen(Game game) {
        super(game);

        graphics = ((AndroidGLGame) game).getGLGraphics();
        random = new Random();
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        game.getInput().getTouchEvents();
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl10 = graphics.getGl();
        gl10.glClearColor(random.nextFloat(), random.nextFloat(), random.nextFloat(), 1);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
