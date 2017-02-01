package br.com.tattobr.superjumper.game;

import br.com.tattobr.samples.framework.game2d.DynamicGameObject;

public class Platform extends DynamicGameObject {
    public static final float WIDTH = 2f;
    public static final float HEIGHT = .5f;
    private static final float MID_WIDTH = WIDTH * .5f;
    private static final float MID_HEIGHT = HEIGHT * .5f;

    public static final int TYPE_STATIC = 0;
    public static final int TYPE_MOVING = 1;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_PULVERIZING = 1;
    public static final float PULVERIZE_TIME = .2f * 4;
    public static final float VELOCITY = 2;

    public int type;
    public int state;
    public float stateTime;

    public Platform(int type, float x, float y) {
        super(x, y, WIDTH, HEIGHT);

        this.type = type;
        state = STATE_NORMAL;
        stateTime = 0;

        if (type == TYPE_MOVING) {
            velocity.set(VELOCITY, 0);
        }
    }

    public void update(float deltaTime) {
        if (type == TYPE_MOVING) {
            position.add(velocity.x * deltaTime, velocity.y * deltaTime);
            if (position.x < MID_WIDTH) {
                position.x = MID_WIDTH;
                velocity.x = -velocity.x;
            } else if (position.x > World.WIDTH - MID_WIDTH) {
                position.x = World.WIDTH - MID_WIDTH;
                velocity.x = -velocity.x;
            }
            bounds.lowerLeft.set(position).sub(MID_WIDTH, MID_HEIGHT);
        }
        stateTime += deltaTime;
    }

    public void pulverize() {
        state = STATE_PULVERIZING;
        stateTime = 0;
        velocity.set(0, 0);
    }
}
