package com.lucasgiancola;

public class Constants {
    public static final short CATEGORY_BALL = 0x001;
    public static final short CATEGORY_BLOCK = 0x002;
    public static final short CATEGORY_WALL = 0x004;
    public static final short CATEGORY_DESTROYER = 0x004;

    public static final short MASK_BALL = Constants.CATEGORY_BLOCK | Constants.CATEGORY_WALL | Constants.CATEGORY_DESTROYER;
    public static final short MASK_BLOCK = Constants.CATEGORY_BALL | Constants.CATEGORY_DESTROYER;
    public static final short MASK_WALL = Constants.CATEGORY_BALL;
    public static final short MASK_DESTROYER = Constants.CATEGORY_BLOCK | Constants.CATEGORY_BALL;

    public static final float PIXELS_PER_METER = 100;

    public static float pixelsToBox(float pix) {
        return pix / PIXELS_PER_METER;
    }

    public static float boxToPixels(float box) {
        return PIXELS_PER_METER * box;
    }
}
