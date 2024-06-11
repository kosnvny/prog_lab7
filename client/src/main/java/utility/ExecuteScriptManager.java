package utility;

import commandLine.UserInput;

import java.io.*;
import java.util.LinkedList;

/**Класс для работы с вводом из файла*/
public class ExecuteScriptManager implements UserInput {
    /**{@link LinkedList}, хранящий пути выполненных файлов*/
    private static final LinkedList<String> filesToExecute = new LinkedList<>();
    /**{@link LinkedList}, хранящий файлы, которые мы использовали*/
    private static final LinkedList<BufferedReader> fileReaders = new LinkedList<>();

    /** Метод, запоминающий путь до файла и сам файл
     * @param args путь до файла
     * @throws FileNotFoundException Файл не найден*/
    public static void addFile(String args) throws FileNotFoundException {
        filesToExecute.push(new File(args).getAbsolutePath());
        fileReaders.push(new BufferedReader(new InputStreamReader(new FileInputStream(args))));
    }

    public static void pushFile(String path) throws FileNotFoundException{
        filesToExecute.push(new File(path).getAbsolutePath());
        fileReaders.push(new BufferedReader(new InputStreamReader(new FileInputStream(path))));
    }

    /** Метод, проверяющий скрипты на рекурсию
     * @param args путь до файла
     * @return были ли мы в этом файле (true - да, false - нет)*/
    public static boolean haveWeBeenInFile(String args) {
        return filesToExecute.contains(new File(args).getAbsolutePath());
    }

    /** Метод, возращающий следующую строку
     * @return Следующая строка
     * @throws IOException Нет следующей строки*/
    public static String readLine() throws IOException {
        return fileReaders.getFirst().readLine();
    }

    /** Метод, удаляющий файл и его путь
     * @throws IOException Невозможно закрыть поток*/
    public static void popFile() throws IOException {
        fileReaders.getFirst().close();
        fileReaders.pop();
        filesToExecute.pop();
    }

    public static void popRecursion(){
        if(!filesToExecute.isEmpty()) {
            filesToExecute.pop();
        }
    }

    @Override
    public String nextLine() {
        try{
            return readLine();
        } catch (IOException e){
            return "";
        }
    }
}
