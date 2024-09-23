package commands;

import dataBases.DatabaseManagerHandler;
import exceptions.IllegalArguments;
import exceptions.LessRoleThanNeedException;
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
    public Response execute(Request request) throws IllegalArguments, LessRoleThanNeedException {
        if (request.getUser().getRole().ordinal() == 0) throw new LessRoleThanNeedException();
        if (!request.getArgs().isBlank()) throw new IllegalArguments("В команде remove_first нет аргументов");
        if (DatabaseManagerHandler.getDatabaseManager().deleteObject(1, request.getUser())) {
            collectionManager.removeFirstElement();
            return new Response(ResponseStatus.OK,"первый элемент удален успешно");
        } else {
            return new Response(ResponseStatus.ERROR, "Выбранный объект не удален. Скорее всего он вам не принадлежит");
        }
    }
}
