package me.dasha.lab7.utility;

/**
 * Enum for colorizing output to the console
 */
public enum ConsoleColors {
    RED("\u001B[31m"),
    RESET("\u001B[0m");

    private final String title;

    ConsoleColors(String title) {
        this.title = title;
    }

    /**
     * @param s
     * @param color
     */
    public static String toColor(String s, ConsoleColors color){
        return color + s + ConsoleColors.RESET;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title;
    }
}
