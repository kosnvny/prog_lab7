package managers;

import commands.Command;
import commands.CommandsWithElement;
import commands.EditingCollection;
import exceptions.*;
import models.Person;
import models.StudyGroup;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;
import utility.ScannerManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;

/**Класс для работы с командами и их запуском*/
public class CommandManager {
    /**Отображение, хранящее как ключ строковое предствление команды, а как значение - саму команду*/
    private final HashMap<String, Command> commands = new HashMap<>();
    private final FileManager fileManager;

    public CommandManager(FileManager fileManager) {
        this.fileManager = fileManager;
    }

    /** Добавление команды в отображение и коллекции
     * @param command добавляемая команда*/
    public void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    /** Добавление коллекции команд
     * @param commands добавляемые команды*/
    public void addCommands(Collection<Command> commands) {
        for (Command c: commands) {
            this.commands.put(c.getName(), c);
        }
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public String getAllCommands() {
        StringBuilder text = new StringBuilder();
        for (String s : commands.keySet()) {
            text.append(s).append(" ");
            text.append(commands.get(s).getDescription()).append("\r\n");
        }
        return text.toString();
    }

    /** Выполнение полученной команды
     * @param request запрос от клиента
     * @throws CommandDoesNotExist команда не существует
     * @throws IllegalArguments невалидные значения
     * @throws ForcedExit выход из приложения
     * @throws RecursionInScriptException рекурсия в скрипте
     * @throws InvalideForm форма заполнения объектов {@link StudyGroup}, {@link Person} или их составляющих получила неверные аргументы
     * */
    public Response execute(Request request) throws CommandDoesNotExist, IllegalArguments, ForcedExit, RecursionInScriptException, InvalideForm {
        Command command = commands.get(request.getCommandName());
        if (command == null) throw new CommandDoesNotExist("Данной команды не существует");
        if (command instanceof CommandsWithElement) {
            if (Objects.isNull(request.getStudyGroup())) return new Response(ResponseStatus.ASK_FOR_OBJECT, "команде " + command.getName() + " требуется элемент для манипуляций");
            else ScannerManager.setUsersScanner(request.getStudyGroup().toString());
        }
        Response response = command.execute(request);
        if (command instanceof EditingCollection) {
            fileManager.writeCollection();
        }
        return response;
    }
}
