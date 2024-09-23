package commands;

import dataBases.DatabaseManagerHandler;
import exceptions.*;
import utility.Request;
import utility.Response;
import utility.ResponseStatus;

import java.sql.SQLException;

public class ChangeRoleCommand extends Command{
    public ChangeRoleCommand() {
        super("change_role", " user_login role: изменяет роль заданного пользователя");
    }

    /**
     * метод для выполнения команды
     *
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws IllegalArguments           Невалидные аргументы
     */
    @Override
    public Response execute(Request request) throws IllegalArguments, LessRoleThanNeedException {
        if (request.getUser().getRole().ordinal() != 3) throw new LessRoleThanNeedException();
        if (request.getArgs().isBlank()) throw new IllegalArguments("Данной команде требуется два аргумента для выполнения: логин пользователя, чью роль будете измнять, и сама роль");
        try {
           String[] args = request.getArgs().split(" ");
           if (DatabaseManagerHandler.getDatabaseManager().checkIfUserExists(args[0])) {
                if (args[0].contains("_admin")) throw new LessRoleThanNeedException();
                if (DatabaseManagerHandler.getDatabaseManager().changeRole(args[0], args[1])) return new Response(ResponseStatus.OK, "Роль пользователя " + args[0] + " изменилась");
                else return new Response(ResponseStatus.ERROR, "ошибка при попытке изменить роль");
           }
        } catch (SQLException e) {
            return new Response(ResponseStatus.WRONG_ARGUMENTS, "такого пользователя нет в базе(");
        }
        return null;
    }
}
