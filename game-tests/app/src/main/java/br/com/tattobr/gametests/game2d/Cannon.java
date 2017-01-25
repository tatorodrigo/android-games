package br.com.tattobr.gametests.game2d;

import br.com.tattobr.samples.framework.game2d.GameObject;

public class Cannon extends GameObject {
    public float angle;

    public Cannon(float x, float y, float width, float height) {
        super(x, y, width, height);
        angle = 0;
    }
}
