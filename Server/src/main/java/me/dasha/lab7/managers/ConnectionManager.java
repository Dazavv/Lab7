package me.dasha.lab7.managers;

import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.utility.*;

import java.io.*;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.*;
import java.util.logging.Logger;

public class ConnectionManager implements Runnable{
    private Server server;
    private final CommandManager commandManager;
    private final DataBaseHandler dataBaseHandler;
    private static final ExecutorService requestExecutor = Executors.newFixedThreadPool(20);
    private static final ExecutorService responseExecutor = Executors.newCachedThreadPool();
    private final SocketChannel clientSocket;

    static final Logger logger = Logger.getLogger(ConnectionManager.class.getName());

    public ConnectionManager(CommandManager commandManager, SocketChannel clientSocket, DataBaseHandler dataBaseHandler) {
        this.commandManager = commandManager;
        this.clientSocket = clientSocket;
        this.dataBaseHandler = dataBaseHandler;
    }


    @Override
    public void run(){
        Request userRequest = null;
        Response responseToUser = null;
        try {
            ObjectInputStream clientReader = new ObjectInputStream(clientSocket.socket().getInputStream());
            ObjectOutputStream clientWriter = new ObjectOutputStream(clientSocket.socket().getOutputStream());
            while (true){
                userRequest = (Request) clientReader.readObject();
                logger.info("Получен запрос с командой " + userRequest.getCommandName());
                if(!dataBaseHandler.loginUser(userRequest.getUser())
                        && !userRequest.getCommandName().equals("register")){
                    logger.info("Юзер не одобрен");
                    responseToUser = new Response(ResponseStatus.LOGIN_FAILED, "Неверный пользователь!");
                    submitNewResponse(new ConnectionManagerPool(responseToUser, clientWriter));
                } else{
                    FutureManager.addNewFixedThreadPoolFuture(requestExecutor.submit(new RequestHandler(commandManager, userRequest, clientWriter)));
                }
            }
        } catch (ClassNotFoundException exception) {
            logger.severe("Произошла ошибка при чтении полученных данных!");
        }catch (CancellationException exception) {
            logger.warning("При обработке запроса произошла ошибка многопоточности!");
        } catch (InvalidClassException | NotSerializableException exception) {
            logger.severe("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            if (userRequest == null) {
                logger.severe("Непредвиденный разрыв соединения с клиентом!");
            } else {
                logger.info("Клиент успешно отключен от сервера!");
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void submitNewResponse(ConnectionManagerPool connectionManagerPool){
        responseExecutor.submit(() -> {
            try {
                connectionManagerPool.objectOutputStream().writeObject(connectionManagerPool.response());
                connectionManagerPool.objectOutputStream().flush();
                logger.info("Ответ отправлен успешно");
            } catch (IOException e) {
                logger.severe("Не удалось отправить ответ");
            }
        });
    }
}

