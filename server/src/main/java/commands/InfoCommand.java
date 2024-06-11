package commands;

import exceptions.IllegalArguments;
import managers.CollectionManager;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

public class InfoCommand extends Command {
    /**{@link CollectionManager}, в котором хранится коллекция и с помощью которого выполняется команда*/
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)");
        this.collectionManager = collectionManager;
    }

    /** Метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Поступили невалидные аргументы
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments("В команде info не может быть аргументов");
        return new Response(ResponseStatus.OK, "Информация о коллекции. \n" + collectionManager.info());
    }
}
