package br.com.tattobr.samples.mrnom.utils;

import br.com.tattobr.samples.framework.Audio;
import br.com.tattobr.samples.framework.Graphics;
import br.com.tattobr.samples.framework.Pixmap;
import br.com.tattobr.samples.framework.Sound;

public class AssetsUtil {
    public static float GRAPHICS_RESOLUTION = 1.0f;
    public static String GRAPHICS_SUFIX = "";

    public static int MAIN_MENU_LOGO_Y;
    public static int MAIN_MENU_OPTIONS_Y;
    public static int MAIN_MENU_OPTIONS_HEIGHT;
    public static int BUTTONS_SIZE;

    public static Pixmap background;
    public static Pixmap logo;
    public static Pixmap mainMenu;
    public static Pixmap buttons;
    public static Pixmap help1;
    public static Pixmap help2;
    public static Pixmap help3;
    public static Pixmap numbers;
    public static Pixmap ready;
    public static Pixmap pause;
    public static Pixmap gameOver;
    public static Pixmap headUp;
    public static Pixmap headLeft;
    public static Pixmap headDown;
    public static Pixmap headRight;
    public static Pixmap tail;
    public static Pixmap stain1;
    public static Pixmap stain2;
    public static Pixmap stain3;

    public static Sound click;
    public static Sound eat;
    public static Sound bitten;

    public static void initDefaultValues() {
        MAIN_MENU_LOGO_Y = (int) (20 * GRAPHICS_RESOLUTION);
        MAIN_MENU_OPTIONS_Y = (int) (220 * GRAPHICS_RESOLUTION);
        MAIN_MENU_OPTIONS_HEIGHT = (int) (42 * GRAPHICS_RESOLUTION);
        BUTTONS_SIZE = (int) (60 * GRAPHICS_RESOLUTION);
    }

    public static void loadGraphics(Graphics graphics) {
        String suffix = AssetsUtil.GRAPHICS_SUFIX;

        //Graphics.PixmapFormat defaultLoadFormat = Graphics.PixmapFormat.ARGB4444;
        Graphics.PixmapFormat defaultLoadFormat = Graphics.PixmapFormat.ARGB8888;

        background = graphics.newPixmap("background" + suffix + ".png", Graphics.PixmapFormat.RGB565);
        logo = graphics.newPixmap("logo" + suffix + ".png", defaultLoadFormat);
        mainMenu = graphics.newPixmap("mainmenu" + suffix + ".png", defaultLoadFormat);
        buttons = graphics.newPixmap("buttons" + suffix + ".png", defaultLoadFormat);
        help1 = graphics.newPixmap("help1" + suffix + ".png", defaultLoadFormat);
        help2 = graphics.newPixmap("help2" + suffix + ".png", defaultLoadFormat);
        help3 = graphics.newPixmap("help3" + suffix + ".png", defaultLoadFormat);
        numbers = graphics.newPixmap("numbers" + suffix + ".png", defaultLoadFormat);
        ready = graphics.newPixmap("ready" + suffix + ".png", defaultLoadFormat);
        pause = graphics.newPixmap("pause" + suffix + ".png", defaultLoadFormat);
        gameOver = graphics.newPixmap("gameover" + suffix + ".png", defaultLoadFormat);
        headUp = graphics.newPixmap("headup" + suffix + ".png", defaultLoadFormat);
        headLeft = graphics.newPixmap("headleft" + suffix + ".png", defaultLoadFormat);
        headDown = graphics.newPixmap("headdown" + suffix + ".png", defaultLoadFormat);
        headRight = graphics.newPixmap("headright" + suffix + ".png", defaultLoadFormat);
        tail = graphics.newPixmap("tail" + suffix + ".png", defaultLoadFormat);
        stain1 = graphics.newPixmap("stain1" + suffix + ".png", defaultLoadFormat);
        stain2 = graphics.newPixmap("stain2" + suffix + ".png", defaultLoadFormat);
        stain3 = graphics.newPixmap("stain3" + suffix + ".png", defaultLoadFormat);
    }

    public static void loadAudio(Audio audio) {
        click = audio.newSound("click.ogg");
        eat = audio.newSound("eat.ogg");
        bitten = audio.newSound("bitten.ogg");
    }
}
