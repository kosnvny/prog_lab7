package formsForUser;

import commandLine.*;
import models.Country;
import utility.ExecuteScriptManager;

public class CountryForm extends Form<Country>{
    private final Printable console;
    private final UserInput userInput;
    public CountryForm(Printable console) {
        this.console = (Console.isIsItInFile() ? new BlankConsole() : console);
        this.userInput = (Console.isIsItInFile() ? new ExecuteScriptManager() : new ConsoleInput());
    }
    /**
     * Абстрактный метод, "строящий" новый объект заданного типа
     * @return Country
     */
    @Override
    public Country build() {
        console.println("Введите страну");
        console.println("Возможные страны: " + Country.nameAll());
        while (true) {
            String input = userInput.nextLine().trim();
            try{
                return Country.valueOf(input.toUpperCase());
            } catch (IllegalArgumentException exception){
                console.printError("Такой страны нет в списке");
                if (Console.isIsItInFile()) console.printError("Невалидные значения для цвета в файле");
            }
        }
    }
}
