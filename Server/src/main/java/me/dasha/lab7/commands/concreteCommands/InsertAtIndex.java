package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.dataBase.DataBaseRequests;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.managers.CollectionManager;

import java.util.Objects;

/**
 * this class adds b new element to b given position
 */
public class InsertAtIndex extends Command {
    private final CollectionManager collectionManager;

    public InsertAtIndex(CollectionManager collectionManager) {
        super("insert_at_index", "insert_at_index {element}: добавить новый элемент в заданную позицию");
        this.collectionManager = collectionManager;
    }
    /**
     * execute command
     * @param request - command
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentExp {
        if (request.getArgs().isBlank()) throw new IllegalArgumentExp();

        Integer index;
        index = Integer.parseInt(request.getArgs());

        if (CollectionManager.stackIsEmpty() && index != 0) {
            return new Response(ResponseStatus.ERROR, "Не удается выполнить команду. Коллекция пуста");
        }
        try {
            if (CollectionManager.checkStackSize(index, request.getUser().getUsername())) {
                if (Objects.isNull(request.getObject())) {
                    return new Response(ResponseStatus.ASK_OBJECT, "Требуется объект");
                }
                else {
                    DataBaseHandler.addObject(request.getObject(), request.getUser());
                    CollectionManager.insertAt(index, request.getObject());
                    return new Response(ResponseStatus.OK, "Элемент добавлен в коллекцию");
                }
            }
            return new Response(ResponseStatus.ERROR, "Недопустимый индекс/у вас нет прав на данный элемент");
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.ERROR,"Команда не выполнена. Вы ввели некорректный аргумент");
        }
    }
}

