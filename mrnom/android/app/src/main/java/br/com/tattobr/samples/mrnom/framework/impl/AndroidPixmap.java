package br.com.tattobr.samples.mrnom.framework.impl;

import android.graphics.Bitmap;

import br.com.tattobr.samples.mrnom.framework.Graphics;
import br.com.tattobr.samples.mrnom.framework.Pixmap;

public class AndroidPixmap implements Pixmap {
    Bitmap bitmap;
    private Graphics.PixmapFormat format;

    public AndroidPixmap(Bitmap bitmap, Graphics.PixmapFormat format) {
        this.bitmap = bitmap;
        this.format = format;
    }

    @Override
    public int getWidth() {
        return bitmap.getWidth();
    }

    @Override
    public int getHeight() {
        return bitmap.getHeight();
    }

    @Override
    public Graphics.PixmapFormat getFormat() {
        return format;
    }

    @Override
    public void dispose() {
        bitmap.recycle();
    }
}
