package commands;

import utility.Request;
import utility.Response;
import utility.ResponseStatus;



public class LogUp extends Command{
    public LogUp() {
        super("log_up", " зайти в аккаунт");
    }

    /**
     * Абстрактный метод для выполнения команды
     *
     * @param request Аргументы команды
     * @return ответ на выполнение командым
     */
    @Override
    public Response execute(Request request) {
        return new Response(ResponseStatus.OK, "вы вошли в приложение");

    }
}
