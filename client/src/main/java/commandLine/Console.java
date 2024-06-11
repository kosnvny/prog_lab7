package commandLine;


/**Класс консоли*/
public class Console implements Printable{
    private static boolean isItInFile = false;
    /** Метод вывода с переходом на следующую строку
     * @param args Передаваемые аргументы
     */
    @Override
    public void println(String args) {
        System.out.println(args);
    }

    /** Метод вывода
     * @param args Передаваемые аргументы
     */
    @Override
    public void print(String args) {
        System.out.print(args);
    }

    /** Метод вывода ошибок
     * @param args Передаваемые аргументы
     */
    @Override
    public void printError(String args) {
        System.out.println("Обнаружена ошибка: " + args);
    }

    public static boolean isIsItInFile() {
        return isItInFile;
    }

    public static void setIsItInFile(boolean isItInFile) {
        Console.isItInFile = isItInFile;
    }
}
