import commandLine.Console;
import commandLine.Printable;
import utility.ClientTCP;
import utility.RuntimeManager;
import java.util.Scanner;
// ssh -p2222 -N -L 5432:127.0.0.1:5432 s409132@se.ifmo.ru если сервер локально
public class AppClient {
    private static final Printable console = new Console();
    public static void main(String[] args) {
        String host = "localhost";
        int port = 30000; // просто порт поменяла
        ClientTCP clientTCP = new ClientTCP(host, port, 3, 5000, console);
        new RuntimeManager(console, clientTCP, new Scanner(System.in)).letsGo();
    }
}
