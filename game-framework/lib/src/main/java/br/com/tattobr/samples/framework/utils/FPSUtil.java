package br.com.tattobr.samples.framework.utils;

import android.util.Log;

public class FPSUtil {
    private final String TAG = FPSUtil.class.getSimpleName();

    private int frames;
    private long startTime;

    public FPSUtil() {
        reset();
    }

    public void logFPS() {
        frames++;
        long now = System.nanoTime();
        if (now - startTime > 1000000000) {
            Log.d(TAG, "FPS: " + frames);
            frames = 0;
            startTime = now;
        }
    }

    public void reset() {
        frames = 0;
        startTime = System.nanoTime();
    }
}
