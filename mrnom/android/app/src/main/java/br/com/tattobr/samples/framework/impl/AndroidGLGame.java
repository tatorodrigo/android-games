package br.com.tattobr.samples.framework.impl;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.Audio;
import br.com.tattobr.samples.framework.FileIO;
import br.com.tattobr.samples.framework.GLGraphics;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Graphics;
import br.com.tattobr.samples.framework.Input;
import br.com.tattobr.samples.framework.Screen;

public abstract class AndroidGLGame extends Activity implements Game, GLSurfaceView.Renderer {
    enum GLGameState {
        Initialized, Running, Paused, Finished, Idle
    }

    private GLSurfaceView glView;
    private GLGraphics graphics;
    private Audio audio;
    private Input input;
    private FileIO fileIO;
    private Screen screen;
    private GLGameState state = GLGameState.Initialized;
    private final Object stateChanged = new Object();
    long startTime = System.nanoTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                        WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        glView = new GLSurfaceView(this);
        glView.setRenderer(this);

        graphics = new GLGraphics(glView);
        fileIO = new AndroidFileIO(getAssets());
        audio = new AndroidAudio(this);
        input = new AndroidInput(this, glView, 1, 1);
        screen = getStartScreen();

        setContentView(glView);
    }

    @Override
    protected void onPause() {
        synchronized (stateChanged) {
            if (isFinishing()) {
                state = GLGameState.Finished;
            } else {
                state = GLGameState.Paused;
            }
            while (true) {
                try {
                    stateChanged.wait();
                    break;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            glView.onPause();
            super.onPause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        glView.onResume();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        graphics.setGl(gl10);

        synchronized (stateChanged) {
            if (GLGameState.Initialized.equals(state)) {
                screen = getStartScreen();
            }
            state = GLGameState.Running;
            screen.resume();
            startTime = System.nanoTime();
        }
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        GLGameState state;
        synchronized (stateChanged) {
            state = this.state;
        }

        if (GLGameState.Running.equals(state)) {
            float deltaTime = (System.nanoTime() - startTime) / 1000000000;
            startTime = System.nanoTime();

            screen.update(deltaTime);
            screen.present(deltaTime);
        } else if (GLGameState.Paused.equals(state)) {
            screen.pause();
            synchronized (stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
        } else if (GLGameState.Finished.equals(state)) {
            screen.pause();
            screen.dispose();
            synchronized (stateChanged) {
                this.state = GLGameState.Idle;
                stateChanged.notifyAll();
            }
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
        throw new IllegalStateException("We are using OpenGL!");
    }

    public GLGraphics getGLGraphics() {
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
