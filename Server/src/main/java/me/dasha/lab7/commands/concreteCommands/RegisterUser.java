package me.dasha.lab7.commands.concreteCommands;

import me.dasha.lab7.commands.Command;
import me.dasha.lab7.dataBase.DataBaseHandler;
import me.dasha.lab7.exp.IllegalArgumentExp;
import me.dasha.lab7.utility.Request;
import me.dasha.lab7.utility.Response;
import me.dasha.lab7.utility.ResponseStatus;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class RegisterUser extends Command {
    DataBaseHandler dataBaseHandler;

    public RegisterUser(DataBaseHandler dataBaseHandler) {
        super("register", "register: зарегистрировать пользователя");
        this.dataBaseHandler = dataBaseHandler;
    }

    @Override
    public Response execute(Request request) throws IllegalArgumentExp {
        this.commandLogger.info("получен юзер: " + request.getUser());
        try {
            dataBaseHandler.registerUser(request.getUser());
        } catch (SQLException e) {
            e.getMessage();
            commandLogger.severe("Невозможно добавить пользователя");
            return new Response(ResponseStatus.LOGIN_FAILED, "Введен невалидный пароль!");
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return new Response(ResponseStatus.OK,"Вы успешно зарегистрированы!");
    }
}
