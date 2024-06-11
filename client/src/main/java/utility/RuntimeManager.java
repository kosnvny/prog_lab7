package utility;

import commandLine.Console;
import commandLine.Printable;
import exceptions.*;
import formsForUser.StudyGroupForm;
import models.StudyGroup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
// сделать команды общими, в них обработчик, в котором мы просим всё сразу, а не перекидываем
// пользователя на постоянный ввод штук для StudyGroup

/**Класс для начала работы приложения*/
public class RuntimeManager {
    /**Поле, отвечающее за вывод информации о работе команды*/
    private final Printable console;
    private final ClientTCP clientTCP;
    private final Scanner userScanner;
    public RuntimeManager(Printable console, ClientTCP clientTCP, Scanner userScanner) {
        this.console = console;
        this.clientTCP = clientTCP;
        this.userScanner = userScanner;
    }

    /**Метод, запускающий приложение*/
    public void letsGo() {
        clientTCP.connectToServer(); // переместила из try
        try {
            if (System.getenv("filePathToRead") != null) ExecuteScriptManager.addFile(System.getenv("filePathToRead"));
            try {
                while (true) {
                    if (!userScanner.hasNext()) throw new ForcedExit("Ввод отсутствует");
                    String[] userCommand = (userScanner.nextLine().trim() + " ").split(" ", 2); // прибавляем пробел, чтобы split выдал два элемента в массиве
                    Response response = clientTCP.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim()));
                    this.printResponse(response);
                    switch (response.getResponseStatus()) {
                        case ASK_FOR_OBJECT -> {
                            console.println(response.getResponse());
                            StudyGroup studyGroup = new StudyGroupForm(console).build();
                            if (!studyGroup.validate())
                                throw new InvalideForm("Данные для формы невалидны, объект не был создан(");
                            Response newResponse = clientTCP.sendAndAskResponse(
                                    new Request(
                                            userCommand[0].trim(),
                                            userCommand[1].trim(),
                                            studyGroup));
                            if (newResponse.getResponseStatus() != ResponseStatus.OK) {
                                console.printError(newResponse.getResponse());
                            } else {
                                this.printResponse(newResponse);
                            }
                        }
                        case EXIT -> throw new ForcedExit("Вы вышли из приложения с помощью команды exit");
                        case EXECUTE_SCRIPT -> {
                            Console.setIsItInFile(true);
                            this.fileExecution(response.getResponse());
                            Console.setIsItInFile(false);
                        }
                        default -> {
                        }
                    }
                }
            } catch (ForcedExit e) {
                console.println(e.getMessage());
                return; // чтобы клиент вышел из приложения
            } catch (InvalideForm e) {
                console.printError(e.getMessage());
            }
        } catch (FileNotFoundException ignored) {}
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
            ExecuteScriptManager.pushFile(args);
            for (String line = ExecuteScriptManager.readLine(); line != null; line = ExecuteScriptManager.readLine()) {
                String[] userCommand = (line + " ").split(" ", 2);
                userCommand[1] = userCommand[1].trim();
                if (userCommand[0].isBlank()) return;
                if (userCommand[0].equals("execute_script")){
                    if(ExecuteScriptManager.haveWeBeenInFile(userCommand[1])){
                        console.printError("Найдена рекурсия по пути " + new File(userCommand[1]).getAbsolutePath());
                        return;
                    }
                }
                console.println("Выполнение команды " + userCommand[0]);
                Response response = clientTCP.sendAndAskResponse(new Request(userCommand[0].trim(), userCommand[1].trim()));
                this.printResponse(response);
                switch (response.getResponseStatus()){
                    case ASK_FOR_OBJECT -> {
                        StudyGroup studyGroup;
                        try{
                            studyGroup = new StudyGroupForm(console).build();
                            if (!studyGroup.validate()) throw new InvalideForm("Невалидные значения для формы StudyGroup или Person");
                        } catch (InvalideForm e){
                            console.printError(e.getMessage());
                            continue;
                        }
                        Response newResponse = clientTCP.sendAndAskResponse(
                                new Request(
                                        userCommand[0].trim(),
                                        userCommand[1].trim(),
                                        studyGroup));
                        if (newResponse.getResponseStatus() != ResponseStatus.OK){
                            console.printError(newResponse.getResponse());
                        }
                        else {
                            this.printResponse(newResponse);
                        }
                    }
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
}
