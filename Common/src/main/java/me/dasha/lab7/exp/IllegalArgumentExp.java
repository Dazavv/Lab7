package me.dasha.lab7.exp;

import java.io.IOException;

/**
 * Exception class for invalid command arguments
 */
public class IllegalArgumentExp extends IOException {

    public IllegalArgumentExp(){}

    public IllegalArgumentExp(String str){
        super(str);
    }
}
