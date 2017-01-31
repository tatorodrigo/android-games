package br.com.tattobr.samples.framework.impl;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.tattobr.samples.framework.Font;
import br.com.tattobr.samples.framework.Game;
import br.com.tattobr.samples.framework.gl.SpriteBatcher;
import br.com.tattobr.samples.framework.gl.Texture;
import br.com.tattobr.samples.framework.gl.TextureRegion;

public class BMFont implements Font {
    private static final String ns = null;
    private List<FontChar> chars;
    private Texture texture;
    private float offsetX;
    private float offsetY;

    public BMFont(Game game, String configurationFileName, Texture texture, float offsetX, float offsetY) {
        try {
            chars = new ArrayList<>();
            this.texture = texture;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            parseXML(game.getFileIO().readAsset(configurationFileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void parseXML(InputStream in) {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            readFont(parser);
            Collections.sort(chars, new FontCharComparator());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readFont(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "font");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("chars")) {
                readChars(parser);
            } else {
                skip(parser);
            }
        }
    }

    private void readChars(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "chars");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("char")) {
                chars.add(readChar(parser));
            } else {
                skip(parser);
            }
        }
    }

    private FontChar readChar(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "char");
        int count = parser.getAttributeCount();
        String attr;
        FontChar fontChar = new FontChar();
        for (int i = 0; i < count; i++) {
            attr = parser.getAttributeName(i);
            if ("id".equalsIgnoreCase(attr)) {
                fontChar.charId = (char) Integer.parseInt(parser.getAttributeValue(i));
            } else if ("x".equalsIgnoreCase(attr)) {
                fontChar.x = (char) Integer.parseInt(parser.getAttributeValue(i));
            } else if ("y".equalsIgnoreCase(attr)) {
                fontChar.y = (char) Integer.parseInt(parser.getAttributeValue(i));
            } else if ("width".equalsIgnoreCase(attr)) {
                fontChar.width = (char) Integer.parseInt(parser.getAttributeValue(i));
            } else if ("height".equalsIgnoreCase(attr)) {
                fontChar.height = (char) Integer.parseInt(parser.getAttributeValue(i));
            } else if ("xoffset".equalsIgnoreCase(attr)) {
                fontChar.offsetX = (char) Integer.parseInt(parser.getAttributeValue(i));
            } else if ("yoffset".equalsIgnoreCase(attr)) {
                fontChar.offsetY = (char) Integer.parseInt(parser.getAttributeValue(i));
            } else if ("xadvance".equalsIgnoreCase(attr)) {
                fontChar.advanceX = (char) Integer.parseInt(parser.getAttributeValue(i));
            }
        }
        fontChar.textureRegion = new TextureRegion(
                texture, offsetX + fontChar.x, offsetY + fontChar.y,
                fontChar.width, fontChar.height
        );
        parser.nextTag();
        return fontChar;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    @Override
    public void drawText(SpriteBatcher batcher, String text, float x, float y) {
        drawText(batcher, text, x, y, 1, 1);
    }

    @Override
    public void drawText(SpriteBatcher batcher, String text, float x, float y, float scaleX, float scaleY) {
        int length = text != null ? text.length() : 0;
        FontChar fontChar;
        for (int i = 0; i < length; i++) {
            char charAt = text.charAt(i);
            int c = charAt - ' ';
            fontChar = null;
            if (c < 0) {
                continue;
            }
            if (c > chars.size() || chars.get(c).charId != charAt) {
                for (FontChar font : chars) {
                    if (charAt == font.charId) {
                        fontChar = font;
                        break;
                    }
                }
                if (fontChar == null) {
                    continue;
                }
            }
            if (fontChar == null) {
                fontChar = chars.get(c);
            }
            batcher.drawSprite(
                    x, y,
                    fontChar.width * scaleX, fontChar.height * scaleY,
                    fontChar.textureRegion
            );
            x += fontChar.width * scaleX;
        }
    }
}
