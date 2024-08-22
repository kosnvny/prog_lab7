package managers;

import commandLine.Console;
import commandLine.Printable;
import dataBases.DatabaseManager;
import utility.Request;
import utility.RequestHandler;
import utility.Response;
import utility.ResponseStatus;

import java.io.*;
import java.nio.channels.SocketChannel;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConnectionManager implements Runnable {
    private final CommandManager commandManager;
    private final DatabaseManager databaseManager;
    private static final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);
    private final SocketChannel clientSocket;
    private final Printable console;


    public ConnectionManager(CommandManager commandManager, SocketChannel clientSocket, DatabaseManager databaseManager, Printable console) {
        this.commandManager = commandManager;
        this.clientSocket = clientSocket;
        this.databaseManager = databaseManager;
        this.console = console;
    }

    @Override
    public void run(){
        Request userRequest = null;
        Response responseToUser = null;
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.socket().getInputStream());
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(clientSocket.socket().getOutputStream());
            while (true){
                userRequest = (Request) objectInputStream.readObject();
                if(!databaseManager.confirmUser(userRequest.getUser())
                        && !userRequest.getCommandName().equals("register")){
                    console.printError("юзер не одобрен");
                    responseToUser = new Response(ResponseStatus.LOGIN_FAILED, "Неверный пользователь!");
                    submitNewResponse(new ConnectionManagerPool(responseToUser, objectOutputStream));
                } else{
                    FutureManager.addNewFixedThreadPoolFuture(fixedThreadPool.submit(new RequestHandler(commandManager, userRequest, objectOutputStream)));
                }
            }
        } catch (ClassNotFoundException exception) {
            console.printError("произошла ошибка при чтении полученных данных!");
        }catch (CancellationException exception) {
            console.printError("при обработке запроса произошла ошибка многопоточности!");
        } catch (InvalidClassException | NotSerializableException exception) {
            console.printError("произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            if (userRequest == null) {
                console.printError("непредвиденный разрыв соединения с клиентом!");
            } else {
                console.print("клиент успешно отключен от сервера!");
            }
        }
    }

    public static void submitNewResponse(ConnectionManagerPool connectionManagerPool){
        fixedThreadPool.submit(() -> {
            try {
                connectionManagerPool.getObjectOutputStream().writeObject(connectionManagerPool.getResponse());
                connectionManagerPool.getObjectOutputStream().flush();
            } catch (IOException e) {
                //connectionManagerLogger.error("Не удалось отправить ответ");
                //connectionManagerLogger.debug(e);
            }
        });
    }
}
