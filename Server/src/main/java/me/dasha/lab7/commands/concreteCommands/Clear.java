package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.managers.CollectionManager;
import org.postgresql.util.PSQLException;

/**
 * this class clear the collection
 */
public class Clear extends Command {
    private final CollectionManager collectionManager;

    public Clear(CollectionManager collectionManager) {
        super("clear", "clear: Очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * execute command
     *
     * @param request - command
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentExp {
        if (!request.getArgs().isBlank()) throw new IllegalArgumentExp();
        if (CollectionManager.stackIsEmpty()) {
            return new Response(ResponseStatus.ERROR, "Не удается выполнить команду. Коллекция пуста");
        }
        if(DataBaseHandler.clear(request.getUser())) {
            CollectionManager.clear();
            return new Response(ResponseStatus.OK, "Коллекция успешно очищена");
        }
        return new Response(ResponseStatus.ERROR, "Коллекция не очищена");
        }
}