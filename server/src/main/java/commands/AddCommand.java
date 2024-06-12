package commands;

import dataBases.DatabaseManagerHandler;
import exceptions.IllegalArguments;
import exceptions.InvalideForm;
import exceptions.LessRoleThanNeedException;
import managers.*;
import models.*;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

/**
 * Класс команды add
 * */
public class AddCommand extends Command implements EditingCollection, CommandsWithElement {
    /**{@link CollectionManager}, в котором хранится коллекция и с помощью которого выполняется команда*/
    private final CollectionManager collectionManager;
    public AddCommand(CollectionManager collectionManager) {
        super("add", "{element} : добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
    }
    /**
     * Выполнение команды
     * @param request аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments аргументы не валидны по различным причинам
     * @throws InvalideForm форма заполнения объектов {@link StudyGroup}, {@link Person} или их составляющих неправильна*/
    @Override
    public Response execute(Request request) throws IllegalArguments, InvalideForm, LessRoleThanNeedException {
        if (request.getUser().getRole().ordinal() < 2) throw new LessRoleThanNeedException();
        if (!request.getArgs().isBlank()) throw new IllegalArguments("Аргументы в команде add должны вводиться со следующей строки");

        int newID = DatabaseManagerHandler.getDatabaseManager().addObject(request.getStudyGroup(), request.getUser());
        if (newID == -1) return new Response(ResponseStatus.ERROR, "Не получилось добавить элемент в коллекцию");
        StudyGroup.updateID(collectionManager.getCollection());
        Person.setSeriesAndNumber(collectionManager.getCollection());
        request.getStudyGroup().setId(StudyGroup.newID());
        collectionManager.addElement(request.getStudyGroup());
        return new Response(ResponseStatus.OK, "Объект был добавлен в коллекцию");
    }
}
