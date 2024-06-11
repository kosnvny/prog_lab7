package commands;

import exceptions.IllegalArguments;
import managers.CollectionManager;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

public class ShowCommand extends Command{
    /**{@link CollectionManager}, в котором хранится коллекция и с помощью которого выполняется команда*/
    private final CollectionManager collectionManager;
    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении");
        this.collectionManager = collectionManager;
    }
    /** Метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Поступили невалидные аргументы
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments("В команде show нет аргументов");
        if (collectionManager.getCollection().isEmpty()) return new Response(ResponseStatus.OK, "В коллекции нет элементов(");
        else return new Response(ResponseStatus.OK, collectionManager.show());
    }
}
