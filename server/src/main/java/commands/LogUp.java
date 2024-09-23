package commands;

import dataBases.DatabaseManager;
import dataBases.DatabaseManagerHandler;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

import java.sql.SQLException;

public class LogUp extends Command{
    private final DatabaseManager databaseManager = DatabaseManagerHandler.getDatabaseManager();
    public LogUp() {
        super("log_up", " зарегистрироваться");
    }

    /**
     * Абстрактный метод для выполнения команды
     *
     * @param request Аргументы команды
     * @return ответ на выполнение командым
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
