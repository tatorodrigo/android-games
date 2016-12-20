package br.com.tattobr.samples.mrnom.screens;

import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Graphics;
import br.com.tattobr.samples.framework.Screen;

public class MenuScreen extends Screen {
    private final int BLOCK_SIZE = 50;
    private float x;
    private float y;
    private StringBuffer stringBuffer;

    public MenuScreen(Game game) {
        super(game);
        x = -BLOCK_SIZE;
        stringBuffer = new StringBuffer();
    }

    @Override
    public void update(float deltaTime) {
        x += BLOCK_SIZE * 2 * deltaTime;
        Graphics graphics = game.getGraphics();
        if (x > graphics.getWidth()) {
            x = -BLOCK_SIZE;
            y += BLOCK_SIZE;
        }

        if (y > graphics.getHeight()) {
            y = 0;
        }

        stringBuffer.setLength(0);
        stringBuffer.append("x: ");
        stringBuffer.append((int) x);
        stringBuffer.append(", y: ");
        stringBuffer.append((int) y);
        stringBuffer.append(", deltaTime: ");
        stringBuffer.append(deltaTime);
    }

    @Override
    public void present(float deltaTime) {
        Graphics graphics = game.getGraphics();

        graphics.clear(0xFF000000);
        graphics.drawRect((int) x, (int) y, BLOCK_SIZE, BLOCK_SIZE, 0xFFFFFFFF);
        graphics.drawText(stringBuffer, 50, 50, 16, 0xFFFF0000);
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
