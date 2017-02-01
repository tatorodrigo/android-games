package br.com.tattobr.superjumper.screens;

import br.com.tattobr.samples.framework.Font;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.gl.Animation;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.superjumper.utils.AssetsUtil;
import br.com.tattobr.superjumper.utils.DimensionUtil;

public class HelpScreenStepFour extends HelpBaseScreen {
    private final String text1;
    private final String text2;
    private float elapsedTime;

    public HelpScreenStepFour(Game game) {
        super(game);
        elapsedTime = 0;
        text1 = "COLLECT COINS!";
        text2 = "FOR PROFIT!";
    }

    @Override
    public Screen getNextScreen() {
        return new MainScreen(game);
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
                DimensionUtil.GAME_WIDTH * .5f, DimensionUtil.GAME_HEIGHT * .80f,
                DimensionUtil.COIN_WIDTH, DimensionUtil.COIN_HEIGHT,
                AssetsUtil.coin.getKeyFrame(elapsedTime, Animation.ANIMATION_LOOPING)
        );

        elapsedTime += deltaTime;
    }
}
