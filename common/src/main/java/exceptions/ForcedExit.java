package exceptions;

/**Класс для исключения при выходе из приложения*/
public class ForcedExit extends InterruptedException{
    public ForcedExit(String s) {
        super(s);
    }
}
