package br.com.tattobr.gametests.screens;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.gametests.game2d.WalkingBoy;
import br.com.tattobr.samples.framework.GLGame;
import br.com.tattobr.samples.framework.GLGraphics;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.game2d.GameObject;
import br.com.tattobr.samples.framework.gl.Animation;
import br.com.tattobr.samples.framework.gl.Camera2D;
import br.com.tattobr.samples.framework.gl.SpatialHashGrid;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.samples.framework.gl.Texture;
import br.com.tattobr.samples.framework.gl.TextureRegion;
import br.com.tattobr.samples.framework.math.OverlapTester;
import br.com.tattobr.samples.framework.math.Vector2;
import br.com.tattobr.samples.framework.utils.FPSUtil;

public class TouchAnimationScreen extends Screen {
    private final int MAX_SPRITES = 30;
    private final float WORLD_WIDTH = 9.6f;
    private final float WORLD_HEIGHT = 4.8f;

    private GLGraphics graphics;
    private Camera2D camera2D;
    private SpriteBatcher spriteBatcher;

    private Vector2 touchPosition;

    private Animation animation;
    private Texture texture;
    private List<WalkingBoy> walkingBoys;
    private WalkingBoy touchWalkingBoy;
    private SpatialHashGrid spatialHashGrid;
    private GameObject touchObject;

    private FPSUtil fpsUtil;

    public TouchAnimationScreen(Game game) {
        super(game);

        graphics = ((GLGame) game).getGLGraphics();

        touchPosition = new Vector2();
        spatialHashGrid = new SpatialHashGrid(
                WORLD_WIDTH, WORLD_HEIGHT,
                2.5f, MAX_SPRITES
        );
        walkingBoys = new ArrayList<>(MAX_SPRITES);
        camera2D = new Camera2D(graphics, WORLD_WIDTH, WORLD_HEIGHT);
        spriteBatcher = new SpriteBatcher(graphics, MAX_SPRITES);
        touchObject = new GameObject(0, 0, .1f, .1f);

        fpsUtil = new FPSUtil();
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        int size = touchEvents.size();

        Input.TouchEvent touchEvent;
        boolean touchUp = false;

        for (int i = 0; i < size; i++) {
            touchEvent = touchEvents.get(i);

            camera2D.touchWorld(touchPosition.set(touchEvent.x, touchEvent.y));
            touchObject.position.set(touchPosition);

            if (touchEvent.type == Input.TouchEvent.TOUCH_DOWN) {
                if (walkingBoys.size() < MAX_SPRITES) {
                    touchWalkingBoy = new WalkingBoy(
                            touchPosition.x, touchPosition.y,
                            .56f, 1f
                    );
                    touchWalkingBoy.velocity.set(.5f, 0);
                }
            } else if (touchEvent.type == Input.TouchEvent.TOUCH_DRAG) {
                if (touchWalkingBoy != null) {
                    touchWalkingBoy.position.set(touchPosition);
                    touchWalkingBoy.walkingTime += deltaTime;
                }
            } else {
                if (touchWalkingBoy != null) {
                    walkingBoys.add(touchWalkingBoy);
                    touchWalkingBoy = null;
                } else {
                    touchUp = true;
                }
            }
        }

        if (touchUp) {
            spatialHashGrid.clearDynamicCells();
        }

        WalkingBoy boy;
        size = walkingBoys.size();
        for (int i = 0; i < size; i++) {
            boy = walkingBoys.get(i);
            boy.update(WORLD_WIDTH, WORLD_HEIGHT, deltaTime);
            if (touchUp) {
                spatialHashGrid.insertDynamicObject(boy);
            }
        }

        if (touchUp) {
            touchObject.bounds.lowerLeft.set(touchObject.position.x - touchObject.bounds.width * .5f, touchObject.position.y - touchObject.bounds.height * .5f);
            List<GameObject> colliders = spatialHashGrid.getPotentialColliders(touchObject);
            size = colliders.size();
            for (int i = 0; i < size; i++) {
                boy = (WalkingBoy) colliders.get(i);
                if (OverlapTester.overlapRectangles(touchObject.bounds, boy.bounds)) {
                    boy.velocity.x = -boy.velocity.x;
                }
            }
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

        int size = walkingBoys.size();
        TextureRegion keyFrame;
        WalkingBoy boy;
        for (int i = 0; i < size; i++) {
            boy = walkingBoys.get(i);
            keyFrame = animation.getKeyFrame(boy.walkingTime, Animation.ANIMATION_LOOPING);

            spriteBatcher.drawSprite(
                    boy.position.x, boy.position.y,
                    boy.velocity.x < 0 ? -boy.bounds.width : boy.bounds.width, boy.bounds.height,
                    keyFrame
            );
        }

        if (touchWalkingBoy != null) {
            keyFrame = animation.getKeyFrame(touchWalkingBoy.walkingTime, Animation.ANIMATION_LOOPING);
            spriteBatcher.drawSprite(
                    touchWalkingBoy.position.x, touchWalkingBoy.position.y,
                    touchWalkingBoy.bounds.width, touchWalkingBoy.bounds.height,
                    keyFrame
            );
        }

        spriteBatcher.endBatch();

        fpsUtil.logFPS();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        final int frames = 8;
        final TextureRegion[] keyFrames = new TextureRegion[frames];
        texture = new Texture((GLGame) game, "animation.png");

        final float width = (float) texture.getWidth() / frames;
        final float height = texture.getHeight();

        for (int i = 0; i < frames; i++) {
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
