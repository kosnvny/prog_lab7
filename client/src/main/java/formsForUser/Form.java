package formsForUser;

import exceptions.InvalideForm;
/**Абстрактный метод для форм*/
public abstract class Form<T> {

    /** Абстрактный метод, "строящий" новый объект заданного типа
     * @return T
     * @throws InvalideForm невалидные значения*/
    public abstract T build() throws InvalideForm;
}
