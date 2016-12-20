package br.com.tattobr.samples.framework.impl;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AndroidFastRenderView extends SurfaceView implements Runnable {
    private AndroidGame androidGame;
    private Bitmap frameBuffer;
    private SurfaceHolder holder;
    private Thread renderThread;

    private volatile boolean running;

    public AndroidFastRenderView(Context context) {
        super(context);
        init(context);
    }

    public AndroidFastRenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AndroidFastRenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AndroidFastRenderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        if (!(context instanceof AndroidGame)) {
            throw new IllegalArgumentException("Need a AndroidGame instance.");
        }

        this.androidGame = (AndroidGame) context;
        holder = getHolder();
    }

    public void resume(Bitmap frameBuffer) {
        this.frameBuffer = frameBuffer;
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    public void pause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        final float nanoTimeToSeconds = 1000000000.0f;
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();

        while (running) {
            if (!holder.getSurface().isValid()) {
                continue;
            }
            float deltaTime = (System.nanoTime() - startTime) / nanoTimeToSeconds;

            androidGame.getCurrentScreen().update(deltaTime);
            androidGame.getCurrentScreen().present(deltaTime);

            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(frameBuffer, null, dstRect, null);
            holder.unlockCanvasAndPost(canvas);
        }
    }
}
