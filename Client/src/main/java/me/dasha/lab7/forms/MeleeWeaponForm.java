package me.dasha.lab7.forms;

import me.dasha.lab7.console.*;
import me.dasha.lab7.collectionClasses.MeleeWeapon;
import me.dasha.lab7.exp.FileModeExp;
import me.dasha.lab7.utility.ExecuteFileManager;

import java.util.Arrays;
import java.util.Locale;

public class MeleeWeaponForm extends Form<MeleeWeapon>{
    private final ReaderWriter console;
    private final UserInput scanner;
    public MeleeWeaponForm(ReaderWriter console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }
    @Override
    public MeleeWeapon build() {
        console.write("Выберите одно оружие ближнего боя: " + Arrays.toString(MeleeWeapon.values()) + "\n");
        while (true){
            String input = scanner.nextLine().trim();
            if (input.equals("") || input.equalsIgnoreCase("null") || input.isBlank()) {
                return null;
            }
            try{
                return MeleeWeapon.valueOf(input.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException exception){
                console.printError("Такого оружия нет в списке. Попробуйте снова");
                if (Console.isFileMode()) throw new FileModeExp();
            }
        }
    }

}
