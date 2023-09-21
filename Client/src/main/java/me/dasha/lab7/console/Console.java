package me.dasha.lab7.console;

import me.dasha.lab7.utility.ConsoleColors;

import java.util.Scanner;

public class Console implements ReaderWriter {
private static boolean fileMode = false;

    public Console() {

    }

    public static boolean isFileMode() {
        return fileMode;
    }

    public static void setFileMode(boolean fileMode) {
        Console.fileMode = fileMode;
    }

    @Override
    public Integer readInteger() {
        Scanner scanner = new Scanner(System.in);
        Integer number = Integer.valueOf(scanner.nextLine().trim());
        return number;
    }

    @Override
    public String readLine() {
        Scanner scanner = new Scanner(System.in);
        String text = scanner.nextLine().trim();
        return text;
    }

    @Override
    public void write(String text) {
        System.out.print(text);
    }

    @Override
    public void write(String text, ConsoleColors consoleColors) {
        System.out.println(ConsoleColors.toColor(text, consoleColors));
    }
    @Override
    public void printError(String text) {
        System.out.println(ConsoleColors.RED + text + ConsoleColors.RESET);
    }

    @Override
    public String getValidatedValue(String message) {
        write(message);
        while (true) {
            String userPrint = readLine();
            if (!userPrint.isEmpty() && !userPrint.isBlank()) {
                return userPrint;
            }
        }

    }
}