package exceptions;

import java.io.IOException;

/**Класс исключения для неверных аргументов для создания объектов с помощью {@link models.formsForUser.Form}*/
public class InvalideForm extends IOException {
    public InvalideForm(String message) {
        super(message);
    }
}
