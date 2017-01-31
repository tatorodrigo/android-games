package br.com.tattobr.samples.framework.impl;

import java.util.Comparator;

public class FontCharComparator implements Comparator<FontChar> {
    @Override
    public int compare(FontChar fontChar1, FontChar fontChar2) {
        return fontChar1.charId - fontChar2.charId;
    }
}
