package br.com.tattobr.samples.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.GLGraphics;
import br.com.tattobr.samples.framework.math.Vector2;

public class Camera2D {
    public final Vector2 position;
    public float zoom;
    public final float frustumWidth;
    public final float frustumHeight;
    private final GLGraphics graphics;

    public Camera2D(GLGraphics graphics, float frustumWidth, float frustumHeight) {
        position = new Vector2(frustumWidth / 2, frustumHeight / 2);
        zoom = 1f;
        this.graphics = graphics;
        this.frustumWidth = frustumWidth;
        this.frustumHeight = frustumHeight;
    }

    public void setViewportAndMatrices() {
        GL10 gl10 = graphics.getGl();
        gl10.glViewport(0, 0, graphics.getWidth(), graphics.getHeight());
        gl10.glMatrixMode(GL10.GL_PROJECTION);
        gl10.glLoadIdentity();
        gl10.glOrthof(
                position.x - frustumWidth * zoom / 2,
                position.x + frustumWidth * zoom / 2,
                position.y - frustumHeight * zoom / 2,
                position.y + frustumHeight * zoom / 2,
                1, -1
        );
        gl10.glMatrixMode(GL10.GL_MODELVIEW);
        gl10.glLoadIdentity();
    }

    public void touchWorld(Vector2 touch) {
        touch.x = (touch.x / graphics.getWidth()) * frustumWidth * zoom;
        touch.y = (1f - touch.y / graphics.getHeight()) * frustumHeight * zoom;
        touch.add(position).sub(frustumWidth * zoom / 2, frustumHeight * zoom / 2);
    }
}
