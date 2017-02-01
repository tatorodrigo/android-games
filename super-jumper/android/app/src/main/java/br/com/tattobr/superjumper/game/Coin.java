package br.com.tattobr.superjumper.game;

import br.com.tattobr.samples.framework.game2d.GameObject;

public class Coin extends GameObject {
    public static final float WIDTH = .5f;
    public static final float HEIGHT = .8f;
    public static final int SCORE = 10;
    public float stateTime;

    public Coin(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
        stateTime = 0;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;
    }
}
