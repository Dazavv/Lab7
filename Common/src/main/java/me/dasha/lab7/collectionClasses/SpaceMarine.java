package me.dasha.lab7.collectionClasses;

import me.dasha.lab7.utility.Validator;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
/**
 * this class SpaceMarine in  collection
 */
public class SpaceMarine implements Comparable<SpaceMarine>, Validator, Serializable {

    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой

    private Coordinates coordinates; //Поле не может быть null

    private LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    private double health; //Значение поля должно быть больше 0

    private String achievements; //Поле может быть null

    private Weapon weaponType; //Поле может быть null

    private MeleeWeapon meleeWeapon; //Поле может быть null

    private Chapter chapter; //Поле не может быть null
    private String owner;
    public SpaceMarine(String name, Coordinates coordinates, double health, String achievements, Weapon weaponType, MeleeWeapon meleeWeapon, Chapter chapter) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.health = health;
        this.achievements = achievements;
        this.weaponType = weaponType;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
    }
    public SpaceMarine(String name, Coordinates coordinates, double health, String achievements, Weapon weaponType, MeleeWeapon meleeWeapon, Chapter chapter, String owner) {
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.health = health;
        this.achievements = achievements;
        this.weaponType = weaponType;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
        this.owner = owner;
    }
    public SpaceMarine(Integer id, String name, Coordinates coordinates, LocalDateTime creationDate, double health, String achievements, Weapon weaponType, MeleeWeapon meleeWeapon, Chapter chapter, String owner) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.health = health;
        this.achievements = achievements;
        this.weaponType = weaponType;
        this.meleeWeapon = meleeWeapon;
        this.chapter = chapter;
        this.owner = owner;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Chapter getChapter() {
        return chapter;
    }

    public void setChapter(Chapter chapter) {
        this.chapter = chapter;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public MeleeWeapon getMeleeWeapon() {
        return meleeWeapon;
    }

    public void setMeleeWeapon(MeleeWeapon meleeWeapon) {
        this.meleeWeapon = meleeWeapon;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public Weapon getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(Weapon weaponType) {
        this.weaponType = weaponType;
    }

    @Override
    public boolean validate() {
        if (this.name == null ||
                this.name.isEmpty() ||
                this.health <= 0 ||
                !this.coordinates.validate() ||
                !this.chapter.validate()) {
            return false;
        } else {
            return true;
        }
    }
    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
    @Override
    public int compareTo(SpaceMarine sm) {
        return (this.id - sm.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpaceMarine product = (SpaceMarine) o;
        return Objects.equals(coordinates, product.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }

    @Override
    public String toString() {
        return  "-----------------------------------\n"
                + "ID элемента коллекции: " + id
                + "\nИмя: " + name
                + "\nКоордината X : " + coordinates.getX()
                + "\nКоордината Y : " + coordinates.getY()
                + "\nДата создания: " + creationDate
                + "\nЗдоровье: " + health
                + "\nДостижения: " + achievements
                + "\nТип оружия: " + weaponType
                + "\nОружие ближнего боя: " + meleeWeapon
                + "\nГлава: " + chapter.getName()
                + "\nРодительский легион: " + chapter.getParentLegion()
                + "\nМир: " + chapter.getWorld()
                + "\n-----------------------------------\n";
    }


}
