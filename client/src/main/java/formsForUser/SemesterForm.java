package formsForUser;

import commandLine.*;
import models.Semester;
import utility.ExecuteScriptManager;

public class SemesterForm extends Form<Semester>{
    private final Printable console;
    private final UserInput userInput;
    public SemesterForm(Printable console) {
        this.console = (Console.isIsItInFile() ? new BlankConsole() : console);
        this.userInput = (Console.isIsItInFile() ? new ExecuteScriptManager() : new ConsoleInput());
    }
    /**
     * Абстрактный метод, "строящий" новый объект заданного типа
     *
     * @return Semester
     */
    @Override
    public Semester build() {
        console.println("Введите семестр обучения");
        console.println("Возможные семестры: " + Semester.nameAll());
        while (true) {
            String input = userInput.nextLine().trim();
            try {
                return Semester.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException exception) {
                console.printError("Такого семестра нет в списке");
                if (Console.isIsItInFile()) console.printError("Невалидные значения для цвета в файле");
            }
        }
    }
}
