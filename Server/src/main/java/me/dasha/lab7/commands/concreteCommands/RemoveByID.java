package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.exp.NoSuchIDExp;
import me.dasha.lab7.managers.CollectionManager;

import java.util.EmptyStackException;

/**
 * this class remove an item from the collection by id
 */
public class RemoveByID extends Command {
    private final CollectionManager collectionManager;

    public RemoveByID(CollectionManager collectionManager) {
        super("remove_by_id", "remove_by_id id: удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
    }
    /**
     * execute command
     * @param request - command
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentExp {
        if (request.getArgs().isBlank()) throw new IllegalArgumentExp();
        if (CollectionManager.stackIsEmpty()) {
            return new Response(ResponseStatus.ERROR, "Нечего удалять. Коллекция пустая");
        }
        try {
            Integer id = Integer.parseInt(request.getArgs());
            if (!collectionManager.checkExist(id)) throw new NoSuchIDExp();
            if (DataBaseHandler.removeById(id, request.getUser())){
                collectionManager.removeById(id);
                return new Response(ResponseStatus.OK, "Элемент удален");
            }
            return new Response(ResponseStatus.ERROR, "Элемент не удален");
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.WRONG_ARGUMENTS,"Команда не выполнена. Вы ввели некорректный аргумент");
        } catch (NoSuchIDExp e) {
            return new Response(ResponseStatus.ERROR,"Элемента с таким id нет в коллекции");
        }
    }
}