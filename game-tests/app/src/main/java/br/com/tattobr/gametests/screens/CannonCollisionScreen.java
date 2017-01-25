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
import br.com.tattobr.samples.framework.gl.SpatialHashGrid;
import br.com.tattobr.samples.framework.gl.Vertices;
import br.com.tattobr.samples.framework.math.OverlapTester;
import br.com.tattobr.samples.framework.math.Vector2;

public class CannonCollisionScreen extends Screen {
    private final int NUM_TARGETS = 20;
    private final float FRUSTUM_WIDTH = 9.6f;
    private final float FRUSTUM_HEIGTH = 4.8f;

    private GLGraphics graphics;
    private Cannon cannon;
    private DynamicGameObject ball;
    private SpatialHashGrid grid;
    private List<GameObject> targets;

    private Vertices cannonVertices;
    private Vertices ballVertices;
    private Vertices targetVertices;

    private Vector2 touchPosition;
    private Vector2 gravity;

    public CannonCollisionScreen(Game game) {
        super(game);


        graphics = ((GLGame) game).getGLGraphics();
        cannon = new Cannon(0, 0, 1, 1);
        ball = new DynamicGameObject(0, 0, .5f, .5f);
        grid = new SpatialHashGrid(FRUSTUM_WIDTH, FRUSTUM_HEIGTH, 2.5f, NUM_TARGETS + 2);
        targets = new ArrayList<>(NUM_TARGETS);
        generateTargets();

        cannonVertices = new Vertices(graphics, 3, 0, false, false);
        cannonVertices.setVertices(new float[]{-.5f, -.5f, .5f, 0f, -.5f, .5f}, 0, 6);

        ballVertices = new Vertices(graphics, 4, 6, false, false);
        ballVertices.setVertices(new float[]{-.1f, -.1f, .1f, -.1f, .1f, .1f, -.1f, .1f}, 0, 8);
        ballVertices.setIndexes(new short[]{0, 1, 2, 2, 3, 0}, 0, 6);

        targetVertices = new Vertices(graphics, 4, 6, false, false);
        targetVertices.setVertices(new float[]{-.25f, -.25f, .25f, -.25f, .25f, .25f, -.25f, .25f}, 0, 8);
        targetVertices.setIndexes(new short[]{0, 1, 2, 2, 3, 0}, 0, 6);

        touchPosition = new Vector2();
        gravity = new Vector2(0, -10);
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

        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glOrthof(0, FRUSTUM_WIDTH, 0, FRUSTUM_HEIGTH, 1, -1);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);

        gl10.glColor4f(0, 1, 0, 1);
        int size = targets.size();
        GameObject target;
        targetVertices.bind();
        for (int i = 0; i < size; i++) {
            target = targets.get(i);

            gl10.glLoadIdentity();
            gl10.glTranslatef(target.position.x, target.position.y, 0);
            targetVertices.draw(GL10.GL_TRIANGLES, 0, 6);
        }
        targetVertices.unbind();

        gl10.glColor4f(1, 0, 0, 1);
        gl10.glLoadIdentity();
        gl10.glTranslatef(ball.position.x, ball.position.y, 0);
        ballVertices.bind();
        ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
        ballVertices.unbind();

        gl10.glColor4f(1, 1, 1, 1);
        gl10.glLoadIdentity();
        gl10.glTranslatef(cannon.position.x, cannon.position.y, 0);
        gl10.glRotatef(cannon.angle, 0, 0, 1);
        cannonVertices.bind();
        cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
        cannonVertices.unbind();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
