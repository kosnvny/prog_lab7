package commands;

import dataBases.DatabaseManager;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

import java.sql.SQLException;

public class LogIn extends Command{
    private DatabaseManager databaseManager;
    public LogIn(DatabaseManager databaseManager) {
        super("log_in", " зарегистрироваться");
        this.databaseManager = databaseManager;
    }

    /**
     * Абстрактный метод для выполнения команды
     *
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     */
    @Override
    public Response execute(Request request) {
        try {
            databaseManager.addUser(request.getUser());
        } catch (SQLException e) {
            return new Response(ResponseStatus.LOGIN_FAILED, "Введен невалидный пароль!");
        }
        return new Response(ResponseStatus.OK,"Вы успешно зарегистрированы!");
    }
}
