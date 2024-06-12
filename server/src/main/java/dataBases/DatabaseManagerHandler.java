package dataBases;

import java.util.Objects;

public class DatabaseManagerHandler {
    private static DatabaseManager databaseManager;
    static {
        databaseManager = new DatabaseManager();
    }
    public static DatabaseManager getDatabaseManager(){
        if (Objects.isNull(databaseManager)) databaseManager = new DatabaseManager();
        return databaseManager;
    }
}
