package br.com.tattobr.openglestests.model;

import java.util.Random;

/**
 * Created by tattobr on 06/01/2017.
 */

public class MoveColoredBody {
    private float worldWidth;
    private float worldHeight;
    private float objectWidth;
    private float objectHeight;

    private Random random;
    private float x, y;
    private float dx, dy;
    private int color;

    public MoveColoredBody(float worldWidth, float worldHeight, float objectWidth, float objectHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.objectWidth = objectWidth;
        this.objectHeight = objectHeight;

        random = new Random();
        color = 0xFF000000 | random.nextInt(16777216);

        x = random.nextFloat() * worldWidth;
        y = random.nextFloat() * worldHeight;

        dx = random.nextFloat() * 50;
        dy = random.nextFloat() * 50;
    }

    public void update(float deltaTime) {
        x += dx * deltaTime;
        y += dy * deltaTime;

        if (x < 0) {
            x = 0;
            dx = -dx;
        } else if (x + objectWidth > worldWidth) {
            x = worldWidth - objectWidth;
            dx = -dx;
        }

        if (y < 0) {
            y = 0;
            dy = -dy;
        } else if (y + objectHeight > worldHeight) {
            y = worldHeight - objectHeight;
            dy = -dy;
        }
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getColor() {
        return color;
    }
}
