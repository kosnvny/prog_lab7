import commandLine.Console;
import commandLine.Printable;
import utility.ClientTCP;
import utility.RuntimeManager;
import java.util.Scanner;

public class AppClient {
    private static final Printable console = new Console();
    public static void main(String[] args) {
        String host = "localhost";
        int port = 9524; // просто порт поменяла
        ClientTCP clientTCP = new ClientTCP(host, port, 3, 5000, console);
        new RuntimeManager(console, clientTCP, new Scanner(System.in)).letsGo();
    }
}
