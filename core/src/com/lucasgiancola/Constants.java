package com.lucasgiancola;

public class Constants {
    public static final float PIXELS_PER_METER = 100;

    public static float pixelsToBox(float pix) {
        return pix / PIXELS_PER_METER;
    }

    public static float boxToPixels(float box) {
        return PIXELS_PER_METER * box;
    }
}
