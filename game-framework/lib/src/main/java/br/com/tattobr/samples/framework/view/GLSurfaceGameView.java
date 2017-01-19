package br.com.tattobr.samples.framework.view;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import br.com.tattobr.samples.framework.Pool;

public class GLSurfaceGameView extends GLSurfaceView {
    private final Object mPoolLock = new Object();
    private OnTouchListener mOuterOnTouchListener;
    private Pool<SynchronizedEvent> mSynchronizedEventPool;

    public GLSurfaceGameView(Context context) {
        super(context);
        init(context);
    }

    public GLSurfaceGameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        Pool.PoolObjectFactory<SynchronizedEvent> factory = new Pool.PoolObjectFactory<SynchronizedEvent>() {
            @Override
            public SynchronizedEvent createObject() {
                return new SynchronizedEvent();
            }
        };
        mSynchronizedEventPool = new Pool<>(factory, 100);

        final OnTouchListener onTouchListener = new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (mOuterOnTouchListener != null) {
                    SynchronizedEvent event;
                    synchronized (mPoolLock) {
                        event = mSynchronizedEventPool.newObject();
                    }
                    event.pool = mSynchronizedEventPool;
                    event.listener = mOuterOnTouchListener;
                    event.view = view;
                    event.motionEvent = motionEvent;
                    queueEvent(event);
                }
                return true;
            }
        };
        super.setOnTouchListener(onTouchListener);
    }

    @Override
    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOuterOnTouchListener = onTouchListener;
    }

    private class SynchronizedEvent implements Runnable {
        Pool<SynchronizedEvent> pool;
        OnTouchListener listener;
        View view;
        MotionEvent motionEvent;

        @Override
        public void run() {
            listener.onTouch(view, motionEvent);
            synchronized (mPoolLock) {
                pool.free(this);
            }
        }
    }
}
