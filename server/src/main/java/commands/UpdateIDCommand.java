package commands;

import exceptions.*;
import managers.CollectionManager;
import models.StudyGroup;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

import java.util.Objects;

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
    public Response execute(Request request) throws IllegalArguments {
        if (request.getArgs().isBlank()) throw new IllegalArguments("В команде update должны быть непустые аргументы");
        if (Objects.isNull(request.getStudyGroup())) return new Response(ResponseStatus.ASK_FOR_OBJECT, "Для команды " + getName() + " требуется объект StudyGroup");
        try {
            int id = Integer.parseInt(request.getArgs().trim());
            if (collectionManager.checkIfExists(id)) {
                StudyGroup sG = request.getStudyGroup();
                collectionManager.updateID(id, sG);
                return new Response(ResponseStatus.OK, "Элемент по заданному ID был изменён");
            } else {
                throw new IllegalArguments("Данный ID не существует");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArguments("ID должен быть целым числом");
        }
    }
}
