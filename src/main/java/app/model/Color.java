package app.model;

public enum Color {
    RED("red"),
    ORANGE("orange"),
    AMBER("amber"),
    YELLOW("yellow"),
    LIME("lime"),
    GREEN("green"),
    EMERALD("emerald"),
    TEAL("teal"),
    CYAN("cyan"),
    SKY("sky"),
    BLUE("blue"),
    INDIGO("indigo"),
    VIOLET("violet"),
    PURPLE("purple"),
    FUCHSIA("fuchsia"),
    PINK("pink"),
    ROSE("rose"),
    SLATE("slate"),
    GRAY("gray"),
    ZINC("zinc"),
    NEUTRAL("neutral"),
    STONE("stone"),
    BROWN("brown");

    private final String value;

    Color(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Color fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (Color color : Color.values()) {
            if (color.value.equalsIgnoreCase(value)) {
                return color;
            }
        }
        throw new IllegalArgumentException("Invalid color value: " + value);
    }
}
