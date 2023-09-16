package me.dasha.lab7.commands;

import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;

import java.util.logging.Logger;

/**
 * this abstract class for creation commands
 */
public abstract class Command {
    private final String name;
    private final String info;
    public Logger commandLogger = Logger.getLogger(this.getClass().getName());
    public Command(String name, String info) {
        this.name = name;
        this.info = info;
    }
    /**
     * output information about command
     */

    public String getName() {
        return name;
    }

    public String getInfo() {
        return info;
    }

    /**
     * execute command
     * @param request - command
     */


    public abstract Response execute(Request request) throws IllegalArgumentExp;

}