package exceptions;


/**Класс для исключения при нахождении рекурсии в скриптах*/
public class RecursionInScriptException extends Exception {
    public RecursionInScriptException() {
        super("Переданные скрипты вызывают друг друга и образуют цикл");
    }
}
