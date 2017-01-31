package br.com.tattobr.samples.framework;

public abstract class GLScreen extends Screen {
    protected final GLGame glGame;
    protected final GLGraphics graphics;

    public GLScreen(Game game) {
        super(game);
        glGame = (GLGame) game;
        graphics = glGame.getGLGraphics();
    }
}
