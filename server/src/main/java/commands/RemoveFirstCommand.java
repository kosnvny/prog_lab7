package commands;

import exceptions.IllegalArguments;
import managers.CollectionManager;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

public class RemoveFirstCommand extends Command implements EditingCollection{
    /**{@link CollectionManager}, в котором хранится коллекция и с помощью которого выполняется команда*/
    private final CollectionManager collectionManager;
    public RemoveFirstCommand(CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
    }
    /** Метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Были получены аргументы
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments("В команде remove_first нет аргументов");
        collectionManager.removeFirstElement();
        return new Response(ResponseStatus.OK, "Первый элемент коллекции удалён");
    }
}
