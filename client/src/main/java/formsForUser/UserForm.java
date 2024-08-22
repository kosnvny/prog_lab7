package formsForUser;

import commandLine.*;
import exceptions.InvalideForm;
import utility.ExecuteScriptManager;
import utility.User;
import utility.UserRoles;

import java.util.Objects;
import java.util.Random;

public class UserForm extends Form<User>{
    private final Printable console;
    private final UserInput scanner;

    public UserForm(Printable console) {
        this.console = (Console.isIsItInFile())
                ? new BlankConsole()
                : console;
        this.scanner = (Console.isIsItInFile())
                ? new ExecuteScriptManager()
                : new ConsoleInput();
    }

    @Override
    public User build() throws InvalideForm {
        User user = new User(
                askLogin(),
                askPassword()
        );
        setRoleForUser(user);
        console.println("ваша роль: " + user.getRole());
        return user;
    }

    public boolean askIfLogin(){
        for(;;) {
            console.print("У вас уже есть аккаунт? [yn]  ");
            String input = scanner.nextLine().trim().toLowerCase();
            switch (input){
                case "y" -> {
                    return true;
                }
                case "n" -> {
                    return false;
                }
                default -> console.printError("Ответ не распознан");
            }
        }
    }

    private String askLogin() throws InvalideForm {
        String login;
        while (true){
            console.println("Введите ваш логин");
            login = scanner.nextLine().trim();
            if (login.isEmpty()){
                console.printError("Логин не может быть пустым");
                if (Console.isIsItInFile()) throw new InvalideForm("ошибка в логине");
            } else {
                return login;
            }
        }
    }

    private String askPassword() throws InvalideForm {
        String pass;
        while (true){
            console.println("Введите пароль");
            pass = (Objects.isNull(System.console()))
                    ? scanner.nextLine().trim()
                    : new String(System.console().readPassword());
            if (pass.isEmpty()){
                console.printError("Пароль не может быть пустым");
                if (Console.isIsItInFile()) throw new InvalideForm("ошибка в пароле");
            }
            else{
                return pass;
            }
        }
    }

    private void setRoleForUser(User user) {
        if (user.getLogin().contains("_admin")) {
            user.setRole(UserRoles.ADMIN);
        } else {
            int randint = new Random().nextInt(0, 3);
            user.setRole(randint);
        }
    }
}
