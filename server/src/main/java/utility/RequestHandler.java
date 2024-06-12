package utility;

import exceptions.*;
import managers.CommandManager;
import managers.ConnectionManagerPool;

import java.io.ObjectOutputStream;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<ConnectionManagerPool> {
    private final CommandManager commandManager;
    private Request request;
    private ObjectOutputStream objectOutputStream;

    public RequestHandler(CommandManager commandManager, Request request, ObjectOutputStream objectOutputStream) {
        this.commandManager = commandManager;
        this.request = request;
        this.objectOutputStream = objectOutputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        this.objectOutputStream = objectOutputStream;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public ConnectionManagerPool call() throws Exception {
        try {
            return new ConnectionManagerPool(commandManager.execute(request), objectOutputStream);
        } catch (IllegalArguments | InvalideForm e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.WRONG_ARGUMENTS,
                    e.getMessage()), objectOutputStream);
        } catch (CommandDoesNotExist | RecursionInScriptException | LessRoleThanNeedException e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.ERROR, e.getMessage()), objectOutputStream);
        } catch (ForcedExit e) {
            return new ConnectionManagerPool(new Response(ResponseStatus.EXIT, e.getMessage()), objectOutputStream);
        }
    }
}
