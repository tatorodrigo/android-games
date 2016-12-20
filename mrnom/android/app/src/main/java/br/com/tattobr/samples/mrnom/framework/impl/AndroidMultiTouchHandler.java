package br.com.tattobr.samples.mrnom.framework.impl;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.tattobr.samples.mrnom.framework.Input;
import br.com.tattobr.samples.mrnom.framework.Pool;

public class AndroidMultiTouchHandler implements TouchHandler {
    private final int MAX_TOUCH = 20;
    private boolean[] isTouched = new boolean[MAX_TOUCH];
    private int[] touchX = new int[MAX_TOUCH];
    private int[] touchY = new int[MAX_TOUCH];
    private Pool<Input.TouchEvent> touchEventPool;
    private List<Input.TouchEvent> touchEvents = new ArrayList<>();
    private List<Input.TouchEvent> touchEventsBuffer = new ArrayList<>();
    private float scaleX;
    private float scaleY;

    public AndroidMultiTouchHandler(View view, float scaleX, float scaleY) {
        Pool.PoolObjectFactory<Input.TouchEvent> factory = new Pool.PoolObjectFactory<Input.TouchEvent>() {
            @Override
            public Input.TouchEvent createObject() {
                return new Input.TouchEvent();
            }
        };

        touchEventPool = new Pool<>(factory, 100);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public boolean isTouchDown(int pointer) {
        return pointer >= 0 && pointer < MAX_TOUCH && isTouched[pointer];
    }

    @Override
    public int getTouchX(int pointer) {
        return pointer >= 0 && pointer < MAX_TOUCH ? touchX[pointer] : 0;
    }

    @Override
    public int getTouchY(int pointer) {
        return pointer >= 0 && pointer < MAX_TOUCH ? touchY[pointer] : 0;
    }

    @Override
    public List<Input.TouchEvent> getTouchEvents() {
        synchronized (this) {
            int length = touchEvents.size();
            for (int i = 0; i < length; i++) {
                touchEventPool.free(touchEvents.get(i));
            }
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        synchronized (this) {
            int action = motionEvent.getActionMasked();
            int pointerIndex = motionEvent.getActionIndex();
            int pointerId = motionEvent.getPointerId(pointerIndex);

            Input.TouchEvent touchEvent;

            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = Input.TouchEvent.TOUCH_DOWN;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (motionEvent.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] = (int) (motionEvent.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = true;
                    touchEventsBuffer.add(touchEvent);
                    break;

                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                case MotionEvent.ACTION_CANCEL:
                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = Input.TouchEvent.TOUCH_UP;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (motionEvent.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] = (int) (motionEvent.getY(pointerIndex) * scaleY);
                    isTouched[pointerId] = false;
                    touchEventsBuffer.add(touchEvent);
                    break;

                case MotionEvent.ACTION_MOVE:
                    int pointerCount = motionEvent.getPointerCount();
                    for (int i = 0; i < pointerCount; i++) {
                        pointerIndex = i;
                        pointerId = motionEvent.getPointerId(pointerIndex);

                        touchEvent = touchEventPool.newObject();
                        touchEvent.type = Input.TouchEvent.TOUCH_DRAG;
                        touchEvent.pointer = pointerId;
                        touchEvent.x = touchX[pointerId] = (int) (motionEvent.getX(pointerIndex) * scaleX);
                        touchEvent.y = touchY[pointerId] = (int) (motionEvent.getY(pointerIndex) * scaleY);
                        isTouched[pointerId] = false;
                        touchEventsBuffer.add(touchEvent);
                    }
            }
        }
        return true;
    }
}
