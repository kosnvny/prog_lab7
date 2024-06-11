package utility;

import commandLine.Printable;
import exceptions.ConnectionErrorException;
import exceptions.ForcedExit;
import exceptions.StartingServerException;
import managers.FileManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ServerTCP {
    private final int port;
    private final Printable console;
    private ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    private final RequestHandler requestHandler;
    private final FileManager fileManager;
    BufferedInputStream bis = new BufferedInputStream(System.in);
    BufferedReader br = new BufferedReader(new InputStreamReader(bis));
    public ServerTCP(int port, Printable console, RequestHandler requestHandler, FileManager fileManager) {
        this.port = port;
        this.console = console;
        this.requestHandler = requestHandler;
        this.fileManager = fileManager;
    }

    private void openServerSocket() throws StartingServerException{ // открытие сокета
        try {
            SocketAddress socketAddress = new InetSocketAddress(port);
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(socketAddress);
            serverSocketChannel.configureBlocking(false);
        } catch (IOException exception) {
            console.printError("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new StartingServerException();
        }
    }

    private SocketChannel connectToClient() throws ConnectionErrorException { // подключение к клиенту
        try {
            socketChannel = serverSocketChannel.accept();
            return socketChannel;
        } catch (IOException exception) {
            throw new ConnectionErrorException();
        }
    }

    private boolean processClientRequest(SocketChannel clientSocket) { // обработка полученных запросов
        Request userRequest = null;
        Response response = null;
        try {
            Request request = getSocketObjet(clientSocket);
            console.println(request.toString());
            response = requestHandler.handle(request);
            sendSocketObject(clientSocket, response);
        } catch (ClassNotFoundException e) {
            console.printError("Произошла ошибка при чтении полученных данных!");
        } catch (InvalidClassException e) {
            console.printError("Произошла ошибка при отправке данных на клиент!");
        } catch (IOException e) {
            if (userRequest == null) {
                console.printError("Непредвиденный разрыв соединения с клиентом!");
            } else {
                console.println("Клиент успешно отключен от сервера!");
            }
        }
        return true;
    }

    private static Request getSocketObjet(SocketChannel channel) throws IOException, ClassNotFoundException { // чтение объекта из запроса
        ByteBuffer buffer = ByteBuffer.allocate(1024*10);
        while (true) {
            try {
                channel.read(buffer);
                buffer.mark();
                byte[] buf = buffer.array();
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(buf);
                ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
                return (Request) objectInputStream.readObject();
            } catch (StreamCorruptedException ignored) {}
        }
    }

    private static void sendSocketObject(SocketChannel socketChannel, Response response) throws IOException { // отправка объекта клиенту
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(response);
        objectOutputStream.flush();
        socketChannel.write(ByteBuffer.wrap(byteArrayOutputStream.toByteArray()));
    }

    private void stop() { // закрытие соединения
        class ClosingSocketException extends Exception{}
        try{
            if (socketChannel == null) throw new ClosingSocketException();
            socketChannel.close();
            serverSocketChannel.close();
        } catch (ClosingSocketException exception) {
            console.printError("Невозможно завершить работу еще не запущенного сервера(");
        } catch (IOException exception) {
            console.printError("Произошла ошибка при завершении работы сервера(");
        }
    }

    // run fast for your mother run fast for your father!
    public void run(){ // запуск соединения
        try{
            openServerSocket();
            while (true) {
                try {
                    if (br.ready()) {
                        String line = br.readLine();
                        if (line.equals("exit")) { // чтобы при exit коллекция сохранялась
                            fileManager.writeCollection();
                        }
                    }
                } catch (IOException ignored) {
                } catch (ForcedExit e) {
                    console.printError(e.getMessage());
                }
                try (SocketChannel clientSocket = connectToClient()) {
                    if(clientSocket == null) continue;
                    clientSocket.configureBlocking(false);
                    if(!processClientRequest(clientSocket)) break;
                } catch (ConnectionErrorException ignored) {
                } catch (IOException exception) {
                    console.printError("Произошла ошибка при попытке завершить соединение с клиентом!");
                }
            }
            stop();
        } catch (StartingServerException e) {
            console.printError("Сервер не может быть запущен");
        }
    }
}
