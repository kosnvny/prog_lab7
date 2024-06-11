package commands;

import exceptions.*;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

public class ExitCommand extends Command{
    public ExitCommand() {
        super("exit", "завершить программу (без сохранения в файл)");
    }

    /** Метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Аргумент этой команды должен быть пустым
     * @throws ForcedExit Выполнение команды*/
    @Override
    public Response execute(Request request) throws ForcedExit, IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments("Аргумент этой команды должен быть пустым");
        return new Response(ResponseStatus.EXIT);
    }
}
