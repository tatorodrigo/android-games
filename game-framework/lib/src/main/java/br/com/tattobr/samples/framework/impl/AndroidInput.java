package br.com.tattobr.samples.framework.impl;

import android.content.Context;
import android.view.View;

import java.util.List;

import br.com.tattobr.samples.framework.Input;

public class AndroidInput implements Input {
    private AndroidAccelerometerHandler androidAccelerometerHandler;
    private AndroidKeyboardHandler androidKeyboardHandler;
    private TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        androidAccelerometerHandler = new AndroidAccelerometerHandler(context);
        androidKeyboardHandler = new AndroidKeyboardHandler(view);
        touchHandler = new AndroidMultiTouchHandler(view, scaleX, scaleY);
    }

    @Override
    public boolean isKeyPressed(int keyCode) {
        return androidKeyboardHandler.isKeyPressed(keyCode);
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }

    @Override
    public float getAccelX() {
        return androidAccelerometerHandler.getAccelX();
    }

    @Override
    public float getAccelY() {
        return androidAccelerometerHandler.getAccelY();
    }

    @Override
    public float getAccelZ() {
        return androidAccelerometerHandler.getAccelY();
    }

    @Override
    public List<KeyEvent> getKeyEvents() {
        return androidKeyboardHandler.getKeyEvents();
    }

    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
}
