package me.dasha.lab7.collectionClasses;

import me.dasha.lab7.utility.Validator;

import java.io.Serializable;
import java.util.Objects;

/**
 * The class with spaceMarine's coordinates
 */
public class Coordinates implements Validator, Serializable {

    private int x; //Значение поля должно быть больше -623

    private double y; //Значение поля должно быть больше -347
    public Coordinates(int x, double y){
        this.x = x;
        this.y = y;
    }

    public Coordinates() {

    }


    public int getX() {
        return x;
    }
    public void setX(int x) {
      this.x = x;
    }

    public double getY() {
        return y;
    }
    public void setY(double y) {
        this.y = y;
    }
    @Override
    public boolean validate() {
        if (this.x < -623 || this.y <= -347) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "-----------------------------------"
                + "\nКоордината X: " + x + ", координата Y: " + y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates product = (Coordinates) o;
        return Double.compare(this.y, y) == 0 &&
                Integer.compare(this.x, x) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}

