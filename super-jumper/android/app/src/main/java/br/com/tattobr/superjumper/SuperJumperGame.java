package br.com.tattobr.superjumper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.impl.AndroidGLGame;
import br.com.tattobr.superjumper.screens.MainScreen;
import br.com.tattobr.superjumper.utils.AssetsUtil;
import br.com.tattobr.superjumper.utils.SettingsUtil;

public class SuperJumperGame extends AndroidGLGame {
    private boolean reloadAssets;

    @Override
    public Screen getStartScreen() {
        return new MainScreen(this);
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        super.onSurfaceCreated(gl10, eglConfig);
        if (reloadAssets) {
            AssetsUtil.reload();
        } else {
            SettingsUtil.load(getFileIO());
            AssetsUtil.load(this);
            reloadAssets = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AssetsUtil.pauseMusic();
    }
}
