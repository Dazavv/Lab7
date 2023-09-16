package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.managers.CollectionManager;

import java.util.EmptyStackException;

/**
 * this class output information about command
 */
public class Info extends Command {
    private final CollectionManager collectionManager;

    public Info(CollectionManager collectionManager) {
        super("info", "info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
    }

    /**
     * execute command
     * @param request - command
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentExp {
        if (!request.getArgs().isBlank()) throw new IllegalArgumentExp();
        return new Response(ResponseStatus.OK, collectionManager.info());
    }
}