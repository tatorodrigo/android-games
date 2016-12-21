package br.com.tattobr.samples.mrnom.controller;

import java.util.List;
import java.util.Random;

import br.com.tattobr.samples.mrnom.model.Snake;
import br.com.tattobr.samples.mrnom.model.SnakePart;
import br.com.tattobr.samples.mrnom.model.Stain;

public class World {
    private final int WORLD_WIDTH = 10;
    private final int WORLD_HEIGHT = 13;
    private final int SCORE_INCREMENT = 10;
    private final float TICK_INITIAL = 0.5f;
    private final float TICK_DECREMENT = 0.05f;

    public Snake snake;
    public Stain stain;
    public boolean gameOver;
    public int score;
    private float tickTime;
    private float tick;

    private Random random;
    private boolean[][] fields;

    public World() {
        snake = new Snake(WORLD_WIDTH, WORLD_HEIGHT);
        random = new Random();
        fields = new boolean[WORLD_WIDTH][WORLD_HEIGHT];
        tick = TICK_INITIAL;
        placeStain();
    }

    private void placeStain() {
        for (int i = 0; i < WORLD_WIDTH; i++) {
            for (int j = 0; j < WORLD_HEIGHT; j++) {
                fields[i][j] = false;
            }
        }

        List<SnakePart> snakeParts = snake.parts;
        for (SnakePart snakePart : snakeParts) {
            fields[snakePart.x][snakePart.y] = true;
        }

        int stainX = random.nextInt(WORLD_WIDTH);
        int stainY = random.nextInt(WORLD_HEIGHT);

        while (true) {
            if (!fields[stainX][stainY]) {
                break;
            }
            stainX++;
            if (stainX >= WORLD_WIDTH) {
                stainX = 0;
                stainY++;
                if (stainY >= WORLD_HEIGHT) {
                    stainY = 0;
                }
            }
        }
        stain = new Stain(stainX, stainY, random.nextInt(3));
    }

    public void update(float deltaTime) {
        if (gameOver) {
            return;
        }

        tickTime += deltaTime;

        while (tickTime > tick) {
            tickTime -= tick;

            advanceSnakeInternal();
        }
    }

    private void advanceSnakeInternal() {
        snake.advance();
        if (snake.checkBitten()) {
            gameOver = true;
            return;
        }

        SnakePart head = snake.parts.get(0);
        if (head.x == stain.x && head.y == stain.y) {
            score += SCORE_INCREMENT;
            snake.eat();
            if (snake.parts.size() == WORLD_WIDTH * WORLD_HEIGHT) {
                gameOver = true;
                return;
            } else {
                placeStain();
            }

            if (score % 100 == 0 && tick - TICK_DECREMENT > 0) {
                tick -= TICK_DECREMENT;
            }
        }
    }

    public void advanceSnake() {
        tickTime = 0;
        advanceSnakeInternal();
    }
}
