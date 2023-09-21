package me.dasha.lab7.managers;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.exp.*;
import me.dasha.lab7.utility.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * this class stores the registered commands and calls them
 */
public class CommandManager {
    private final DataBaseHandler dataBaseHandler;
    private HashMap<String, Command> commands = new HashMap<>();
    static final Logger commandManagerLogger = Logger.getLogger(CommandManager.class.getName());

    public CommandManager(DataBaseHandler dataBaseHandler) {
        this.dataBaseHandler = dataBaseHandler;
    }
    /**
     * add command
     * @param commands
     */
    public void addCommand(Collection<Command> commands) {
        this.commands.putAll(commands.stream()
                .collect(Collectors.toMap(Command::getName, с -> с)));
    }
    public Collection<Command> getCommands() {
        return commands.values();
    }
    /**
     * command input
     * @param request
     */


    public Response executeCommand(Request request) throws IllegalArgumentExp, NoSuchCommandExp, CommandRuntimeExp, ExitExp {
        Command command = commands.get(request.getCommandName());
        if (command == null) {
            commandManagerLogger.info("Команда отсутствует");
            throw new NoSuchCommandExp();
        }
        Response response = command.execute(request);
        return response;
    }

}
