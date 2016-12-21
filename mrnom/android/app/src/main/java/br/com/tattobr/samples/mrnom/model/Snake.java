package br.com.tattobr.samples.mrnom.model;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    public static final int UP = 0;
    public static final int LEFT = 1;
    public static final int DOWN = 2;
    public static final int RIGHT = 3;

    public List<SnakePart> parts;
    public int direction;

    private int worldWidth;
    private int worldHeight;

    public Snake(int worldWidth, int worldHeight) {
        direction = UP;

        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        parts = new ArrayList<>();
        parts.add(new SnakePart(5, 6));
        parts.add(new SnakePart(5, 7));
        parts.add(new SnakePart(5, 8));
    }

    public void turnLeft() {
        direction += 1;
        if (direction > RIGHT) {
            direction = UP;
        }
    }

    public void turnRight() {
        direction -= 1;
        if (direction < UP) {
            direction = RIGHT;
        }
    }

    public void eat() {
        SnakePart last = parts.get(parts.size() - 1);
        parts.add(new SnakePart(last.x, last.y));
    }

    public void advance() {
        SnakePart head = parts.get(0);
        int length = parts.size() - 1;

        SnakePart currentTail;
        SnakePart previousTail;
        for (int i = length; i > 0; i--) {
            currentTail = parts.get(i);
            previousTail = parts.get(i - 1);

            currentTail.x = previousTail.x;
            currentTail.y = previousTail.y;
        }

        if (direction == UP) {
            head.y -= 1;
        } else if (direction == LEFT) {
            head.x -= 1;
        } else if (direction == DOWN) {
            head.y += 1;
        } else if (direction == RIGHT) {
            head.x += 1;
        }

        if (head.x < 0) {
            head.x = worldWidth - 1;
        } else if (head.x >= worldWidth) {
            head.x = 0;
        }

        if (head.y < 0) {
            head.y = worldHeight - 1;
        } else if (head.y >= worldHeight) {
            head.y = 0;
        }
    }

    public boolean checkBitten() {
        SnakePart head = parts.get(0);
        int length = parts.size();
        SnakePart tail;
        for (int i = 1; i < length; i++) {
            tail = parts.get(i);
            if (tail.x == head.x && tail.y == head.y) {
                return true;
            }
        }
        return false;
    }
}
