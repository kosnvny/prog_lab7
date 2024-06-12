import commandLine.Console;
import commandLine.Printable;
import commands.*;
import dataBases.DatabaseManagerHandler;
import exceptions.ForcedExit;
import managers.CollectionManager;
import managers.CommandManager;
import utility.ServerTCP;

import java.util.List;

public class AppServer extends Thread{
    public static int port = 1399; // просто порт поменяла
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
                new UpdateIDCommand(collectionManager)));
        ServerTCP serverTCP = new ServerTCP(port, console, commandManager, DatabaseManagerHandler.getDatabaseManager());
        serverTCP.run();
    }
}
