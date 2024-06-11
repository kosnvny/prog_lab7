package commandLine;

/**Интерфейс для различных вариантов консоли*/
public interface Printable {
    void println(String args);
    void print(String args);
    void printError(String args);
}
