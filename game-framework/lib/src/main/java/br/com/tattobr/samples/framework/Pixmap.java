package br.com.tattobr.samples.framework;

public interface Pixmap {
    int getWidth();

    int getHeight();

    Graphics.PixmapFormat getFormat();

    void dispose();
}
