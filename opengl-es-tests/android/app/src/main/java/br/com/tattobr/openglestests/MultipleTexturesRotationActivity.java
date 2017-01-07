package br.com.tattobr.openglestests;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.openglestests.utils.Texture;

/**
 * Created by tattobr on 07/01/2017.
 */

public class MultipleTexturesRotationActivity extends MultipleTexturesActivity {
    private float rotation;

    @Override
    public void onDrawFrame(GL10 gl10) {
        super.onDrawFrame(gl10);

        rotation += 2;
        if (rotation > 360) {
            rotation = 0;
        }
    }

    protected void drawTexture(GL10 gl10, Texture texture, float scale, float x, float y) {
        float translationScaled = .5f * scale;
        gl10.glLoadIdentity();
        gl10.glTranslatef(x + translationScaled, y, 0);
        gl10.glRotatef(rotation, 0, 1, 0);
        gl10.glTranslatef(-translationScaled, 0, 0);
        gl10.glScalef(scale, scale, 0);
        texture.bind(gl10);
        gl10.glDrawElements(GL10.GL_TRIANGLES, 6, GL10.GL_UNSIGNED_SHORT, shortBuffer);
        texture.unbind(gl10);
    }
}
