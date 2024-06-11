package commandLine;

import utility.ScannerManager;

import java.util.Scanner;

/**Класс консоли ввода пользователя*/
public class ConsoleInput implements UserInput{
    /**Поле, хранящее ввод пользователя*/
    private final Scanner userInput = ScannerManager.getUsersScanner();
    /** Метод, читающий следующую строку ввода пользователя
     * @return Строковое представление ввода
     */
    @Override
    public String nextLine() {
        return userInput.nextLine();
    }
}
