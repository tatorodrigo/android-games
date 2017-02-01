package br.com.tattobr.superjumper.game;

import br.com.tattobr.samples.framework.game2d.GameObject;

public class Castle extends GameObject {
    public static final float VIEW_WIDTH = 2f;
    public static final float VIEW_HEIGHT = 2f;
    public static final float WIDTH = 1.7f;
    public static final float HEIGHT = 1.7f;

    public Castle(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
    }
}
