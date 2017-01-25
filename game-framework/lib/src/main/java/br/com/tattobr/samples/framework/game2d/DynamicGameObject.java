package br.com.tattobr.samples.framework.game2d;

import br.com.tattobr.samples.framework.math.Vector2;

public class DynamicGameObject extends GameObject {
    public final Vector2 velocity;
    public final Vector2 acceleration;
    
    public DynamicGameObject(float x, float y, float width, float height) {
        super(x, y, width, height);

        velocity = new Vector2();
        acceleration = new Vector2();
    }
}
