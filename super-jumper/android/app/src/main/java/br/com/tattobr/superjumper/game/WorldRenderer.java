package br.com.tattobr.superjumper.game;

import java.util.List;

import br.com.tattobr.samples.framework.GLGraphics;
import br.com.tattobr.samples.framework.gl.Animation;
import br.com.tattobr.samples.framework.gl.Camera2D;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.superjumper.utils.AssetsUtil;

public class WorldRenderer {
    private final float WORLD_WIDTH = 10f;
    private final float WORLD_HEIGHT = 15f;

    private SpriteBatcher spriteBatcher;
    private World world;
    private Camera2D camera2D;

    public WorldRenderer(GLGraphics graphics, SpriteBatcher batcher, World world) {
        spriteBatcher = batcher;
        camera2D = new Camera2D(graphics, WORLD_WIDTH, WORLD_HEIGHT);
        this.world = world;
    }

    public void render() {
        if (world.bob.position.y > camera2D.position.y) {
            camera2D.position.y = world.bob.position.y;
        }
        camera2D.setViewportAndMatrices();
        renderBackground();
        renderObjects();
    }

    private void renderBackground() {
        spriteBatcher.beginBatch(AssetsUtil.background);
        spriteBatcher.drawSprite(
                camera2D.position.x, camera2D.position.y,
                WORLD_WIDTH, WORLD_HEIGHT,
                AssetsUtil.backgroundRegion
        );
        spriteBatcher.endBatch();
    }

    private void renderObjects() {
        spriteBatcher.beginBatch(AssetsUtil.items);
        renderCastle();
        renderPlatforms();
        renderSprings();
        renderSquirrels();
        renderCoins();
        renderBob();
        spriteBatcher.endBatch();
    }

    private void renderCastle() {
        Castle castle = world.castle;
        spriteBatcher.drawSprite(
                castle.position.x, castle.position.y,
                Castle.VIEW_WIDTH, Castle.VIEW_HEIGHT,
                AssetsUtil.castle
        );
    }

    private void renderPlatforms() {
        List<Platform> platforms = world.platforms;
        for (Platform platform : platforms) {
            if (platform.state == Platform.STATE_NORMAL) {
                spriteBatcher.drawSprite(
                        platform.position.x, platform.position.y,
                        Platform.VIEW_WIDTH, Platform.VIEW_HEIGHT,
                        AssetsUtil.platform
                );
            } else {
                spriteBatcher.drawSprite(
                        platform.position.x, platform.position.y,
                        Platform.VIEW_WIDTH, Platform.VIEW_HEIGHT,
                        AssetsUtil.breakingPlatform.getKeyFrame(
                                platform.stateTime,
                                Animation.ANIMATION_NONLOOPING
                        )
                );
            }
        }
    }

    private void renderSprings() {
        List<Spring> springs = world.springs;
        for (Spring spring : springs) {
            spriteBatcher.drawSprite(
                    spring.position.x, spring.position.y,
                    Spring.VIEW_WIDTH, Spring.VIEW_HEIGHT,
                    AssetsUtil.spring
            );
        }
    }

    private void renderSquirrels() {
        List<Squirrel> squirrels = world.squirrels;
        for (Squirrel squirrel : squirrels) {
            spriteBatcher.drawSprite(
                    squirrel.position.x, squirrel.position.y,
                    squirrel.velocity.x > 0 ? Squirrel.VIEW_WIDTH : -Squirrel.VIEW_WIDTH, Squirrel.VIEW_HEIGHT,
                    AssetsUtil.squirrel.getKeyFrame(
                            squirrel.stateTime,
                            Animation.ANIMATION_LOOPING
                    )
            );
        }
    }

    private void renderCoins() {
        List<Coin> coins = world.coins;
        for (Coin coin : coins) {
            spriteBatcher.drawSprite(
                    coin.position.x, coin.position.y,
                    Coin.VIEW_WIDTH, Coin.VIEW_HEIGHT,
                    AssetsUtil.coin.getKeyFrame(
                            coin.stateTime,
                            Animation.ANIMATION_LOOPING
                    )
            );
        }
    }

    private void renderBob() {
        Bob bob = world.bob;
        if (bob.state == Bob.STATE_JUMP) {
            spriteBatcher.drawSprite(
                    bob.position.x, bob.position.y,
                    bob.velocity.x > 0 ? Bob.VIEW_WIDTH : -Bob.VIEW_WIDTH, Bob.VIEW_HEIGHT,
                    AssetsUtil.bobJump.getKeyFrame(
                            bob.stateTime,
                            Animation.ANIMATION_NONLOOPING
                    )
            );
        } else if (bob.state == Bob.STATE_FALL) {
            spriteBatcher.drawSprite(
                    bob.position.x, bob.position.y,
                    bob.velocity.x > 0 ? Bob.VIEW_WIDTH : -Bob.VIEW_WIDTH, Bob.VIEW_HEIGHT,
                    AssetsUtil.bobFall.getKeyFrame(
                            bob.stateTime,
                            Animation.ANIMATION_NONLOOPING
                    )
            );
        } else {
            spriteBatcher.drawSprite(
                    bob.position.x, bob.position.y,
                    bob.velocity.x > 0 ? Bob.VIEW_WIDTH : -Bob.VIEW_WIDTH, Bob.VIEW_HEIGHT,
                    AssetsUtil.bobHit
            );
        }
    }
}
