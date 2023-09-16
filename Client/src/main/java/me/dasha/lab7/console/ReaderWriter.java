package me.dasha.lab7.console;

import me.dasha.lab7.utility.ConsoleColors;

public interface ReaderWriter {
    Integer readInteger();
    String readLine();
    void write(String text);
    void write(String text, ConsoleColors consoleColors);
    void printError(String text);
    String getValidatedValue(String message);
}
