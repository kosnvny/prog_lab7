package commands;

import exceptions.*;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

/**
 * Класс команды execute_script*/
public class ExecuteScriptCommand extends Command{
    public ExecuteScriptCommand() {
        super("execute_script", "file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.");
    }
    /** Метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Аргумент этой команды не может быть пустым
     * @throws RecursionInScriptException Скрипт вызывает сам себя, или скрипты образовали цикл
     * @throws ForcedExit Во время выполнения команды случилось непоправимое
     * @throws CommandDoesNotExist Команда не существует
     * @throws InvalideForm формы для объектов получили неверные аргументы*/
    @Override
    public Response execute(Request request) throws IllegalArguments, RecursionInScriptException, ForcedExit, CommandDoesNotExist, InvalideForm {
        if (request.getArgs().isBlank()) throw new IllegalArguments("В команде execute_script аргументом должен быть путь");
        return new Response(ResponseStatus.EXECUTE_SCRIPT, request.getArgs());
    }
}
