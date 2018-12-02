package com.cuupa.classificator.services;

public class TextAndPosition {
    private String value = "";

    private float fromX;

    private float toX;

    private float y;

    private float maxheight;


    public void add(String value, float xDirAdj, float width, float yDirAdj, float heightDir) {
        this.value += value;

        if (xDirAdj < fromX || fromX == 0f) {
            fromX = xDirAdj;
        }

        if (xDirAdj > toX) {
            toX = xDirAdj + width;
        }

        y = yDirAdj;

        maxheight = heightDir;
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
        return maxheight;
    }

    public String getValue() {
        return value;
    }
}
