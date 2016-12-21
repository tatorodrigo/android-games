package br.com.tattobr.samples.framework.impl;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import br.com.tattobr.samples.framework.Audio;
import br.com.tattobr.samples.framework.FileIO;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Graphics;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.mrnom.utils.AssetsUtil;

public abstract class AndroidGame extends Activity implements Game {
    private AndroidFastRenderView renderView;
    private Graphics graphics;
    private Audio audio;
    private Input input;
    private FileIO fileIO;
    private Screen screen;
    private Bitmap frameBuffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        AssetsUtil.GRAPHICS_RESOLUTION = 2.6f;
        AssetsUtil.GRAPHICS_SUFIX = "_hd";

        //AssetsUtil.GRAPHICS_RESOLUTION = 1f;
        //AssetsUtil.GRAPHICS_SUFIX = "";

        final int GAME_WIDTH = (int) (480 * AssetsUtil.GRAPHICS_RESOLUTION);
        final int GAME_HEIGHT = (int) (320 * AssetsUtil.GRAPHICS_RESOLUTION);

        Resources resources = getResources();
        boolean isLandscape = resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
        int frameBufferWidth = isLandscape ? GAME_WIDTH : GAME_HEIGHT;
        int frameBufferHeight = isLandscape ? GAME_HEIGHT : GAME_WIDTH;

        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);

        frameBuffer = Bitmap.createBitmap(frameBufferWidth, frameBufferHeight, Bitmap.Config.RGB_565);
        float scaleX = (float) frameBufferWidth / size.x;
        float scaleY = (float) frameBufferHeight / size.y;

        renderView = new AndroidFastRenderView(this);
        graphics = new AndroidGraphics(getAssets(), frameBuffer);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, renderView, scaleX, scaleY);
        screen = getStartScreen();

        setContentView(renderView);
    }

    @Override
    protected void onResume() {
        super.onResume();

        screen.resume();
        renderView.resume(frameBuffer);
    }

    @Override
    protected void onPause() {
        super.onPause();

        renderView.pause();
        screen.pause();

        if (isFinishing()) {
            screen.dispose();
        }
    }

    @Override
    public Input getInput() {
        return input;
    }

    @Override
    public FileIO getFileIO() {
        return fileIO;
    }

    @Override
    public Graphics getGraphics() {
        return graphics;
    }

    @Override
    public Audio getAudio() {
        return audio;
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null) {
            throw new IllegalArgumentException("Screen muds not be null");
        }

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    @Override
    public Screen getCurrentScreen() {
        return screen;
    }
}
