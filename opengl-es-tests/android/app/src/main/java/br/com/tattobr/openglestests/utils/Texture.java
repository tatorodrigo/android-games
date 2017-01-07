package br.com.tattobr.openglestests.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by tattobr on 07/01/2017.
 */

public class Texture {
    private int textureId;
    private boolean disposed;
    private String filename;

    public Texture(Context context, GL10 gl10, String filename) {
        this.filename = filename;
        load(context, gl10);
    }

    private void load(Context context, GL10 gl10) {
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            int[] textureIds = new int[1];
            gl10.glGenTextures(1, textureIds, 0);
            textureId = textureIds[0];
            disposed = false;
            
            bind(gl10);
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
            gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);

            bitmap.recycle();
            unbind(gl10);
        } catch (IOException e) {
            throw new RuntimeException("Cannot find asset " + filename);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void reload(Context context, GL10 gl10) {
        dispose(gl10);
        load(context, gl10);
    }

    public void bind(GL10 gl10) {
        if (disposed) {
            throw new IllegalStateException("Cannot bind a diposed texture");
        }
        gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    public void unbind(GL10 gl10) {
        gl10.glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    public void dispose(GL10 gl10) {
        if (!disposed) {
            disposed = true;
            unbind(gl10);
            gl10.glDeleteTextures(1, new int[]{textureId}, 0);
        }
    }
}
