package br.com.tattobr.superjumper.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import br.com.tattobr.samples.framework.math.OverlapTester;
import br.com.tattobr.samples.framework.math.Vector2;

public class World {
    public static final float WIDTH = 10f;
    public static final float HEIGHT = 15f * 20f;
    public static final int STATE_RUNNING = 0;
    public static final int STATE_NEXT_LEVEL = 1;
    public static final int STATE_GAME_OVER = 2;
    public static final Vector2 GRAVITY = new Vector2(0, -12);

    public interface WorldListener {
        void jump();

        void highJump();

        void hit();

        void coin();
    }

    private WorldListener listener;

    public Bob bob;
    public Castle castle;
    public List<Coin> coins;
    public List<Platform> platforms;
    public List<Spring> springs;
    public List<Squirrel> squirrels;

    private Random random;
    public int state;
    public int score;
    private float heightSoFar;

    public World(WorldListener listener) {
        this.listener = listener;

        bob = new Bob(WIDTH * .5f, 1);
        coins = new ArrayList<>();
        platforms = new ArrayList<>();
        springs = new ArrayList<>();
        squirrels = new ArrayList<>();

        random = new Random();
        state = STATE_RUNNING;
        score = 0;
        heightSoFar = 0;

        generateLevel();
    }

    private void generateLevel() {
        //Torricelli = Vf^2 = Vo^2 + 2 * a * dS
        //... Vf = 0
        //-> dS = (-Vo^2) / (-2a)
        float y = Platform.HEIGHT * .5f;
        float maxJumpHeight = (Bob.JUMP_VELOCITY * Bob.JUMP_VELOCITY) / (2 * -GRAVITY.y);

        float oneThirdWorldHeight = HEIGHT * .333333f;
        int type;
        float x;
        Platform platform;
        Spring spring;
        Squirrel squirrel;
        Coin coin;

        while (y < HEIGHT) {
            type = random.nextFloat() > .8f ? Platform.TYPE_MOVING : Platform.TYPE_STATIC;
            x = random.nextFloat() * (WIDTH - Platform.WIDTH) + Platform.WIDTH * .5f;
            platform = new Platform(type, x, y);
            platforms.add(platform);

            if (random.nextFloat() > .9f && type == Platform.TYPE_STATIC) {
                spring = new Spring(x, y + Platform.HEIGHT * .5f + Spring.HEIGHT * .5f);
                platform.hasSpringAbove = true;
                springs.add(spring);
            }

            if (y > oneThirdWorldHeight && random.nextFloat() > .8f) {
                squirrel = new Squirrel(x + random.nextFloat(), y + Squirrel.HEIGHT + random.nextFloat() * 2);
                squirrels.add(squirrel);
            }

            if (random.nextFloat() > .6f) {
                coin = new Coin(x + random.nextFloat(), y + Coin.HEIGHT + random.nextFloat() * 3);
                coins.add(coin);
            }

            y += maxJumpHeight - .5f;
            y -= random.nextFloat() * (maxJumpHeight * .333333f);
        }

        castle = new Castle(WIDTH * .5f, y);
    }

    public void update(float deltaTime, float accelX) {
        updateBob(deltaTime, accelX);
        updatePlatforms(deltaTime);
        updateSquirrels(deltaTime);
        updateCoins(deltaTime);
        if (bob.state != Bob.STATE_HIT) {
            checkCollisions();
        }
        checkGameOver();
    }

    private void updateBob(float deltaTime, float accelX) {
        if (bob.state != Bob.STATE_HIT && bob.position.y <= .5) {
            bob.hitPlatform();
        }
        if (bob.state != Bob.STATE_HIT) {
            bob.velocity.x = (-accelX / 10f) * Bob.MOVE_VELOCITY;
        }
        bob.update(deltaTime);
        heightSoFar = Math.max(heightSoFar, bob.position.y);
    }

    private void updatePlatforms(float deltaTime) {
        int size = platforms.size();
        Platform platform;
        for (int i = 0; i < size; i++) {
            platform = platforms.get(i);
            platform.update(deltaTime);

            if (platform.state == Platform.STATE_PULVERIZING && platform.stateTime > Platform.PULVERIZE_TIME) {
                platforms.remove(platform);
                size--;
            }
        }
    }

    private void updateSquirrels(float deltaTime) {
        List<Squirrel> squirrels = this.squirrels;
        for (Squirrel squirrel : squirrels) {
            squirrel.update(deltaTime);
        }
    }

    private void updateCoins(float deltaTime) {
        List<Coin> coins = this.coins;
        for (Coin coin : coins) {
            coin.update(deltaTime);
        }
    }

    private void checkCollisions() {
        checkPlatformCollisions();
        checkSquirrelCollisions();
        checkItemCollisions();
        checkCastleCollisions();
    }

    private void checkPlatformCollisions() {
        if (bob.velocity.y < 0) {
            List<Platform> platforms = this.platforms;
            for (Platform platform : platforms) {
                if (bob.position.y > platform.position.y &&
                        OverlapTester.overlapRectangles(bob.bounds, platform.bounds)) {
                    bob.hitPlatform();
                    listener.jump();
                    if (!platform.hasSpringAbove && random.nextFloat() > .5f) {
                        platform.pulverize();
                    }
                    break;
                }
            }
        }
    }

    private void checkSquirrelCollisions() {
        List<Squirrel> squirrels = this.squirrels;
        for (Squirrel squirrel : squirrels) {
            if (OverlapTester.overlapRectangles(bob.bounds, squirrel.bounds)) {
                bob.hitSquirrel();
                listener.hit();
                break;
            }
        }
    }

    private void checkItemCollisions() {
        List<Coin> coins = this.coins;
        int size = coins.size();
        Coin coin;
        for (int i = 0; i < size; i++) {
            coin = coins.get(i);
            if (OverlapTester.overlapRectangles(bob.bounds, coin.bounds)) {
                coins.remove(coin);
                size--;
                listener.coin();
                score += Coin.SCORE;
            }
        }

        if (bob.velocity.y < 0) {
            List<Spring> springs = this.springs;
            for (Spring spring : springs) {
                if (bob.position.y > spring.position.y &&
                        OverlapTester.overlapRectangles(bob.bounds, spring.bounds)) {
                    bob.hitSpring();
                    listener.highJump();
                    break;
                }
            }
        }
    }

    private void checkCastleCollisions() {
        if (OverlapTester.overlapRectangles(castle.bounds, bob.bounds)) {
            state = STATE_NEXT_LEVEL;
        }
    }

    private void checkGameOver() {
        if (heightSoFar - 8f > bob.position.y) {
            state = STATE_GAME_OVER;
        }
    }
}
