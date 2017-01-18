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

public class CannonRotationScreen extends Screen {
    private GLGraphics glGraphics;
    private float viewFrustumWidth;
    private float viewFrustumHeight;
    private Vertices vertices;

    private float cannonRotation;
    private Vector2 cannonPosition;
    private Vector2 touchPosition;

    public CannonRotationScreen(Game game, float viewFrustumWidth, float viewFrustumHeight) {
        super(game);

        glGraphics = ((GLGame) game).getGLGraphics();

        cannonPosition = new Vector2(2.4f, 0.5f);
        touchPosition = new Vector2();

        vertices = new Vertices(glGraphics, 3, 0, false, false);
        vertices.setVertices(new float[]{-.5f, -.5f, .5f, 0, -.5f, .5f}, 0, 6);

        this.viewFrustumWidth = viewFrustumWidth;
        this.viewFrustumHeight = viewFrustumHeight;
    }

    @Override
    public void update(float deltaTime) {
        game.getInput().getKeyEvents();
        List<Input.TouchEvent> touchEvents = game.getInput().getTouchEvents();
        Input.TouchEvent touchEvent;

        int len = touchEvents.size();
        int w = glGraphics.getWidth();
        int h = glGraphics.getHeight();
        for (int i = 0; i < len; i++) {
            touchEvent = touchEvents.get(i);

            touchPosition.x = ((float) touchEvent.x / w) * viewFrustumWidth;
            touchPosition.y = (1 - (float) touchEvent.y / h) * viewFrustumHeight;

            cannonRotation = touchPosition.sub(cannonPosition).angle();
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGl();

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glViewport(0, 0, glGraphics.getWidth(), glGraphics.getHeight());
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrthof(0, viewFrustumWidth, 0, viewFrustumHeight, 1, -1);

        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(cannonPosition.x, cannonPosition.y, 1);
        gl.glRotatef(cannonRotation, 0, 0, 1);

        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, 3);
        vertices.unbind();
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
