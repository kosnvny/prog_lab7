package formsForUser;

import commandLine.*;
import models.Coordinates;
import utility.ExecuteScriptManager;

public class CoordinatesForm extends Form<Coordinates>{
    private final Printable console;
    private final UserInput userInput;
    public CoordinatesForm(Printable console) {
        this.console = (Console.isIsItInFile() ? new BlankConsole() : console);
        this.userInput = (Console.isIsItInFile() ? new ExecuteScriptManager() : new ConsoleInput());
    }

    /**
     * Абстрактный метод, "строящий" новый объект заданного типа
     *
     * @return Coordinates
     */
    @Override
    public Coordinates build() {
        return new Coordinates(askX(), askY());
    }

    private float askX() {
        console.println("Пожалуйста, введите координату X");
        while (true) {
            String input = userInput.nextLine();
            try {
                return Float.parseFloat(input.trim());
            } catch (NumberFormatException e) {
                console.printError("координата X должна быть типа float");
            }
        }
    }

    private float askY() {
        console.println("Пожалуйста, введите координату Y, требование: координата Y должна быть > -964");
        while (true) {
            String input = userInput.nextLine();
            try {
                float y = Float.parseFloat(input.trim());
                if (y > -964) return y;
                else console.printError("координата Y должна быть > -964");
            } catch (NumberFormatException e) {
                console.printError("координата Y должна быть типа float");
            }
        }
    }
}
