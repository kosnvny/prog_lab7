package exceptions;

import java.io.IOException;

/**Класс исключения при отсутствии команды*/
public class CommandDoesNotExist extends IOException {
    /**
     * Constructs an {@code IOException} with the specified detail message.
     *
     * @param message The detail message (which is saved for later retrieval
     *                by the {@link #getMessage()} method)
     */
    public CommandDoesNotExist(String message) {
        super(message);
    }
}
