package br.com.tattobr.gametests.screens;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.GLGame;
import br.com.tattobr.samples.framework.GLGraphics;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.gl.Vertices;
import br.com.tattobr.samples.framework.math.Vector2;
import br.com.tattobr.samples.framework.utils.FPSUtil;

public class CannonGravityScreen extends Screen {
    private final String TAG = CannonGravityScreen.class.getSimpleName();

    private final float FRUSTUM_WIDTH = 9.6f;
    private final float FRUSTUM_HEIGHT = 6.4f;

    private GLGraphics graphics;

    private Vertices cannonVertices;
    private Vertices ballVertices;

    private float cannonAngle;
    private Vector2 cannonPosition;
    private Vector2 ballPosition;
    private Vector2 ballVelocity;
    private Vector2 gravity;
    private Vector2 touchPosition;

    private FPSUtil fpsUtil;

    public CannonGravityScreen(Game game) {
        super(game);

        graphics = ((GLGame) game).getGLGraphics();

        cannonVertices = new Vertices(graphics, 3, 0, false, false);
        cannonVertices.setVertices(new float[]{
                -.5f, -.5f,
                .5f, 0f,
                -.5f, .5f
        }, 0, 6);

        ballVertices = new Vertices(graphics, 4, 6, false, false);
        ballVertices.setVertices(new float[]{
                -0.1f, -0.1f,
                0.1f, -0.1f,
                0.1f, 0.1f,
                -0.1f, 0.1f
        }, 0, 8);
        ballVertices.setIndexes(new short[]{0, 1, 2, 2, 3, 0}, 0, 6);

        cannonAngle = 0;

        cannonPosition = new Vector2();
        ballPosition = new Vector2();
        ballVelocity = new Vector2();
        gravity = new Vector2(0, -10);
        touchPosition = new Vector2();
        
        fpsUtil = new FPSUtil();
    }

    @Override
    public void update(float deltaTime) {
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        Input.TouchEvent touchEvent;
        int size = touchEvents.size();

        for (int i = 0; i < size; i++) {
            touchEvent = touchEvents.get(i);

            touchPosition.x = ((float) touchEvent.x / graphics.getWidth()) * FRUSTUM_WIDTH;
            touchPosition.y = (1f - (float) touchEvent.y / graphics.getHeight()) * FRUSTUM_HEIGHT;

            cannonAngle = touchPosition.sub(cannonPosition).angle();

            if (touchEvent.type == Input.TouchEvent.TOUCH_UP) {
                float radians = cannonAngle * Vector2.TO_RADIANS;
                float ballSpeed = touchPosition.len();

                ballPosition.set(cannonPosition);
                ballVelocity.x = (float) Math.cos(radians) * ballSpeed;
                ballVelocity.y = (float) Math.sin(radians) * ballSpeed;
            }
        }

        ballVelocity.add(gravity.x * deltaTime, gravity.y * deltaTime);
        ballPosition.add(ballVelocity.x * deltaTime, ballVelocity.y * deltaTime);
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl10 = graphics.getGl();

        gl10.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glOrthof(0, FRUSTUM_WIDTH, 0, FRUSTUM_HEIGHT, 1, -1);
        gl10.glMatrixMode(GL10.GL_MODELVIEW);

        gl10.glLoadIdentity();
        gl10.glTranslatef(cannonPosition.x, cannonPosition.y, 0);
        gl10.glRotatef(cannonAngle, 0, 0, 1);
        gl10.glColor4f(1, 1, 1, 1);
        cannonVertices.bind();
        cannonVertices.draw(GL10.GL_TRIANGLES, 0, 3);
        cannonVertices.unbind();

        gl10.glLoadIdentity();
        gl10.glTranslatef(ballPosition.x, ballPosition.y, 0);
        gl10.glColor4f(1, 0, 0, 1);
        ballVertices.bind();
        ballVertices.draw(GL10.GL_TRIANGLES, 0, 6);
        ballVertices.unbind();

        fpsUtil.logFPS();
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
