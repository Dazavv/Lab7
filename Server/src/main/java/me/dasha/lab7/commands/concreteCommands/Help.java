package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.managers.CommandManager;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;

/**
 * this class output command information
 */
public class Help extends Command {
    private final CommandManager commandManager;

    public Help(CommandManager commandManager) {
        super("help", "help: вывести список всех команд");
        this.commandManager = commandManager;
    }

    /**
     * execute command
     * @param request - command
     */
    public Response execute(Request request) throws IllegalArgumentExp {
        if (!request.getArgs().isBlank()) throw new IllegalArgumentExp();
        return new Response(ResponseStatus.OK, """
                add {element}: добавить новый элемент в коллекцию
                add_if_min {element}: добавить новый элемент в коллекцию, если его значение здоровья меньше, чем у наименьшего элемента этой коллекции
                clear: Очистить коллекцию
                info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)
                insert_at_index {element}: добавить новый элемент в заданную позицию
                group_by_coordinates: сгруппировать по координатам
                execute_script: выполнить скрипт
                show: вывести все элементы коллекции
                remove_by_id id: удалить элемент из коллекции по его id
                update id {element}: обновить значение элемента коллекции, id которого равен заданному
                exit: завершить программу
                remove_any_by_achievements achievements: удалить из коллекции один элемент, значение поля achievements которого эквивалентно заданному
                help: вывести список всех команд
                """);
    }
}
