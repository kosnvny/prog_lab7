package commands;

import dataBases.DatabaseManagerHandler;
import exceptions.IllegalArguments;
import exceptions.LessRoleThanNeedException;
import managers.CollectionManager;
import models.StudyGroup;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

import java.util.Collection;
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
    public Response execute(Request request) throws IllegalArguments, LessRoleThanNeedException {
        if (request.getUser().getRole().ordinal() < 2) throw new LessRoleThanNeedException();
        if (!request.getArgs().isBlank()) throw new IllegalArguments("Аргументы запроса должны быть пустыми");
        //if (Objects.isNull(request.getStudyGroup())) return new Response(ResponseStatus.ASK_FOR_OBJECT, "Команде " + getName() + " требуется объект StudyGroup");
        //collectionManager.removeGreater(request.getStudyGroup());
        Collection<StudyGroup> toRemove = collectionManager.getCollection().stream()
                .filter(Objects::nonNull)
                .filter(studyGroup -> studyGroup.compareTo(request.getStudyGroup()) >= 1)
                .filter(studyGroup -> studyGroup.getUserLogin().equals(request.getUser().getLogin()))
                .filter((obj) -> DatabaseManagerHandler.getDatabaseManager().deleteObject(obj.getId(), request.getUser()))
                .toList();
        collectionManager.removeCollection(toRemove);
        return new Response(ResponseStatus.OK, "Элементы, большие заданного, были удалены");
    }
}
