package br.com.tattobr.samples.framework.gl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.FileIO;
import br.com.tattobr.samples.framework.GLGame;
import br.com.tattobr.samples.framework.GLGraphics;

public class Texture {
    private GLGraphics glGraphics;
    private FileIO fileIO;
    private String filename;
    private int textureId;
    private int minFilter;
    private int magFilter;

    public Texture(GLGame glGame, String filename) {
        this.glGraphics = glGame.getGLGraphics();
        this.fileIO = glGame.getFileIO();
        this.filename = filename;
        load();
    }

    private void load() {
        GL10 gl10 = glGraphics.getGl();
        int[] textureIds = new int[1];
        gl10.glGenTextures(1, textureIds, 0);
        textureId = textureIds[0];

        InputStream inputStream = null;
        try {
            inputStream = fileIO.readAsset(filename);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
            GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
            setFilters(GL10.GL_NEAREST, GL10.GL_NEAREST);
            gl10.glBindTexture(GL10.GL_TEXTURE_2D, 0);
            bitmap.recycle();
        } catch (IOException e) {
            throw new RuntimeException("Could not load texture '" + filename + "'", e);
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

    public void reload() {
        load();
        bind();
        setFilters(minFilter, magFilter);
        glGraphics.getGl().glBindTexture(GL10.GL_TEXTURE_2D, 0);
    }

    public void setFilters(int minFilter, int magFilter) {
        this.minFilter = minFilter;
        this.magFilter = magFilter;

        GL10 gl10 = glGraphics.getGl();
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, minFilter);
        gl10.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, magFilter);
    }

    public void bind() {
        GL10 gl10 = glGraphics.getGl();
        gl10.glBindTexture(GL10.GL_TEXTURE_2D, textureId);
    }

    public void dispose() {
        GL10 gl10 = glGraphics.getGl();
        gl10.glBindTexture(GL10.GL_TEXTURE_2D, 0);
        gl10.glDeleteTextures(1, new int[]{textureId}, 0);
    }
}
