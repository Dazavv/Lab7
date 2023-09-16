package me.dasha.lab7.console;

import me.dasha.lab7.utility.ScannerManager;

import java.util.Scanner;

/**
 * Класс для стандартного ввода через консоль
 */
public class ConsoleInput implements UserInput{
    private static final Scanner userScanner = ScannerManager.getUserScanner();
@Override
    public String nextLine() {
        return userScanner.nextLine();
    }
}
