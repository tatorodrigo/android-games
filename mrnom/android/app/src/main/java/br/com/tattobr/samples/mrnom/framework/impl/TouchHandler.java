package br.com.tattobr.samples.mrnom.framework.impl;

import android.view.View;

import java.util.List;

import br.com.tattobr.samples.mrnom.framework.Input;

public interface TouchHandler extends View.OnTouchListener {
    boolean isTouchDown(int pointer);

    int getTouchX(int pointer);

    int getTouchY(int pointer);

    List<Input.TouchEvent> getTouchEvents();
}
