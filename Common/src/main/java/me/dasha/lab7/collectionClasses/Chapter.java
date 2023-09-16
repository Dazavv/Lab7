package me.dasha.lab7.collectionClasses;


import me.dasha.lab7.utility.Validator;

import java.io.Serializable;

/**
 * The class with spaceMarine's chapter
 */
public class Chapter implements Validator, Serializable {

    private String name; //Поле не может быть null, Строка не может быть пустой

    private String parentLegion;

    private String world; //Поле может быть null

    public Chapter(String name, String parentLegion, String world){
        this.name = name;
        this.parentLegion = parentLegion;
        this. world = world;
    }

    public Chapter() {

    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getParentLegion() {
        return parentLegion;
    }
    public void setParentLegion(String parentLegion) {
        this.parentLegion = parentLegion;
    }

    public String getWorld() {
        return world;
    }
    public void setWorld(String world) {
        this.world = world;
    }

    public boolean validate() {
        if (this.name == null || this.name.isBlank()) {
            return false;
        }
        return true;
    }
}

