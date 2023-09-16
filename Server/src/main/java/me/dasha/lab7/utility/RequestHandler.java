package me.dasha.lab7.utility;

import me.dasha.lab7.managers.CommandManager;
import me.dasha.lab7.exp.*;
import me.dasha.lab7.managers.ConnectionManagerPool;

import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<ConnectionManagerPool> { //обработка полученных команд
    private CommandManager commandManager;
    private Request request;
    private ObjectOutputStream objectOutputStream;

    public RequestHandler(CommandManager commandManager, Request request, ObjectOutputStream objectOutputStream) {
        this.commandManager = commandManager;
        this.request = request;
        this.objectOutputStream = objectOutputStream;
    }
    @Override
    public ConnectionManagerPool call() {
        try {
            return new ConnectionManagerPool(commandManager.executeCommand(request), objectOutputStream);
        } catch (IllegalArgumentExp e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.WRONG_ARGUMENTS, "Неверное использование аргументов команды"), objectOutputStream);
        } catch (CommandRuntimeExp e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.ERROR, "Ошибка при исполнении программы"), objectOutputStream);
        } catch (NoSuchCommandExp e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.ERROR, "Такой команды нет в списке\nДля получения справки о доступных командах введите help"), objectOutputStream);
        } catch (ExitExp e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.EXIT), objectOutputStream);
        }
    }

}