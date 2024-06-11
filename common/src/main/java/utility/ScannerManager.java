package utility;

import java.util.Scanner;

/**Класс, хранящий пользовательский ввод*/
public class ScannerManager {
    public static Scanner usersScanner = new Scanner(System.in);

    /**Статический метод, устанавливающий строку в сканнер
     * @param usersScanner1 строка, которую закидываем в сканнер*/
    public static void setUsersScanner(String usersScanner1) {
        usersScanner = new Scanner(usersScanner1);
    }

    /** Статический метод, передающий пользовательский ввод
     * @return Scanner*/
    public static Scanner getUsersScanner() {
        return usersScanner;
    }
}
