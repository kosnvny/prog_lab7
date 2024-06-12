package utility;

import commandLine.Console;
import commandLine.Printable;
import exceptions.*;
import formsForUser.StudyGroupForm;
import formsForUser.UserForm;
import models.StudyGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

/**Класс для начала работы приложения*/
public class RuntimeManager {
    /**Поле, отвечающее за вывод информации о работе команды*/
    private final Printable console;
    private final ClientTCP clientTCP;
    private final Scanner userScanner;
    private User user = null;
    public RuntimeManager(Printable console, ClientTCP clientTCP, Scanner userScanner) {
        this.console = console;
        this.clientTCP = clientTCP;
        this.userScanner = userScanner;
    }

    /**Метод, запускающий приложение*/
    public void letsGo() {
        clientTCP.connectToServer();
        while (true) {
            try {
                if (Objects.isNull(user)) {
                    Response response = null;
                    boolean isLogIn = true;
                    do {
                        if (!Objects.isNull(response)) {
                            console.println((isLogIn) ? "такой логин не был найден (" : "попробуйте использовать другой догин");
                        }
                        UserForm userForm = new UserForm(console);
                        isLogIn = userForm.askIfLogin();
                        user = userForm.build();
                        if (isLogIn) {
                            response = clientTCP.sendAndAskResponse(new Request("log_up", "", user)); //зайти в "аккаунт"
                        } else {
                            response = clientTCP.sendAndAskResponse(new Request("log_in", "", user)); // зарегистрироваться
                        }
                    } while (response.getResponseStatus() != ResponseStatus.OK);
                    console.println("вы зашли в аккаунт!");
                }
                StudyGroup studyGroup = null;
                if (!userScanner.hasNext()) throw new ForcedExit("Ввод отсутствует");
                String[] userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2); // прибавляем пробел, чтобы split выдал два элемента в массиве
                if (userCommand[0].equals("add") || userCommand[0].equals("update") || userCommand[0].equals("remove_greater")) {
                    studyGroup = this.build();
                }
                Response response = clientTCP.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim(), studyGroup, user));
                this.printResponse(response);
                switch (response.getResponseStatus()) {
                    case EXIT -> throw new ForcedExit("Вы вышли из приложения с помощью команды exit");
                    case EXECUTE_SCRIPT -> {
                        Console.setIsItInFile(true);
                        this.fileExecution(response.getResponse());
                        Console.setIsItInFile(false);
                    }
                    case LOGIN_FAILED -> {
                        console.printError(response.getResponse());
                        this.user = null;
                    }
                    default -> {
                    }
                }
            } catch (ForcedExit e) {
                console.println(e.getMessage());
                return; // чтобы клиент вышел из приложения
            } catch (InvalideForm e) {
                console.printError(e.getMessage());
            }
        }
    }

    private void printResponse(Response response){
        switch (response.getResponseStatus()){
            case OK -> {
                if ((Objects.isNull(response.getCollection()))) {
                    console.println(response.getResponse());
                } else {
                    console.println(response.getResponse() + "\n" + response.getCollection().toString());
                }
            }
            case ERROR -> console.printError(response.getResponse());
            case WRONG_ARGUMENTS -> console.printError("Неверное использование команды!\n" + response.getResponse());
            default -> {}
        }
    }

    private void fileExecution(String args) throws ForcedExit{
        if (args == null || args.isEmpty()) {
            console.printError("Путь не распознан");
            return;
        }
        else console.println("Путь получен успешно");
        args = args.trim();
        try {
            StudyGroup studyGroup = null;
            ExecuteScriptManager.pushFile(args);
            for (String line = ExecuteScriptManager.readLine(); line != null; line = ExecuteScriptManager.readLine()) {
                String[] userCommand = (line + " ").split(" ", 2);
                if (userCommand[0].equals("add") || userCommand[0].equals("update") || userCommand[0].equals("remove_greater")) {
                    studyGroup = this.build();
                }
                userCommand[1] = userCommand[1].trim();
                if (userCommand[0].isBlank()) return;
                if (userCommand[0].equals("execute_script")){
                    if(ExecuteScriptManager.haveWeBeenInFile(userCommand[1])){
                        console.printError("Найдена рекурсия по пути " + new File(userCommand[1]).getAbsolutePath());
                        return;
                    }
                }
                console.println("Выполнение команды " + userCommand[0]);
                Response response = clientTCP.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim(), studyGroup, user));
                this.printResponse(response);
                switch (response.getResponseStatus()){
                    case EXIT -> throw new ForcedExit("Вы вышли из приложения с помощью команды exit");
                    case EXECUTE_SCRIPT -> {
                        this.fileExecution(response.getResponse());
                        ExecuteScriptManager.popRecursion();
                    }
                    default -> {}
                }
            }
            ExecuteScriptManager.popFile();
        } catch (FileNotFoundException e){
            console.printError("Файла нет (((((");
        } catch (IOException e) {
            console.printError("Ошибка ввода вывода");
        }
    }
    private StudyGroup build() {
        try {
            return new StudyGroupForm(console).build(user);
        } catch (InvalideForm ignored) {}
        return null;
    }
}
