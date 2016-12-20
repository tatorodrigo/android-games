package br.com.tattobr.samples.mrnom.framework;

public interface Pixmap {
    int getWidth();

    int getHeight();

    Graphics.PixmapFormat getFormat();

    void dispose();
}
