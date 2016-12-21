package br.com.tattobr.samples.mrnom.utils;

import br.com.tattobr.samples.framework.Graphics;

public class TextUtil {
    public static void drawText(Graphics graphics, String line, int x, int y) {
        int length = line.length();
        char character;
        for (int i = 0; i < length; i++) {
            character = line.charAt(i);

            if (character == ' ') {
                x += AssetsUtil.NUMBERS_SIZE;
                continue;
            }

            int srcX = 0;
            int srcWidth = 0;
            if (character == '.') {
                srcX = 10 * AssetsUtil.NUMBERS_SIZE;
                srcWidth = AssetsUtil.DOT_SIZE;
            } else {
                srcX = (character - '0') * AssetsUtil.NUMBERS_SIZE;
                srcWidth = AssetsUtil.NUMBERS_SIZE;
            }
            graphics.drawPixmap(AssetsUtil.numbers, x, y, srcX, 0, srcWidth, AssetsUtil.numbers.getHeight());
            x += srcWidth;
        }
    }
}
