package br.com.tattobr.superjumper.screens;

import br.com.tattobr.samples.framework.Font;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.Screen;
import br.com.tattobr.samples.framework.gl.Animation;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.superjumper.utils.AssetsUtil;
import br.com.tattobr.superjumper.utils.DimensionUtil;

public class HelpScreenStepTwo extends HelpBaseScreen {
    private final String text1;
    private final String text2;
    private final String text3;
    private float elapsedTime;

    public HelpScreenStepTwo(Game game) {
        super(game);
        elapsedTime = 0;
        text1 = "BEWARE OF";
        text2 = "FLYING KILLER";
        text3 = "SQUIRRELS!";
    }

    @Override
    public Screen getNextScreen() {
        return new HelpScreenStepThree(game);
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

        font.drawText(
                spriteBatcher,
                text3,
                DimensionUtil.GAME_WIDTH * .5f - text3.length() * font.getBiggestCharWidth() * .25f,
                DimensionUtil.GAME_HEIGHT * .2f - font.getBiggestCharHeight() * 2
        );

        spriteBatcher.drawSprite(
                DimensionUtil.GAME_WIDTH * .5f - DimensionUtil.SQUIRREL_WIDTH * 2, DimensionUtil.GAME_HEIGHT * .73f,
                DimensionUtil.SQUIRREL_WIDTH, DimensionUtil.SQUIRREL_HEIGHT,
                AssetsUtil.squirrel.getKeyFrame(elapsedTime, Animation.ANIMATION_LOOPING)
        );
        elapsedTime += deltaTime;
    }
}
