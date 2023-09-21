package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;

/**
 * this class ends the program
 */
public class Exit extends Command {

    public Exit() {
        super("exit", "exit: завершить программу (без сохранения в файл)");
    }
    /**
     * execute command
     * @param request - command
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentExp{
        if (!request.getArgs().isBlank()) throw new IllegalArgumentExp();
        return new Response(ResponseStatus.EXIT);
    }
}