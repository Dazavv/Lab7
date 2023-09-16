package me.dasha.lab7.utility;

import me.dasha.lab7.collectionClasses.SpaceMarine;
import me.dasha.lab7.console.Console;
import me.dasha.lab7.exp.*;
import me.dasha.lab7.forms.SpaceMarineForm;
import me.dasha.lab7.forms.UserForm;

import java.io.*;
import java.util.*;
/**
 *
 * User input handling class
 */
public class RuntimeManager {
    private final Console console;
    private final Scanner userScanner;
    private final Client client;
    private HashSet<String> executedScripts = new HashSet<>();
    private User user = null;

    public RuntimeManager(Console console, Scanner userScanner, Client client) {
        this.console = console;
        this.userScanner = userScanner;
        this.client = client;
    }

    /**
     * Permanent work with the user and execution of commands
     */
    public void interactiveMode(){
        while (true) {
            try {
                if (Objects.isNull(user)) {
                    Response response = null;
                    String command;
                    do {
                        UserForm userForm = new UserForm(console);
                        command = userForm.askAuthorization();
                        user = new UserForm(console).build();
                        response = client.sendAndAskResponse(new Request(command, "", user));
                        printResponse(response);
                    } while (response.getResponseStatus() != ResponseStatus.OK);
                    console.write("Для получения справки о доступных командах введите help\n");
                }
                if (!userScanner.hasNext()) throw new ExitExp();
                String[] userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2); // прибавляем пробел, чтобы split выдал два элемента в массиве
                Response response = client.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim(), user));
                this.printResponse(response);
                switch (response.getResponseStatus()) {
                    case ASK_OBJECT -> {
                        SpaceMarine spaceMarine = new SpaceMarineForm(console).build();
                        if(!spaceMarine.validate()) throw new InvalidFormExp();
                        Response newResponse = client.sendAndAskResponse(
                                new Request(
                                        userCommand[0].trim(),
                                        userCommand[1].trim(),
                                        user,
                                        spaceMarine));
                        if (newResponse.getResponseStatus() != ResponseStatus.OK) {
                            console.printError(newResponse.getResponse());
                        } else {
                            this.printResponse(newResponse);
                        }
                    }
                    case EXIT -> throw new ExitExp();
                    case EXECUTE_SCRIPT -> {
                        Console.setFileMode(true);
                        this.fileExecution(response.getResponse());
                        Console.setFileMode(false);
                    }
                    case LOGIN_FAILED -> {
                        console.printError("Проблема со входом. Попробуйте снова");
                        user = null;
                    }
                    default -> {
                    }
                }

            } catch (InvalidFormExp err){
                console.printError("Поля не валидны! Объект не создан");
            } catch (NoSuchElementException exception) {
                console.printError("Пользовательский ввод не обнаружен!");
            } catch (ExitExp exitObliged) {
                console.write("Завершение программы");
                return;
            }
//            } catch (IOException e) {
//                console.printError("Ошибка!!!");
//                return;
//            }
        }
    }

    private void printResponse(Response response){
        switch (response.getResponseStatus()){
            case OK -> {
                if ((Objects.isNull(response.getCollection()))) {
                    console.write(response.getResponse());
                } else {
                    console.write(response.getResponse() + "\n" + response.getCollection().toString() + "\n");
                }
            }
            case ERROR -> console.printError(response.getResponse());
            case WRONG_ARGUMENTS -> console.printError("Неверное использование команды!");
            default -> {}
        }
    }

    private void fileExecution(String args) throws ExitExp{

        args = args.trim();
        try {
            ExecuteFileManager.pushFile(args);
            for (String line = ExecuteFileManager.readLine();
                 line != null;
                 line = ExecuteFileManager.readLine()) {
                String[] userCommand = (line + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                if (userCommand[0].isBlank()) return;
                if (userCommand[0].equals("execute_script")){
//                    String scriptPath = new File(userCommand[1]).getAbsolutePath();
//                    if (executedScripts.contains(scriptPath)) {
//                        console.printError("Пресечена попытка рекурсивного вызова скрипта");
//                        continue;
//                    }
//                    executedScripts.add(scriptPath);
                    if(ExecuteFileManager.fileRepeat(userCommand[1])){
                        console.printError("Пресечена попытка рекурсивного вызова скрипта");
                        continue;
                    }
                }
                Response response = client.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim(), user));
                this.printResponse(response);
                switch (response.getResponseStatus()){
                    case ASK_OBJECT -> {
                        SpaceMarine spaceMarine;
                        try{
                            spaceMarine = new SpaceMarineForm(console).build();
                            if (!spaceMarine.validate()) throw new FileModeExp();
                        } catch (FileModeExp err){
                            console.printError("Поля в файле не валидны! Объект не создан");
                            continue;
                        }
                        Response newResponse = client.sendAndAskResponse(
                                new Request(
                                        userCommand[0].trim(),
                                        userCommand[1].trim(),
                                        user,
                                        spaceMarine));
                        if (newResponse.getResponseStatus() != ResponseStatus.OK){
                            console.printError(newResponse.getResponse());
                        }
                        else {
                            this.printResponse(newResponse);
                        }
                    }
                    case EXIT -> throw new ExitExp();
                    case EXECUTE_SCRIPT -> {
                        this.fileExecution(response.getResponse());
                        ExecuteFileManager.popRecursion();
                    }
                    case LOGIN_FAILED -> {
                        console.printError("Ошибка с вашим аккаунтом. Зайдите в него снова");
                        this.user = null;
                    }
                    default -> {}
                }
            }
            executedScripts.remove(new File(args).getAbsolutePath());
            ExecuteFileManager.popFile();
        } catch (FileNotFoundException fileNotFoundException){
            console.printError("Такого файла не существует");
        } catch (IOException e) {
            console.printError("Ошибка ввода вывода");
        }
    }

}
