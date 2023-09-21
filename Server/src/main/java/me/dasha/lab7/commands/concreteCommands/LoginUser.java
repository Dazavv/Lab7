package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.managers.CollectionManager;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class LoginUser extends Command {
    DataBaseHandler dataBaseHandler;

    public LoginUser(DataBaseHandler dataBaseHandler) {
        super("login", "login: войти");
        this.dataBaseHandler = dataBaseHandler;
    }

    @Override
    public Response execute(Request request) throws IllegalArgumentExp {
        this.commandLogger.info("получен юзер: " + request.getUser());
        try {
            dataBaseHandler.loginUser(request.getUser());
            this.commandLogger.info("юзер успешно авторизовался");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return new Response(ResponseStatus.OK,"Вы успешно авторизовались");
    }
}
