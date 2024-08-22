package dataBases;

import commandLine.Console;
import commandLine.Printable;
import main.AppServer;
import models.*;
import utility.User;
import utility.UserRoles;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private Connection connection;
    private MessageDigest messageDigest;
    private final Printable console = new Console();
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" +
            "abcdefghijklmnopqrstuvwxyz1234567890,.<>?!@#$%^&*()-=_+:*№/|";
    public DatabaseManager() {
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            this.connect();
            this.createAllBases();
        } catch (NoSuchAlgorithmException e) {
            console.printError("такого алгоритма нет(");
        } catch (SQLException e) {
            console.printError("не получилось создать таблицы(");
        }
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(AppServer.DATABASE_URL, AppServer.USER_HELIOS, AppServer.PASSWORD_HELIOS);
        } catch (SQLException e) { // обработка отвала бд
            try {
                connection = DriverManager.getConnection(AppServer.DATABASE_URL_HELIOS, AppServer.USER_HELIOS, AppServer.PASSWORD_HELIOS);
            } catch (SQLException ex) {
                console.printError("невозможно подключится к базе данных (");
                System.exit(1);
            }
        }
    }

    public void addUser(User user) throws SQLException {
        String login = user.getLogin();
        String password = user.getPassword();
        UserRoles role = user.getRole();
        String salt = this.generateSalt();
        String pass = password  + salt;
        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseCommands.addUser);
        if (this.checkIfUserExists(login)) throw new SQLException();
        preparedStatement.setString(1, login);
        preparedStatement.setString(2, this.passwordSHA256(pass));
        preparedStatement.setString(3, role.toString());
        preparedStatement.setString(4, salt);
        preparedStatement.execute();
    }

    public boolean confirmUser(User inputUser){
        try {
            String login = inputUser.getLogin();
            PreparedStatement getUser = connection.prepareStatement(DatabaseCommands.getUser);
            getUser.setString(1, login);
            ResultSet resultSet = getUser.executeQuery();
            if(resultSet.next()) {
                String salt = resultSet.getString("salt");
                String toCheckPass = this.passwordSHA256(inputUser.getPassword() + salt);
                return toCheckPass.equals(resultSet.getString("password"));
            }
            else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean checkIfUserExists(String login) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(DatabaseCommands.getUser);
        preparedStatement.setString(1, login);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public int addObject(StudyGroup studyGroup, User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DatabaseCommands.addObject);
            preparedStatement.setString(1, studyGroup.getName());
            preparedStatement.setFloat(2, studyGroup.getCoordinates().getX());
            preparedStatement.setFloat(3, studyGroup.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(studyGroup.getCreationDate().getYear()));
            preparedStatement.setLong(5, studyGroup.getStudentsCount());
            preparedStatement.setInt(6, studyGroup.getShouldBeExpelled());
            preparedStatement.setObject(7, studyGroup.getFormOfEducation(), Types.OTHER);
            preparedStatement.setObject(8, studyGroup.getSemesterEnum(), Types.OTHER);
            preparedStatement.setString(9, studyGroup.getGroupAdmin().getName());
            preparedStatement.setFloat(10, studyGroup.getGroupAdmin().getWeight());
            preparedStatement.setObject(11, studyGroup.getGroupAdmin().getHairColor(), Types.OTHER);
            preparedStatement.setObject(12, studyGroup.getGroupAdmin().getNationality(), Types.OTHER);
            preparedStatement.setString(13, user.getLogin());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                console.printError("объект не был добален в коллекцию");
                return -1;
            }
            console.printError("объект был добален в коллекцию");
            return resultSet.getInt(1);
        } catch (SQLException e) {
            console.printError("объект не был добален в коллекцию");
            return -1;
        }
    }

    public boolean updateObject(int id, StudyGroup studyGroup, User user) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DatabaseCommands.updateUserObject);
            preparedStatement.setString(1, studyGroup.getName());
            preparedStatement.setFloat(2, studyGroup.getCoordinates().getX());
            preparedStatement.setFloat(3, studyGroup.getCoordinates().getY());
            preparedStatement.setTimestamp(4, new java.sql.Timestamp(studyGroup.getCreationDate().getYear()));
            preparedStatement.setLong(5, studyGroup.getStudentsCount());
            preparedStatement.setInt(6, studyGroup.getShouldBeExpelled());
            preparedStatement.setObject(7, studyGroup.getFormOfEducation(), Types.OTHER);
            preparedStatement.setObject(8, studyGroup.getSemesterEnum(), Types.OTHER);
            preparedStatement.setString(9, studyGroup.getGroupAdmin().getName());
            preparedStatement.setFloat(10, studyGroup.getGroupAdmin().getWeight());
            preparedStatement.setString(11, studyGroup.getGroupAdmin().getPassportID());
            preparedStatement.setObject(12, studyGroup.getGroupAdmin().getHairColor(), Types.OTHER);
            preparedStatement.setObject(13, studyGroup.getGroupAdmin().getNationality(), Types.OTHER);
            preparedStatement.setString(14, user.getLogin());
            preparedStatement.setInt(15, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteObject(int id, User user) {
        try{
            PreparedStatement ps = connection.prepareStatement(DatabaseCommands.deleteObject);
            ps.setString(1, user.getLogin());
            ps.setInt(2, id);
            ResultSet resultSet = ps.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean deleteAllObjects(User user, List<Integer> ids) {
        try {
            for (Integer id : ids) {
                PreparedStatement ps = connection.prepareStatement(DatabaseCommands.deleteObject);
                ps.setString(1, user.getLogin());
                ps.setInt(2, id);
                ResultSet resultSet = ps.executeQuery();
            }
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean changeRole(String login, String role) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE users SET role = " + "WHERE login = " + login);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private String generateSalt() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            stringBuilder.append(characters.charAt(random.nextInt(characters.length())));
        }
        return stringBuilder.toString();
    }

    public LinkedList<StudyGroup> loadCollection(){
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(DatabaseCommands.getAllObjects);
            ResultSet resultSet = preparedStatement.executeQuery();
            LinkedList<StudyGroup> collection = new LinkedList<>();
            while (resultSet.next()){
                collection.add(new StudyGroup(
                        resultSet.getString("group_name"),
                        new Coordinates(resultSet.getFloat("cord_x"), resultSet.getFloat("cord_y")),
                        resultSet.getLong("students_count"),
                        resultSet.getInt("expelled_students"),
                        FormOfEducation.valueOf(resultSet.getString("form_of_education")),
                        Semester.valueOf(resultSet.getString("semester")),
                        new Person(resultSet.getString("person_name"),
                                resultSet.getFloat("person_weight"),
                                Colour.valueOf(resultSet.getString("person_hair_color")),
                                Country.valueOf(resultSet.getString("person_nationality"))
                        ),
                        resultSet.getString("owner_login")
                ));
            }
            return collection;
        } catch (SQLException e) {
            return new LinkedList<>();
        }
    }

    private String passwordSHA256(String input) {
        byte[] inputBytes = input.getBytes();
        messageDigest.update(inputBytes);
        byte[] hashBytes = messageDigest.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private void createAllBases() throws SQLException {
        connection.prepareStatement(DatabaseCommands.creation).execute();
        console.println("Таблицы созданы");
    }
}
