package br.com.tattobr.gametests.game2d;

import br.com.tattobr.samples.framework.game2d.DynamicGameObject;

public class WalkingBoy extends DynamicGameObject {
    public float walkingTime;

    public WalkingBoy(float x, float y, float width, float height) {
        super(x, y, width, height);
    }

    public void startAtRandom(float worldWidth, float worldHeight) {
        velocity.set(Math.random() > .5f ? .5f : -.5f, 0);
        position.set((float) (worldWidth * Math.random()), (float) (worldHeight * Math.random()));
        walkingTime = (float) (Math.random() * 10);
    }

    public void update(float worldWidth, float worldHeight, float deltaTime) {
        walkingTime += deltaTime;
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

        if (position.x < 0) {
            position.x = worldWidth;
        } else if (position.x > worldWidth) {
            position.x = 0;
        }
    }
}
