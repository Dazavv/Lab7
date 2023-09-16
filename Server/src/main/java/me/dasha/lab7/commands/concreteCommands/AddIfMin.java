package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.managers.CollectionManager;

import java.util.Objects;

/**
 * this class adds b new element if its value is less than the smallest element of this collection
 */
public class AddIfMin extends Command {
    private final CollectionManager collectionManager;

    public AddIfMin(CollectionManager collectionManager) {
        super("add_if_min", "add_if_min {element}: добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции");
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
            return new Response(ResponseStatus.ASK_OBJECT, "Требуется элемент");
        } else {
            if (CollectionManager.stackIsEmpty()) {
                int id = DataBaseHandler.addObject(request.getObject(), request.getUser());
                if (id == -1) return new Response(ResponseStatus.ERROR, "Элемент не удалось добавить");
                request.getObject().setId(id);
                request.getObject().setOwner(request.getUser().getUsername());
                collectionManager.add(request.getObject());
                return new Response(ResponseStatus.OK, "Объект добавлен");
            }
            int id = DataBaseHandler.addObjectIfMin(request.getObject(), request.getUser());
            if (id == -1) return new Response(ResponseStatus.ERROR, "Элемент не удалось добавить");
            request.getObject().setId(id);
            request.getObject().setOwner(request.getUser().getUsername());
            collectionManager.add(request.getObject());
            return new Response(ResponseStatus.OK, "Объект добавлен");
        }
    }
}