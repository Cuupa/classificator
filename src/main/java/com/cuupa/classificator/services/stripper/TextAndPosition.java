package com.cuupa.classificator.services.stripper;

import org.apache.logging.log4j.util.Strings;

public class TextAndPosition {

    private String value = Strings.EMPTY;

    private float fromX;

    private float toX;

    private float y;

    private float maxHeight;

    public void add(String value, float xDirAdj, float width, float yDirAdj, float heightDir) {
        this.value += value;

        if (xDirAdj < fromX || fromX == 0f) {
            fromX = xDirAdj;
        }

        if (xDirAdj > toX) {
            toX = xDirAdj + width;
        }

        y = yDirAdj;

        maxHeight = heightDir;
    }

    public float getX() {
        return fromX;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return toX - fromX;
    }

    public float getHeight() {
        return maxHeight;
    }

    public String getValue() {
        return value;
    }
}
