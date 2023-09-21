package me.dasha.lab7.forms;


import me.dasha.lab7.console.*;
import me.dasha.lab7.exp.FileModeExp;
import me.dasha.lab7.utility.ExecuteFileManager;


public class HealthForm extends Form<Double>{
    private final ReaderWriter console;
    private final UserInput scanner;
    public HealthForm(ReaderWriter console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }
    @Override
    public Double build() {
        while (true) {
            console.write("Введите уровень здоровья бойца:");
            String input = scanner.nextLine().trim();
            try {
                Double health = Double.parseDouble(input);
                if (health <= 0) {
                    console.printError("Значение поля должно быть больше 0");
                }
                if (input.isEmpty()) {
                    console.printError("Введите число, а не пустую строку");
                } else {
                    return health;
                }
            } catch (NumberFormatException exception) {
                console.printError("Число введено неверно");
                if (Console.isFileMode()) throw new FileModeExp();
            }
        }
    }
}
