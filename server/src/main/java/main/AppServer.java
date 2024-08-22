package main;

import commandLine.Console;
import commandLine.Printable;
import commands.*;
import dataBases.DatabaseManagerHandler;
import managers.CollectionManager;
import managers.CommandManager;
import utility.ServerTCP;

import java.util.List;

public class AppServer extends Thread{
    public static final String DATABASE_URL = "jdbc:postgresql://pg:5432/studs";
    public static final String DATABASE_URL_HELIOS = "jdbc:postgresql://localhost:5432/studs";
    public static final String USER_HELIOS = "s409132";
    public static final String PASSWORD_HELIOS = "pCkh5rlt0jOUBMLS";
    public static int port = 9524; // просто порт поменяла
    public static Printable console = new Console(); // BlankConsole поменяла, чтобы было видно, что делает сервер
    public static void main(String[] args) {
        CollectionManager collectionManager = new CollectionManager();
        CommandManager commandManager = new CommandManager();
        commandManager.addCommands(List.of(new AddCommand(collectionManager),
                new ClearCommand(collectionManager),
                new CountLessThanSemesterEnumCommand(collectionManager),
                new ExecuteScriptCommand(),
                new ExitCommand(),
                new HeadCommand(collectionManager),
                new HelpCommand(commandManager),
                new InfoCommand(collectionManager),
                new PrintDescendingCommand(collectionManager),
                new PrintUniqueSemesterEnumCommand(collectionManager),
                new RemoveByIDCommand(collectionManager),
                new RemoveFirstCommand(collectionManager),
                new RemoveGreaterCommand(collectionManager),
                new ShowCommand(collectionManager),
                new UpdateIDCommand(collectionManager),
                new LogIn(),
                new LogUp(DatabaseManagerHandler.getDatabaseManager())));
        ServerTCP serverTCP = new ServerTCP(port, console, commandManager, DatabaseManagerHandler.getDatabaseManager());
        console.println("начинаем работу!");
        serverTCP.run();
    }
}
