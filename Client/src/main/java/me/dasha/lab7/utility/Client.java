package me.dasha.lab7.utility;

import me.dasha.lab7.console.Console;
import me.dasha.lab7.exp.ExitExp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Objects;

public class Client {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private Console console;
    private Socket socket;
    private ObjectOutputStream writer;
    private ObjectInputStream reader;
    private boolean firstExecution = true;


    public Client(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, Console console) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.console = console;
    }
    public Response sendAndAskResponse(Request request) {
        while (true) {
            try {
                if (Objects.isNull(writer) || Objects.isNull(reader)) throw new IOException();
                    //connectToServer();
                if (request.isEmpty()) return new Response(ResponseStatus.WRONG_ARGUMENTS, "Запрос пустой!");
                writer.writeObject(request);
                writer.flush();
                Response response = (Response) reader.readObject();
                //closeServer();
                reconnectionAttempts = 0;
                return response;
            } catch (IOException e) {
                if (reconnectionAttempts == 0) {
                    connectToServer();
                    reconnectionAttempts++;
                }
                else console.printError("Соединение с сервером разорвано\n");
                try {
                    reconnectionAttempts++;
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        console.printError("Превышено максимальное количество попыток соединения с сервером\n");
                        return new Response(ResponseStatus.EXIT);
                    }
                    console.write("Повторная попытка через " + reconnectionTimeout / 1000 + " секунд\n");
                    Thread.sleep(reconnectionTimeout);
                    connectToServer(); // Повторное подключение к серверу
                } catch (Exception exception) {
                    console.printError("Попытка соединения с сервером неуспешна\n");
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void connectToServer(){
        try{
            if (reconnectionAttempts > 0)
                console.write("Попытка повторного подключения\n");
            socket = new Socket(host, port);
            console.write("Подключение успешно восстановлено\n");

            this.writer = new ObjectOutputStream(socket.getOutputStream());
            this.reader = new ObjectInputStream(socket.getInputStream());
            console.write("Обмен пакетами разрешен\n\n");

        } catch (IllegalArgumentException e){
            console.printError("Адрес сервера введен некорректно\n");
        } catch (IOException e) {
            console.printError("Произошла ошибка при соединении с сервером\n");
        }
    }

    public void closeServer(){
        try {
            writer.close();
            reader.close();
            socket.close();
        } catch (IOException e) {
            console.printError("Нет подключения к серверу");
        }

    }

}