package me.dasha.lab7.utility;

import me.dasha.lab7.managers.CommandManager;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.error.ClosingSocketExp;
import me.dasha.lab7.error.ConnectionErrorExp;
import me.dasha.lab7.error.OpeningServerExp;
import me.dasha.lab7.managers.ConnectionManager;
import me.dasha.lab7.managers.FutureManager;

import java.io.*;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Server {
    private int port;
    private int soTimeout;
    private Writer console;
    private ServerSocketChannel serverSocket;
    private SocketChannel socketChannel;
    private final DataBaseHandler dataBaseHandler;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    static final Logger serverLogger = Logger.getLogger(Server.class.getName());

    BufferedInputStream bf = new BufferedInputStream(System.in);

    BufferedReader scanner = new BufferedReader(new InputStreamReader(bf));
    private CommandManager commandManager;

    public Server(int port, int soTimeout, DataBaseHandler dataBaseHandler, CommandManager commandManager) {
        this.port = port;
        this.soTimeout = soTimeout;
        this.commandManager = commandManager;
        this.dataBaseHandler = dataBaseHandler;
        this.console = new Console();
    }

    /**
     * Opens a server socket
     */
    public void open() throws OpeningServerExp {
        try {
            serverLogger.info("Запуск сервера...");
//            serverSocket = new ServerSocket(port);
            SocketAddress socketAddress = new InetSocketAddress(port);
            serverSocket = ServerSocketChannel.open();
            serverSocket.bind(socketAddress);

            serverLogger.info("Сервер запущен успешно");
        } catch (IllegalArgumentException exception) {
            console.printError("Порт " + port + "недоступен.");
            serverLogger.info("Порт " + port + "недоступен.");
            throw new OpeningServerExp();
        } catch (IOException exception) {
            console.printError("Ошибка при использовании порта " + port);
            serverLogger.info("Ошибка при использовании порта " + port);
            throw new OpeningServerExp();
        }
    }

    /**
     * Connecting to a client
     */
    public SocketChannel connectToClient() throws ConnectionErrorExp, SocketTimeoutException {//прием подключений
        try {
            serverSocket.socket().setSoTimeout(soTimeout);
            socketChannel = serverSocket.socket().accept().getChannel();
            serverLogger.info("Соединение с клиентом установлено успешно.");
            return socketChannel;
        } catch (SocketTimeoutException exception) {
            serverLogger.info("Превышено время ожидания подключения.");
        } catch (IOException exception) {
            console.printError("Произошла ошибка при соединении с клиентом.");
            serverLogger.info("Произошла ошибка при соединении с клиентом.");
        }
        return null;
    }


    /**
     * Starting the server
     */

    public void runServer(){
        try{
            open();//открываем сокеты
            while(true) {
                FutureManager.checkAllFutures();
                try {
                    cachedThreadPool.submit(new ConnectionManager(commandManager, connectToClient(), dataBaseHandler));
                } catch (SocketTimeoutException e){
                    e.printStackTrace();
                }catch (ConnectionErrorExp e){
                    serverLogger.warning("Ошибка при соединении с клиентом");
                }
            }
        } catch (OpeningServerExp ex) {
            console.printError("Сервер не может быть запущен");
        }
        stop();
        serverLogger.info("Соединение закрыто");
    }

    /**
     * Terminates the server
     */
    public void stop() {
        try {
            serverLogger.info("Завершение работы сервера...");
            if (serverSocket == null) throw new ClosingSocketExp();
            cachedThreadPool.shutdown();
            serverSocket.close();
            console.println("Работа сервера успешно завершена.");
            serverLogger.info("Работа сервера успешно завершена.");
        } catch (ClosingSocketExp exception) {
            console.printError("Нельзя завершить работу незапущенного сервера.");
            serverLogger.info("Нельзя завершить работу незапущенного сервера.");
        } catch (IOException exception) {
            console.printError("Произошла ошибка при завершении работы сервера.");
            serverLogger.info("Произошла ошибка при завершении работы сервера.");
        }
    }
}
