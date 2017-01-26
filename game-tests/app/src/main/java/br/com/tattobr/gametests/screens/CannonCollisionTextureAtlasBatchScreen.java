package br.com.tattobr.gametests.screens;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.gametests.game2d.Cannon;
import br.com.tattobr.samples.framework.GLGame;
import br.com.tattobr.samples.framework.GLGraphics;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.game2d.DynamicGameObject;
import br.com.tattobr.samples.framework.game2d.GameObject;
import br.com.tattobr.samples.framework.gl.Camera2D;
import br.com.tattobr.samples.framework.gl.SpatialHashGrid;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.samples.framework.gl.Texture;
import br.com.tattobr.samples.framework.gl.TextureRegion;
import br.com.tattobr.samples.framework.math.OverlapTester;
import br.com.tattobr.samples.framework.math.Vector2;
import br.com.tattobr.samples.framework.utils.FPSUtil;

public class CannonCollisionTextureAtlasBatchScreen extends Screen {
    private final int NUM_TARGETS = 20;
    private final float FRUSTUM_WIDTH = 9.6f;
    private final float FRUSTUM_HEIGTH = 4.8f;

    private GLGraphics graphics;
    private Cannon cannon;
    private DynamicGameObject ball;
    private SpatialHashGrid grid;
    private List<GameObject> targets;

    private Vector2 touchPosition;
    private Vector2 gravity;

    private Camera2D camera2D;
    private SpriteBatcher spriteBatcher;

    private Texture texture;
    private TextureRegion cannonTextureRegion;
    private TextureRegion ballTextureRegion;
    private TextureRegion targetTextureRegion;

    private FPSUtil fpsUtil;

    public CannonCollisionTextureAtlasBatchScreen(Game game) {
        super(game);

        graphics = ((GLGame) game).getGLGraphics();
        cannon = new Cannon(0, 0, 1, .5f);
        ball = new DynamicGameObject(0, 0, .2f, .2f);
        grid = new SpatialHashGrid(FRUSTUM_WIDTH, FRUSTUM_HEIGTH, 2.5f, NUM_TARGETS + 2);
        targets = new ArrayList<>(NUM_TARGETS);
        generateTargets();

        touchPosition = new Vector2();
        gravity = new Vector2(0, -10);

        camera2D = new Camera2D(graphics, FRUSTUM_WIDTH, FRUSTUM_HEIGTH);
        camera2D.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGTH / 2);
        camera2D.zoom = 1;
        spriteBatcher = new SpriteBatcher(graphics, NUM_TARGETS + 2);

        fpsUtil = new FPSUtil();
    }

    private void generateTargets() {
        GameObject target;
        grid.clearStaticCells();
        targets.clear();
        for (int i = 0; i < NUM_TARGETS; i++) {
            target = new GameObject(
                    (float) (Math.random() * FRUSTUM_WIDTH),
                    (float) (Math.random() * FRUSTUM_HEIGTH),
                    .5f, .5f
            );
            grid.insertStaticObject(target);
            targets.add(target);
        }
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        Input.TouchEvent touchEvent;

        if (targets.size() == 0) {
            generateTargets();
        }

        int size = touchEvents.size();
        for (int i = 0; i < size; i++) {
            touchEvent = touchEvents.get(i);

            touchPosition.x = ((float) touchEvent.x / graphics.getWidth()) * FRUSTUM_WIDTH;
            touchPosition.y = (1f - (float) touchEvent.y / graphics.getHeight()) * FRUSTUM_HEIGTH;

            cannon.angle = touchPosition.sub(cannon.position).angle();

            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                float radians = cannon.angle * Vector2.TO_RADIANS;
                float speed = touchPosition.len() * 2;

                ball.position.set(cannon.position);
                ball.velocity.x = (float) (Math.cos(radians) * speed);
                ball.velocity.y = (float) (Math.sin(radians) * speed);
                ball.bounds.lowerLeft.set(ball.position.x - ball.bounds.width / 2f, ball.position.y - ball.bounds.height / 2f);
            }
        }

        ball.velocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
        ball.position.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);
        ball.bounds.lowerLeft.add(ball.velocity.x * deltaTime, ball.velocity.y * deltaTime);

        List<GameObject> colliders = grid.getPotentialColliders(ball);
        size = colliders.size();
        GameObject collider;
        for (int i = 0; i < size; i++) {
            collider = colliders.get(i);
            if (OverlapTester.overlapRectangles(ball.bounds, collider.bounds)) {
                grid.removeObject(collider);
                targets.remove(collider);
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl10 = graphics.getGl();

        gl10.glClearColor(.5f, .5f, .5f, 1);
        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        camera2D.setViewportAndMatrices();


        gl10.glEnable(GL10.GL_BLEND);
        gl10.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl10.glEnable(GL10.GL_TEXTURE_2D);

        spriteBatcher.beginBatch(texture);

        int size = targets.size();
        GameObject target;
        for (int i = 0; i < size; i++) {
            target = targets.get(i);

            spriteBatcher.drawSprite(
                    target.position.x, target.position.y,
                    target.bounds.width, target.bounds.height,
                    targetTextureRegion
            );
        }

        spriteBatcher.drawSprite(
                ball.position.x, ball.position.y,
                ball.bounds.width, ball.bounds.height,
                ballTextureRegion
        );

        spriteBatcher.drawSprite(
                cannon.position.x, cannon.position.y,
                cannon.bounds.width, cannon.bounds.height,
                cannon.angle,
                cannonTextureRegion
        );

        spriteBatcher.endBatch();
        fpsUtil.logFPS();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {
        fpsUtil.logFPS();
        texture = new Texture((GLGame) game, "atlas.png");
        texture.setFilters(GL10.GL_LINEAR, GL10.GL_LINEAR);
        cannonTextureRegion = new TextureRegion(texture, 0, 0, 64, 32);
        ballTextureRegion = new TextureRegion(texture, 0, 32, 16, 16);
        targetTextureRegion = new TextureRegion(texture, 32, 32, 32, 32);
    }

    @Override
    public void dispose() {

    }
}
