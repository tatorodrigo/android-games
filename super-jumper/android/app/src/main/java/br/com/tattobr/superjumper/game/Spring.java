package br.com.tattobr.superjumper.game;

import br.com.tattobr.samples.framework.game2d.GameObject;

public class Spring extends GameObject {
    public static final float WIDTH = .3f;
    public static final float HEIGHT = .3f;

    public Spring(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
    }
}
