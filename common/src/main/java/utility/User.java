package utility;

import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String password;
    private UserRoles role = UserRoles.CHILD;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRoles getRole() {
        return role;
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public void setRole(int i) {
        switch (i) {
            case 0 -> setRole(UserRoles.CHILD);
            case 1 -> setRole(UserRoles.TEEN);
            case 2 -> setRole(UserRoles.ADULT);
        }
    }
}
