package br.com.tattobr.openglestests.utils;

import android.util.Log;

/**
 * Created by tattobr on 06/01/2017.
 */

public class FPSUtil {
    private final String TAG = FPSUtil.class.getSimpleName();

    private int frames;
    private long startTime;

    public FPSUtil() {
        frames = 0;
        startTime = System.nanoTime();
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
}
