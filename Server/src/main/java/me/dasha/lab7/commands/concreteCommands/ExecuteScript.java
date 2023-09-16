package me.dasha.lab7.commands.concreteCommands;


import me.dasha.lab7.commands.Command;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
/**
 * this class reads and executes b script from b specified file
 */
public class ExecuteScript extends Command {


    public ExecuteScript() {
        super("execute_script", "execute_script: выполнить скрипт");
    }



    /**
     * execute command
     * @param request - command
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentExp {
        if (request.getArgs().isBlank()) throw new IllegalArgumentExp();
        return new Response(ResponseStatus.EXECUTE_SCRIPT, request.getArgs());
    }
}