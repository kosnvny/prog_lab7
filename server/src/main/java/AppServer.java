import commandLine.Console;
import commandLine.Printable;
import commands.*;
import dataBases.DatabaseManager;
import exceptions.ForcedExit;
import managers.CollectionManager;
import managers.CommandManager;
import managers.FileManager;
import models.*;
import utility.RequestHandler;
import utility.ServerTCP;

import java.util.List;

public class AppServer {
    public static int port = 1399; // просто порт поменяла
    public static Printable console = new Console(); // BlankConsole поменяла, чтобы было видно, что делает сервер
    public static void main(String[] args) {
        CollectionManager collectionManager = new CollectionManager();
        FileManager fileManager = new FileManager(collectionManager, console);
        try {
            fileManager.readFile();
            fileManager.createObjects();
        } catch (ForcedExit e) {
            console.printError(e.getMessage());
            return;
        }
        CommandManager commandManager = new CommandManager(fileManager);
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
                new UpdateIDCommand(collectionManager)));
        RequestHandler requestHandler = new RequestHandler(commandManager);
        DatabaseManager databaseManager = new DatabaseManager();
        ServerTCP serverTCP = new ServerTCP(port, console, commandManager, databaseManager);
        serverTCP.run();
    }
}
