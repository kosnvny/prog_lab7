package exceptions;

import java.io.IOException;

/**Класс исключения при неправильных аргументах*/
public class IllegalArguments extends IOException {
    public IllegalArguments(String message) {
        super(message);
    }
}
