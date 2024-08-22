package utility;

import commandLine.Printable;
import dataBases.DatabaseManager;
import exceptions.ConnectionErrorException;
import exceptions.StartingServerException;
import managers.ConnectionManager;
import managers.CommandManager;
import managers.FutureManager;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP {
    private final int port;
    private final Printable console;
    private ServerSocketChannel serverSocketChannel;
    private SocketChannel socketChannel;
    private final CommandManager commandManager;
    private final DatabaseManager databaseManager;
    private final ExecutorService fixedThreadPool = Executors.newFixedThreadPool(8);
    public ServerTCP(int port, Printable console, CommandManager commandManager, DatabaseManager databaseManager) {
        this.port = port;
        this.console = console;
        this.commandManager = commandManager;
        this.databaseManager = databaseManager;
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
            while(true){
                FutureManager.checkAllFutures();
                try{
                    fixedThreadPool.submit(new ConnectionManager(commandManager, connectToClient(), databaseManager, console));
                } catch (ConnectionErrorException  ignored){}
            }
        } catch (StartingServerException e) {
            console.printError("сервер не может быть запущен");
        }
        stop();
    }
}
