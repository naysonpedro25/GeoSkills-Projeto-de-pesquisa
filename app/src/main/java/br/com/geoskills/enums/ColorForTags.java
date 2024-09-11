package br.com.geoskills.enums;

public enum ColorForTags {
    RED("red"),
    GREEN("green"),
    BLUE("blue"),
    ORANGE("orange"),
    YELLOW("yellow"),
    PURPLE("purple"),
    WHITE("white");

    private final String colorName;

    ColorForTags(String colorName) {
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }
}
