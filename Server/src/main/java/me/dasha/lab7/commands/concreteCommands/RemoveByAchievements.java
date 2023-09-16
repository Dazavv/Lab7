package me.dasha.lab7.commands.concreteCommands;


import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.exp.NoSuchAchievementsExp;
import me.dasha.lab7.managers.CollectionManager;

/**
 * this class removes from the collection one item whose achievements field value is equivalent to the given one
 */
public class RemoveByAchievements extends Command {
    private final CollectionManager collectionManager;

    public RemoveByAchievements(CollectionManager collectionManager) {
        super("remove_any_by_achievements", "remove_any_by_achievements achievements: удалить из коллекции один элемент, значение поля achievements которого эквивалентно заданному");
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
        try {
            String str = request.getArgs();
            if (!collectionManager.checkExistAchievements(str)) throw new NoSuchAchievementsExp();
            if (DataBaseHandler.removeByAchievements(str, request.getUser())){
                CollectionManager.removeAnyByAchievements(str);
                return new Response(ResponseStatus.OK, "Элемент удален");
            }
            return new Response(ResponseStatus.ERROR, "Элемент не удален");
        } catch (NoSuchAchievementsExp e) {
            return new Response(ResponseStatus.ERROR, "Элемента с таким достижением нет в коллекции");
        }
    }
}