package br.com.tattobr.samples.framework.math;

public class Rectangle {
    public final Vector2 lowerLeft = new Vector2();
    public float width;
    public float height;

    public Rectangle(float x, float y, float width, float height) {
        lowerLeft.set(x, y);
        this.width = width;
        this.height = height;
    }
}
