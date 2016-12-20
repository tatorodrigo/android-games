package br.com.tattobr.samples.mrnom.framework.impl;

import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.tattobr.samples.mrnom.framework.Input;
import br.com.tattobr.samples.mrnom.framework.Pool;

public class AndroidKeyboardHandler implements View.OnKeyListener {
    private boolean[] pressedKeys = new boolean[128];
    private Pool<Input.KeyEvent> keyEventPool;
    private List<Input.KeyEvent> keyEventsBuffer = new ArrayList<>();
    private List<Input.KeyEvent> keyEvents = new ArrayList<>();

    public AndroidKeyboardHandler(View view) {
        Pool.PoolObjectFactory<Input.KeyEvent> factory = new Pool.PoolObjectFactory<Input.KeyEvent>() {
            @Override
            public Input.KeyEvent createObject() {
                return new Input.KeyEvent();
            }
        };
        keyEventPool = new Pool<>(factory, 100);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @Override
    public boolean onKey(View view, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE) {
            return false;
        }

        synchronized (this) {
            Input.KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                keyEvent.keyCode = Input.KeyEvent.KEY_DOWN;
                if (keyCode >= 0 && keyCode < 128) {
                    pressedKeys[keyCode] = true;
                }
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                keyEvent.keyCode = Input.KeyEvent.KEY_UP;
                if (keyCode >= 0 && keyCode < 128) {
                    pressedKeys[keyCode] = false;
                }
            }

            keyEventsBuffer.add(keyEvent);
        }

        return false;
    }

    public boolean isKeyPressed(int keyCode) {
        return keyCode >= 0 && keyCode < 128 && pressedKeys[keyCode];
    }

    public List<Input.KeyEvent> getKeyEvents() {
        synchronized (this) {
            int length = keyEvents.size();
            for (int i = 0; i < length; i++) {
                keyEventPool.free(keyEvents.get(i));
            }

            keyEvents.clear();
            keyEvents.addAll(keyEventsBuffer);
            keyEventsBuffer.clear();
            return keyEvents;
        }
    }
}
