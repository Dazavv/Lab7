package me.dasha.lab7.forms;


import me.dasha.lab7.collectionClasses.*;
import me.dasha.lab7.console.*;
import me.dasha.lab7.collectionClasses.MeleeWeapon;
import me.dasha.lab7.utility.*;
import me.dasha.lab7.utility.ExecuteFileManager;

import java.time.LocalDate;


public class SpaceMarineForm extends Form<SpaceMarine> {
    private final ReaderWriter console;
    private final UserInput scanner;

    public SpaceMarineForm(Console console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    /**
     *
     * Construct an element of a new class {@link SpaceMarine}
     *
     * @return class object {@link SpaceMarine}
     */
    @Override
    public SpaceMarine build() {
        ReadNameManager readManager = new ReadNameManager(console);
        SpaceMarine spaceMarine = new SpaceMarine(
                        readManager.readNameCanNotBeNull("Введите имя: "),
                        readCoordinates(),
                        readHealth(),
                        readManager.readNameCanBeNull("Введите достижение: "),
                        readWeapon(),
                        readMeleeWeapon(),
                        readChapter()
                );
        return spaceMarine;
    }
    private Coordinates readCoordinates(){
        return new CoordinatesForm(console).build();
    }
    private Weapon readWeapon(){
        return new WeaponForm(console).build();
    }
    private MeleeWeapon readMeleeWeapon(){
        return new MeleeWeaponForm(console).build();
    }
    private Chapter readChapter(){
        return new ChapterForm(console).build();
    }
    private Double readHealth(){
        return new HealthForm(console).build();
    }

}
