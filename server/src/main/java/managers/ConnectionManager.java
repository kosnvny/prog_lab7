package managers;

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


    public ConnectionManager(CommandManager commandManager, SocketChannel clientSocket, DatabaseManager databaseManager) {
        this.commandManager = commandManager;
        this.clientSocket = clientSocket;
        this.databaseManager = databaseManager;
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
                    //connectionManagerLogger.info("Юзер не одобрен");
                    responseToUser = new Response(ResponseStatus.LOGIN_FAILED, "Неверный пользователь!");
                    submitNewResponse(new ConnectionManagerPool(responseToUser, objectOutputStream));
                } else{
                    FutureManager.addNewFixedThreadPoolFuture(fixedThreadPool.submit(new RequestHandler(commandManager, userRequest, objectOutputStream)));
                }
            }
        } catch (ClassNotFoundException exception) {
            //connectionManagerLogger.fatal("Произошла ошибка при чтении полученных данных!");
        }catch (CancellationException exception) {
            //connectionManagerLogger.warn("При обработке запроса произошла ошибка многопоточности!");
        } catch (InvalidClassException | NotSerializableException exception) {
            //connectionManagerLogger.error("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException exception) {
            if (userRequest == null) {
                //connectionManagerLogger.error("Непредвиденный разрыв соединения с клиентом!");
            } else {
                //connectionManagerLogger.info("Клиент успешно отключен от сервера!");
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
