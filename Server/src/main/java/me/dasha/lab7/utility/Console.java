package me.dasha.lab7.utility;

public class Console implements Writer {
    @Override
    public void println(String str) {
        System.out.println(str);
    }

    @Override
    public void print(String str) {
        System.out.print(str);
    }

    @Override
    public void printError(String str) {
        System.out.println(str);
    }
}
