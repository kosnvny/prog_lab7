package commands;

import exceptions.IllegalArguments;
import managers.CollectionManager;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

public class PrintDescendingCommand extends Command{
    /**{@link CollectionManager}, в котором хранится коллекция и с помощью которого выполняется команда*/
    private final CollectionManager collectionManager;
    public PrintDescendingCommand(CollectionManager collectionManager) {
        super("print_descending", "вывести элементы коллекции в порядке убывания");
        this.collectionManager = collectionManager;
    }

    /** Метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Поступили невалидные аргументы
     */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments("В команде print_descending не должно быть аргументов");
        return new Response(ResponseStatus.OK, collectionManager.descendingOrder());
    }
}
