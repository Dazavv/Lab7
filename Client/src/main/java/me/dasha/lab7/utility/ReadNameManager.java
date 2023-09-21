package me.dasha.lab7.utility;

import me.dasha.lab7.console.*;
import me.dasha.lab7.exp.FileModeExp;

/**
 * The class is responsible for what the user enters
 */
public class ReadNameManager {
    private final ReaderWriter console;
    private final UserInput scanner;

    public ReadNameManager(ReaderWriter console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * method checks if the name is entered correctly, it contains only letters or not
     *
     * @return name
     */

    public String readNameCanNotBeNull(String str) {
        String name;
        while (true) {
            console.write(str);
            name = scanner.nextLine().trim();
            if (name.equals("") || !name.matches("^[a-zA-Z-А-Яа-я]*$")) {
                console.printError("Имя не может быть пустой строкой/иными знаками, кроме букв");
                if (Console.isFileMode()) throw new FileModeExp();
            } else {
                return name;
            }
        }
    }

    public String readNameCanBeNull(String str) {
        String name;
        while (true) {
            console.write(str);
            name = scanner.nextLine().trim();
            if (name.matches("^[a-zA-Z-А-Яа-я]*$")){
                return name;
            }
            if (name.equals("")) {
                return null;
            } else {
                console.printError("Имя не может быть иными знаками, кроме букв");
                if (Console.isFileMode()) throw new FileModeExp();
            }
        }
    }
}