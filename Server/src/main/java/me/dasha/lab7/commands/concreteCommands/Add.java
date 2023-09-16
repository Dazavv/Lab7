package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.managers.CollectionManager;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * this class add item to collection
 */
public class Add extends Command {
    private final CollectionManager collectionManager;
    Logger logger = Logger.getLogger(Add.class.getName());

    public Add(CollectionManager collectionManager) {
        super("add", "add {element}: добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }

    /**
     * execute command
     * @param request - command
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentExp {
        if (!request.getArgs().isBlank()) throw new IllegalArgumentExp();
        if (Objects.isNull(request.getObject())) {
            return new Response(ResponseStatus.ASK_OBJECT, "Требуется объект");
        }
        else {
            logger.info("получен объект");
            int id = DataBaseHandler.addObject(request.getObject(), request.getUser());
            if (id == -1) return new Response(ResponseStatus.ERROR, "Элемент не удалось добавить");
            request.getObject().setId(id);
            request.getObject().setOwner(request.getUser().getUsername());
            collectionManager.add(request.getObject());
            return new Response(ResponseStatus.OK, "Элемент добавлен в коллекцию");
        }
    }
}

