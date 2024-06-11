package utility;

import commands.Command;
import exceptions.*;
import managers.CommandManager;

public class RequestHandler {
    private final CommandManager commandManager;
    public RequestHandler(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    public Response handle(Request request) {
        try {
            return commandManager.execute(request);
        } catch (CommandDoesNotExist e) {
            return new Response(ResponseStatus.ERROR, e.getMessage()); //"Команды не существует"
        } catch (IllegalArguments e) {
            return new Response(ResponseStatus.WRONG_ARGUMENTS, e.getMessage()); //"Невалидные аргументы для команды"
        } catch (ForcedExit e) {
            return new Response(ResponseStatus.EXIT, e.getMessage());
        } catch (RecursionInScriptException e) {
            return new Response(ResponseStatus.ERROR, e.getMessage()); //"Рекурсия в запускаемых файлах"
        } catch (InvalideForm e) {
            return new Response(ResponseStatus.WRONG_ARGUMENTS, e.getMessage()); //"Форма создания объекта получила невалидные значения"
        }
    }
}
