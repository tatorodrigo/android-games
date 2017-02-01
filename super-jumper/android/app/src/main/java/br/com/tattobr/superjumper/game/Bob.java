package br.com.tattobr.superjumper.game;

import br.com.tattobr.samples.framework.game2d.DynamicGameObject;

public class Bob extends DynamicGameObject {
    public static final float WIDTH = .8f;
    public static final float HEIGHT = .8f;
    private static final float MID_WIDTH = WIDTH * .5f;
    private static final float MID_HEIGHT = HEIGHT * .5f;

    public static final int STATE_JUMP = 0;
    public static final int STATE_FALL = 1;
    public static final int STATE_HIT = 2;

    public static final float JUMP_VELOCITY = 11;
    public static final float MOVE_VELOCITY = 20;

    public int state;
    public float stateTime;

    public Bob(float x, float y) {
        super(x, y, WIDTH, HEIGHT);
        state = STATE_FALL;
        stateTime = 0;
    }

    public void update(float deltaTime) {
        velocity.add(World.GRAVITY.x * deltaTime, World.GRAVITY.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

        if (state != STATE_HIT) {
            if (velocity.y > 0) {
                if (state != STATE_JUMP) {
                    state = STATE_JUMP;
                    stateTime = 0;
                }
            } else if (velocity.y < 0) {
                if (state != STATE_FALL) {
                    state = STATE_FALL;
                    stateTime = 0;
                }
            }
        }

        if (position.x > World.WIDTH) {
            position.x = 0;
        } else if (position.x < 0) {
            position.x = World.WIDTH;
        }

        bounds.lowerLeft.set(position).sub(MID_WIDTH, MID_HEIGHT);
        stateTime += deltaTime;
    }

    public void hitSquirrel() {
        state = STATE_HIT;
        stateTime = 0;
        velocity.set(0, 0);
    }

    public void hitPlatform() {
        state = STATE_JUMP;
        stateTime = 0;
        velocity.y = JUMP_VELOCITY;
    }

    public void hitSpring() {
        state = STATE_JUMP;
        stateTime = 0;
        velocity.y = JUMP_VELOCITY * 1.5f;
    }
}
