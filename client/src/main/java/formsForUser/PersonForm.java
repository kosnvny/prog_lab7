package formsForUser;

import commandLine.*;
import models.Person;
import utility.ExecuteScriptManager;

public class PersonForm extends Form<Person>{
    private final Printable console;
    private final UserInput userInput;
    public PersonForm(Printable console) {
        this.console = (Console.isIsItInFile() ? new BlankConsole() : console);
        this.userInput = (Console.isIsItInFile() ? new ExecuteScriptManager() : new ConsoleInput());
    }
    /**
     * Абстрактный метод, "строящий" новый объект заданного типа
     *
     * @return Person
     */
    @Override
    public Person build() {
        return new Person(askName(), askWeight(), new ColourForm(console).build(), new CountryForm(console).build());
    }

    private String askName() {
        console.println("Пожалуйста, введите имя человека, требования: не должно быть пустым или null");
        while (true) {
            String input = userInput.nextLine();
            if (!input.isBlank()) return input;
            console.printError("Имя не может быть пустым или null");
        }
    }

    private float askWeight() {
        console.println("Пожалуйста, введите вес, требование: должен быть больше 0");
        while (true) {
            String input = userInput.nextLine();
            try {
                float weight = Float.parseFloat(input);
                if (weight > 0) return weight;
                console.printError("Вес не может быть неположительным");
            } catch (NumberFormatException e) {
                console.printError("Вес должен быть типа float");
            }
        }
    }
}
