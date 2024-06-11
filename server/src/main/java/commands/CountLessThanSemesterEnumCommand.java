package commands;

import exceptions.*;
import managers.CollectionManager;
import models.Semester;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

public class CountLessThanSemesterEnumCommand extends Command{
    /**{@link CollectionManager}, в котором хранится коллекция и с помощью которого выполняется команда*/
    private final CollectionManager collectionManager;
    public CountLessThanSemesterEnumCommand(CollectionManager collectionManager) {
        super("count_less_than_semester_enum", " semesterEnum : вывести количество элементов, значение поля semesterEnum которых меньше заданного");
        this.collectionManager = collectionManager;
    }

    /**метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments Выбрасывается, если получены пустые аргументы
     * */
    @Override
    public Response execute(Request request) throws IllegalArguments {
        if (request.getArgs().isBlank()) throw new IllegalArguments("В команде count_less_than_semester_enum не может быть пустого аргумента");
        try {
            Semester semester = Semester.valueOf(request.getArgs().trim().toUpperCase());
            return new Response(ResponseStatus.OK, "Выводим количество элементов, где значение поля semesterEnum меньше заданного.\n" + collectionManager.countLessThanSemester(semester));
        } catch (IllegalArgumentException e) {
            return new Response(ResponseStatus.ERROR, "Вы ввели значение не из списка");
        }
    }
}
