package me.dasha.lab7.forms;


import me.dasha.lab7.console.*;
import me.dasha.lab7.exp.InvalidFormExp;
import me.dasha.lab7.utility.ExecuteFileManager;
import me.dasha.lab7.utility.User;

import java.util.Objects;

public class UserForm extends Form<User> {
    private final ReaderWriter console;
    private final UserInput scanner;

    public UserForm(ReaderWriter console) {
        this.console = (Console.isFileMode())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isFileMode())
                ? new ExecuteFileManager()
                : new ConsoleInput();
    }

    @Override
    public User build() throws InvalidFormExp {
        return new User(askLogin(), askPassword());
    }

    public String askLogin(){
        String login;
        while (true){
            console.write("Введите логин: ");
            login = scanner.nextLine().trim();
            if (login.isEmpty()) {
                console.printError("Логин не введён");
            }
            return login;
        }
    }
    public String askPassword(){
        String pass;
        while (true){
            console.write("Введите пароль: ");
            pass = (Objects.isNull(System.console()))
                    ? scanner.nextLine().trim()
                    : new String(System.console().readPassword());
            if (pass.isEmpty()){
                console.printError("Пароль не может быть пустым");
                //if (Console.isFileMode()) throw new FileModeException();
            }
            else{
                return pass;
            }
        }
//        String password;
//        java.io.Console console1 = System.console();
//        while (true){
//            console.write("Введите пароль: ");
//            if (console != null) {
//                char[] symbols = console1.readPassword();
//                if (symbols == null) continue;
//                password = String.valueOf(symbols);
//            }
//            password = scanner.nextLine().trim();
//            return password;
//        }
    }

    public String askAuthorization(){
        String answer;
        for(;;) {
            console.write("Вы зарегистрированы? (yes/no)\n");
            answer = scanner.nextLine().trim();
            if (answer.equalsIgnoreCase("yes")) return "login";
            if (answer.equalsIgnoreCase("no")) return "register";
            else {
                console.write("Ответ может быть только \"yes\" или \"no\"\n");
            }
        }
    }

}
