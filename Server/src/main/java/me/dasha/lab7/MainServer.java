package me.dasha.lab7;

import me.dasha.lab7.managers.CommandManager;
import me.dasha.lab7.commands.concreteCommands.*;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.managers.CollectionManager;
import me.dasha.lab7.utility.*;

import java.util.List;
import java.util.logging.Logger;


public class MainServer {
    public static int port = 6090;
    public static final int connection_timeout = 60 * 500;
    public static Logger mainServerLogger = Logger.getLogger(MainServer.class.getName());

    public static void main(String[] args) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не найден!!!");;
            e.printStackTrace();
            System.exit(-1);
        }

        DataBaseHandler dataBaseHandler = new DataBaseHandler();
        CommandManager commandManager = new CommandManager(dataBaseHandler);
        CollectionManager collectionManager = new CollectionManager();
        commandManager.addCommand(List.of(
                new Add(collectionManager),
                new AddIfMin(collectionManager),
                new Clear(collectionManager),
                new ExecuteScript(),
                new Exit(),
                new InsertAtIndex(collectionManager),
                new Sort(collectionManager),
                new RemoveByAchievements(collectionManager),
                new Help(commandManager),
                new Info(collectionManager),
                new RemoveByID(collectionManager),
                new GroupByCoordinates(collectionManager),
                new Show(collectionManager),
                new Update(collectionManager),
                new LoginUser(dataBaseHandler),
                new RegisterUser(dataBaseHandler)
        ));

        Server server = new Server(port, connection_timeout, dataBaseHandler, commandManager);
        server.runServer();
    }
}
