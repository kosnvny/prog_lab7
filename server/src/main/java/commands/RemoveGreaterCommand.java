package commands;

import exceptions.IllegalArguments;
import exceptions.InvalideForm;
import managers.CollectionManager;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

import java.util.Objects;

public class RemoveGreaterCommand extends Command implements EditingCollection, CommandsWithElement {
    /**{@link CollectionManager}, в котором хранится коллекция и с помощью которого выполняется команда*/
    private final CollectionManager collectionManager;
    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater", "{element} : удалить из коллекции все элементы, превышающие заданный");
        this.collectionManager = collectionManager;
    }

    /** Метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Поступили невалидные аргументы*/
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (!request.getArgs().isBlank()) throw new IllegalArguments("Аргументы запроса должны быть пустыми");
        if (Objects.isNull(request.getStudyGroup())) return new Response(ResponseStatus.ASK_FOR_OBJECT, "Команде " + getName() + " требуется объект StudyGroup");
        collectionManager.removeGreater(request.getStudyGroup());
        return new Response(ResponseStatus.OK, "Элементы, большие заданного, были удалены");
    }
}
