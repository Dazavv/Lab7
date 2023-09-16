package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.managers.CollectionManager;

import java.util.Map;

/**
 * this class groups the elements by coordinates value and prints the number of elements in each group
 */
public class GroupByCoordinates extends Command {
    private final CollectionManager collectionManager;

    public GroupByCoordinates(CollectionManager collectionManager) {
        super("group_by_coordinates", "group_by_coordinates: сгруппировать по координатам");
        this.collectionManager = collectionManager;
    }

    /**
     * execute command
     * @param request - command
     */
    @Override
    public Response execute(Request request) throws IllegalArgumentExp {
        if (!request.getArgs().isBlank()) throw new IllegalArgumentExp();
        if (CollectionManager.stackIsEmpty()) {
            return new Response(ResponseStatus.ERROR, "Не удается выполнить команду. Коллекция пуста");
        }
        Map<String, Integer> groupCountMap = CollectionManager.groupCountingByCoordinates();
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Integer> entry : groupCountMap.entrySet()) {
            result.append(entry.getKey()).append(", количество - ").append(entry.getValue()).append('\n');
        }
        return new Response(ResponseStatus.OK, result.toString());
    }
}
