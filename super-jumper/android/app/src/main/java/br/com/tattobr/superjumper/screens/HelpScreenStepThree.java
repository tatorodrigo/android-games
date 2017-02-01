package br.com.tattobr.superjumper.screens;

import br.com.tattobr.samples.framework.Font;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.superjumper.utils.AssetsUtil;
import br.com.tattobr.superjumper.utils.DimensionUtil;

public class HelpScreenStepThree extends HelpBaseScreen {
    private final String text1;
    private final String text2;

    public HelpScreenStepThree(Game game) {
        super(game);
        text1 = "USE SPRING";
        text2 = "FOR PROFIT!";
    }

    @Override
    public Screen getNextScreen() {
        return new HelpScreenStepFour(game);
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
                DimensionUtil.GAME_WIDTH * .5f - text2.length() * font.getBiggestCharWidth() * .25f,
                DimensionUtil.GAME_HEIGHT * .2f - font.getBiggestCharHeight() * 2
        );

        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .5f, DimensionUtil.GAME_HEIGHT * .48f + AssetsUtil.spring.width * .5f,
                AssetsUtil.spring.width, AssetsUtil.spring.height,
                AssetsUtil.spring
        );
    }
}
