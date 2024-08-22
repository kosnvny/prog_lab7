package commands;

import dataBases.DatabaseManager;
import dataBases.DatabaseManagerHandler;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

import java.sql.SQLException;

public class LogIn extends Command{

    public LogIn() {
        super("log_in", " зайти в аккаунт");
    }

    /**
     * Абстрактный метод для выполнения команды
     *
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     */
    @Override
    public Response execute(Request request) {
        DatabaseManager databaseManager = DatabaseManagerHandler.getDatabaseManager();
        if (databaseManager.confirmUser(request.getUser())) {
            return new Response(ResponseStatus.OK, "Доступ разрешён");
        }
        return new Response(ResponseStatus.WRONG_ARGUMENTS, "Введённые пароль и логин не являются актуальными или пользователь не зарегистрирован ;(");
    }
}
