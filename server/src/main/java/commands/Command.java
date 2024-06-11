package commands;

import exceptions.*;
import utility.Request;
import utility.Response;

import java.io.Serializable;
import java.util.Objects;

/**
 * Абстрактный класс для всех команд*/
public abstract class Command implements Serializable {
    /**Название команды*/
    private final String name;
    /**Описание команды*/
    private final String description;
    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }
    /**Абстрактный метод для выполнения команды
     * @param request Аргументы команды
     * @return ответ на выполнение команды
     * @throws ForcedExit Различные неурядицы
     * @throws IllegalArguments Невалидные аргументы
     * @throws RecursionInScriptException Рекурсия в скриптах
     * @throws CommandDoesNotExist Команда не существует
     * @throws InvalideForm Невалидные данные для форм*/
    public abstract Response execute(Request request) throws ForcedExit, IllegalArguments, RecursionInScriptException, CommandDoesNotExist, InvalideForm;
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command = (Command) o;
        return Objects.equals(name, command.name) && Objects.equals(description, command.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description);
    }

    @Override
    public String toString() {
        return "Command{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
