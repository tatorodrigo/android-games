package br.com.tattobr.superjumper.utils;

import br.com.tattobr.samples.framework.Font;
import br.com.tattobr.samples.framework.GLGame;
import br.com.tattobr.samples.framework.Music;
import br.com.tattobr.samples.framework.Sound;
import br.com.tattobr.samples.framework.gl.Animation;
import br.com.tattobr.samples.framework.gl.Texture;
import br.com.tattobr.samples.framework.gl.TextureRegion;
import br.com.tattobr.samples.framework.impl.BMFont;

public class AssetsUtil {
    public static Texture background;
    public static TextureRegion backgroundRegion;

    public static Texture items;
    public static TextureRegion mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static TextureRegion gameOver;
    public static TextureRegion highScores;
    public static TextureRegion logo;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;
    public static TextureRegion pause;
    public static TextureRegion spring;
    public static TextureRegion castle;
    public static TextureRegion platform;
    public static Animation coin;
    public static Animation squirrel;
    public static Animation brakingPlatform;
    public static Animation bobJump;
    public static Animation bobFall;
    public static TextureRegion bobHit;

    public static Font font;

    public static Music music;
    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;

    public static void load(GLGame game) {
        background = new Texture(game, "background.png");
        backgroundRegion = new TextureRegion(background, 0, 0, background.getWidth(), background.getHeight());

        items = new Texture(game, "items.png");
        mainMenu = new TextureRegion(items, 0, 448, 600, 220);
        pauseMenu = new TextureRegion(items, 448, 256, 384, 192);
        ready = new TextureRegion(items, 640, 448, 384, 64);
        gameOver = new TextureRegion(items, 704, 512, 320, 192);
        highScores = new TextureRegion(items, 0, 521.33f, 600, 73.33f);
        logo = new TextureRegion(items, 0, 704, 548, 248);
        soundOn = new TextureRegion(items, 128, 0, 128, 128);
        soundOff = new TextureRegion(items, 0, 0, 128, 128);
        arrow = new TextureRegion(items, 0, 128, 128, 128);
        pause = new TextureRegion(items, 128, 128, 128, 128);
        spring = new TextureRegion(items, 256, 0, 64, 64);
        castle = new TextureRegion(items, 256, 128, 128, 128);
        platform = new TextureRegion(items, 128, 320, 128, 32);
        coin = new Animation(
                .2f,
                new TextureRegion(items, 256, 64, 64, 64),
                new TextureRegion(items, 320, 64, 64, 64),
                new TextureRegion(items, 384, 64, 64, 64),
                new TextureRegion(items, 320, 64, 64, 64)
        );
        squirrel = new Animation(
                .2f,
                new TextureRegion(items, 0, 320, 64, 64),
                new TextureRegion(items, 64, 320, 64, 64)
        );
        brakingPlatform = new Animation(
                .2f,
                new TextureRegion(items, 128, 320, 128, 32),
                new TextureRegion(items, 128, 352, 128, 32),
                new TextureRegion(items, 128, 384, 128, 32),
                new TextureRegion(items, 128, 416, 128, 32)
        );
        bobJump = new Animation(
                .2f,
                new TextureRegion(items, 0, 256, 64, 64),
                new TextureRegion(items, 64, 256, 64, 64)
        );
        bobFall = new Animation(
                .2f,
                new TextureRegion(items, 128, 256, 64, 64),
                new TextureRegion(items, 192, 256, 64, 64)
        );
        bobHit = new TextureRegion(items, 256, 256, 64, 64);

        font = new BMFont(game, "8bit.fnt", items, 512, 0);
    }

    public static void reload() {
        background.reload();
        items.reload();
    }

    public static void playSound(Sound sound) {
        if (sound != null && SettingsUtil.soundEnabled) {
            sound.play(1);
        }
    }
}
