package commands;

import dataBases.DatabaseManagerHandler;
import exceptions.*;
import managers.CollectionManager;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

public class UpdateIDCommand extends Command implements EditingCollection, CommandsWithElement{
    /**{@link CollectionManager}, в котором хранится коллекция и с помощью которого выполняется команда*/
    private final CollectionManager collectionManager;
    public UpdateIDCommand(CollectionManager collectionManager) {
        super("update", "id {element} : обновить значение элемента коллекции, id которого равен заданному");
        this.collectionManager = collectionManager;
    }

    /** Метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Аргумент этой команды не может быть пустым
     * */
    @Override
    public Response execute(Request request) throws IllegalArguments, LessRoleThanNeedException {
        if (request.getUser().getRole().ordinal() < 2) throw new LessRoleThanNeedException();
        if (request.getArgs().isBlank()) throw new IllegalArguments("В команде update должны быть непустые аргументы");
        try {
            int id = Integer.parseInt(request.getArgs().trim());
            if (collectionManager.checkIfExists(id)) {
                if(DatabaseManagerHandler.getDatabaseManager().updateObject(id, request.getStudyGroup(), request.getUser())){
                    collectionManager.updateID(id, request.getStudyGroup());
                    return new Response(ResponseStatus.OK, "Объект успешно обновлен");
                } else {
                    return new Response(ResponseStatus.ERROR, "первоначальный объект не принадлежал вам(");
                }
            } else {
                throw new IllegalArguments("Данный ID не существует");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArguments("ID должен быть целым числом");
        }
    }
}
