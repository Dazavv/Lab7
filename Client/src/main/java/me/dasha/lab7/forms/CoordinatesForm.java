package me.dasha.lab7.forms;

import me.dasha.lab7.collectionClasses.Coordinates;
import me.dasha.lab7.console.*;
import me.dasha.lab7.exp.FileModeExp;
import me.dasha.lab7.utility.ExecuteFileManager;


/**
 * Форма для координат
 */
public class CoordinatesForm extends Form<Coordinates> {
    private final ReaderWriter console;
    private final UserInput scanner;

    public CoordinatesForm(ReaderWriter console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     * Сконструировать новый элемент класса {@link Coordinates}
     *
     * @return объект класса {@link Coordinates}
     */
    @Override
    public Coordinates build() {
        return new Coordinates(readX(), readY()
        );
    }

    public Integer readX() {
        while (true) {
            console.write("Введите координату X: ");
            String input = scanner.nextLine().trim();
            try {
                int x = Integer.parseInt(input);
                if (x <= -623) {
                    console.printError("Значение поля должно быть больше -623");
                }
                if (input.isEmpty()) {
                    console.printError("Введите число, а не пустую строку");
                } else {
                    return x;
                }
            } catch (NumberFormatException exception) {
                console.printError("Число введено неверно");
                if (Console.isFileMode()) throw new FileModeExp();
            }
        }
    }

    /**
     * checks if the entered coordinate is correct, whether a number is entered or not
     *
     * @return Y
     */

    public Double readY() {
        while (true) {
            console.write("Введите координату Y: ");
            String input = scanner.nextLine().trim();
            try {
                double y = Double.parseDouble(input);
                if (y <= -347) {
                    console.printError("Значение поля должно быть больше -347");
                }
                if (input.isEmpty()) {
                    console.printError("Введите число, а не пустую строку");
                } else {
                    return y;
                }
            } catch (NumberFormatException exception) {
                console.printError("Число введено неверно");
                if (Console.isFileMode()) throw new FileModeExp();
            }
        }
    }
}

