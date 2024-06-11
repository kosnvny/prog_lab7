package utility;

import commandLine.Printable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Objects;

public class ClientTCP {
    private final String host;
    private final int port;
    private final Printable console;
    private Socket socket;
    private int countOfReconnections;
    private final int maxCountOfConnections;
    private final int reconnectionTimeout;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    public ClientTCP(String host, int port, int maxCountOfConnections, int reconnectionTimeout, Printable console) {
        this.host = host;
        this.port = port;
        this.maxCountOfConnections = maxCountOfConnections;
        this.reconnectionTimeout = reconnectionTimeout;
        this.console = console;
    }

    public void connectToServer() {
        try {
            if (countOfReconnections > 0) console.println("Повторим подключение!");
            this.socket = new Socket(host, port);
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            console.printError("ошибка при соединении с сервером");
        }
    }

    public void disconnectFromServer() {
        try {
            this.socket.close();
            objectOutputStream.close();
            objectInputStream.close();
        } catch (IOException e) {
            console.printError("нет подключения к серверу");
        }
    }

    public Response sendAndAskResponse(Request request) {
        while (true) {
            try {
                if (request.isEmpty()) return new Response(ResponseStatus.ERROR, "Запрос пуст!");
                if (Objects.isNull(objectOutputStream)) throw new IOException("OOS is null");
                objectOutputStream.writeObject(request);
                objectOutputStream.flush();
                this.objectInputStream = new ObjectInputStream(socket.getInputStream());
                Response response = (Response) objectInputStream.readObject();
                //this.disconnectFromServer(); //это был эксперимент, и он оказался удачным
                // здесь это не нужно, т.к. на сложных командах он отлетает на переподключение
                // upd он всё равно отлетает на переподключение :(
                countOfReconnections = 0;
                return response;
            } catch (IOException e) {
                //console.println(e.getMessage());
                //e.printStackTrace(); // проверка чё вообще происходит -> происходит какая-то белеберда, будем дебажить
                if (countOfReconnections == 0) {
                    console.println("Попробуем подключиться снова!");
                    connectToServer();
                    countOfReconnections++;
                    continue;
                } else {
                    console.printError("Соединение с сервером разорвано(");
                }
                try {
                    countOfReconnections++;
                    if (countOfReconnections >= maxCountOfConnections) {
                        return new Response(ResponseStatus.ERROR, "Превышено максимальное количество переподключений: " + maxCountOfConnections);
                    }
                    console.println("Повторно подключимся через " + reconnectionTimeout / 1000 + " секунд.");
                    Thread.sleep(reconnectionTimeout);
                    connectToServer();
                } catch (Exception ex) {
                    console.printError("Попытка соединения с сервером неуспешна(");
                    disconnectFromServer();
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
