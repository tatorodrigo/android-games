package br.com.tattobr.superjumper.game;

import br.com.tattobr.samples.framework.game2d.DynamicGameObject;

public class Squirrel extends DynamicGameObject {
    public static final float WIDTH = 1f;
    public static final float HEIGHT = .6f;
    private static final float MID_WIDTH = WIDTH * .5f;
    private static final float MID_HEIGHT = HEIGHT * .5f;
    public static final float VELOCITY = 3f;
    public float stateTime;

    public Squirrel(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
        stateTime = 0;
        velocity.set(VELOCITY, 0);
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        if (position.x < MID_WIDTH) {
            position.x = MID_WIDTH;
            velocity.x = -velocity.x;
        } else if (position.x > World.WIDTH - MID_WIDTH) {
            position.x = World.WIDTH - MID_WIDTH;
            velocity.x = -velocity.x;
        }
        bounds.lowerLeft.set(position).sub(MID_WIDTH, MID_HEIGHT);
        stateTime += deltaTime;
    }
}
