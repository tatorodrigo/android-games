package br.com.tattobr.superjumper.screens;

import br.com.tattobr.samples.framework.Font;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.superjumper.utils.AssetsUtil;
import br.com.tattobr.superjumper.utils.DimensionUtil;

public class HelpScreen extends HelpBaseScreen {
    private final String text1;
    private final String text2;

    public HelpScreen(Game game) {
        super(game);
        text1 = "GET BOB";
        text2 = "TO THE TOP";
    }

    @Override
    public Screen getNextScreen() {
        return new HelpScreenStepTwo(game);
    }

    @Override
    public void presentItems(SpriteBatcher spriteBatcher, float deltaTime) {
        Font font = AssetsUtil.font;
        font.drawText(
                spriteBatcher,
                text1,
                DimensionUtil.GAME_WIDTH * .5f - text1.length() * font.getBiggestCharWidth() * .5f,
                DimensionUtil.GAME_HEIGHT * .95f,
                2f, 2f
        );

        font.drawText(
                spriteBatcher,
                text2,
                DimensionUtil.GAME_WIDTH * .5f - text2.length() * font.getBiggestCharWidth() * .5f,
                DimensionUtil.GAME_HEIGHT * .95f - font.getBiggestCharHeight() * 2,
                2f, 2f
        );
    }
}
