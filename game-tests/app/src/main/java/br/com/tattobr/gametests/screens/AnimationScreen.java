package br.com.tattobr.gametests.screens;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.gametests.game2d.WalkingBoy;
import br.com.tattobr.samples.framework.GLGame;
import br.com.tattobr.samples.framework.GLGraphics;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.gl.Animation;
import br.com.tattobr.samples.framework.gl.Camera2D;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.samples.framework.gl.Texture;
import br.com.tattobr.samples.framework.gl.TextureRegion;

public class AnimationScreen extends Screen {
    private final float WORLD_WIDTH = 4.8f;
    private final float WORLD_HEIGHT = 3.2f;
    private final int WALKING_BOYS_COUNT = 20;

    private GLGraphics graphics;
    private Texture texture;
    private Animation animation;
    private Camera2D camera2D;
    private SpriteBatcher spriteBatcher;
    private WalkingBoy[] walkingBoys;

    public AnimationScreen(Game game) {
        super(game);

        graphics = ((GLGame) game).getGLGraphics();

        camera2D = new Camera2D(graphics, WORLD_WIDTH, WORLD_HEIGHT);
        camera2D.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2);

        spriteBatcher = new SpriteBatcher(graphics, WALKING_BOYS_COUNT);

        walkingBoys = new WalkingBoy[WALKING_BOYS_COUNT];
        for (int i = 0; i < WALKING_BOYS_COUNT; i++) {
            walkingBoys[i] = new WalkingBoy(0, 0, .28f, .5f);
            walkingBoys[i].startAtRandom(WORLD_WIDTH, WORLD_HEIGHT);
        }
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        game.getInput().getTouchEvents();

        for (int i = 0; i < WALKING_BOYS_COUNT; i++) {
            walkingBoys[i].update(WORLD_WIDTH, WORLD_HEIGHT, deltaTime);
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl10 = graphics.getGl();

        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera2D.setViewportAndMatrices();

        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glEnable(GL10.GL_TEXTURE_2D);

        spriteBatcher.beginBatch(texture);
        WalkingBoy boy;
        for (int i = 0; i < WALKING_BOYS_COUNT; i++) {
            boy = walkingBoys[i];
            spriteBatcher.drawSprite(
                    boy.position.x, boy.position.y,
                    boy.velocity.x < 0 ? -boy.bounds.width : boy.bounds.width, boy.bounds.height,
                    animation.getKeyFrame(boy.walkingTime, Animation.ANIMATION_LOOPING)
            );
        }
        spriteBatcher.endBatch();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        final int animationFrames = 8;
        final TextureRegion[] keyFrames = new TextureRegion[animationFrames];

        texture = new Texture((GLGame) game, "animation.png");
        float width = (float) texture.getWidth() / animationFrames;
        float height = texture.getHeight();
        for (int i = 0; i < animationFrames; i++) {
            keyFrames[i] = new TextureRegion(
                    texture,
                    i * width, 0,
                    width, height
            );
        }
        animation = new Animation(.13f, keyFrames);
    }

    @Override
    public void dispose() {

    }
}
