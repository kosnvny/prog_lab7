package commands;

import dataBases.DatabaseManagerHandler;
import exceptions.IllegalArguments;
import exceptions.LessRoleThanNeedException;
import managers.CollectionManager;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

public class RemoveByIDCommand extends Command implements EditingCollection{
    /**{@link CollectionManager}, в котором хранится коллекция и с помощью которого выполняется команда*/
    private final CollectionManager collectionManager;
    public RemoveByIDCommand(CollectionManager collectionManager) {
        super("remove_by_id", "id : удалить элемент из коллекции по его id");
        this.collectionManager = collectionManager;
    }
    /** Метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Команде поступили невалидные аргументы
     */
    @Override
    public Response execute(Request request) throws IllegalArguments, LessRoleThanNeedException {
        if (request.getUser().getRole().ordinal() < 2) throw new LessRoleThanNeedException();
        if (request.getArgs().isBlank()) throw new IllegalArguments("В команде remove_by_id должны быть аргументы");
        try {
            int id = Integer.parseInt(request.getArgs().trim());
            if (collectionManager.checkIfExists(id)) {
                if (DatabaseManagerHandler.getDatabaseManager().deleteObject(id, request.getUser())) {
                    collectionManager.removeByID(id);
                    return new Response(ResponseStatus.OK,"Объект удален успешно");
                } else{
                    return new Response(ResponseStatus.ERROR, "Выбранный объект не удален. Скорее всего он вам не принадлежит");
                }
            } else {
                throw new IllegalArguments("Этот ID не существует");
            }
        } catch (NumberFormatException e) {
            return new Response(ResponseStatus.ERROR, "Вы ввели не целое число(");
        }
    }
}
