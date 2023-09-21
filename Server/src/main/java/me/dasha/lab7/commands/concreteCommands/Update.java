package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.exp.NoSuchIDExp;
import me.dasha.lab7.managers.CollectionManager;

import java.util.Objects;

/**
 * this class update the id of the element whose value is equal to the given one
 */
public class Update extends Command {
    private final CollectionManager collectionManager;

    public Update(CollectionManager collectionManager) {
        super("update", "update id {element}: обновить значение элемента коллекции, id которого равен заданному");
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
            return new Response(ResponseStatus.ERROR, "Не удается выполнить команду. Коллекция пуста");
        }
        Integer id;
        try {
            id = Integer.parseInt(request.getArgs().trim());
            if (!collectionManager.checkExist(id)) throw new NoSuchIDExp();
            if (CollectionManager.checkUsersId(id, request.getUser().getUsername())) {
                if (Objects.isNull(request.getObject())){
                    return new Response(ResponseStatus.ASK_OBJECT, "Для команды " + this.getName() + " требуется объект");
                }
                    if (DataBaseHandler.update(id, request.getObject(), request.getUser())){
                        collectionManager.update(request.getObject(), id);
                        return new Response(ResponseStatus.OK, "Элемент с id " + id + " обновлён");
                    }
                    return new Response(ResponseStatus.ERROR, "Элемент с id " + id + " не обновлён");
            }
            return new Response(ResponseStatus.ERROR, "У вас нет прав на этот объект");
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.WRONG_ARGUMENTS, "Команда не выполнена. Вы ввели некорректный аргумент");
        } catch (NoSuchIDExp e) {
            return new Response(ResponseStatus.ERROR, "Элемента с таким id нет в коллекции");
        }
    }
}