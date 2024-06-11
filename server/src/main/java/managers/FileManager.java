package managers;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import commandLine.Console;
import commandLine.Printable;
import exceptions.ForcedExit;
import models.Person;
import models.StudyGroup;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedList;

// работа с файлом, хранящим коллекцию
/**Класс, работающий с файлом*/
public class FileManager {
    /**{@link CollectionManager}, в котором находится коллекция*/
    private final CollectionManager collectionManager;
    /***/
    private final Printable console;
    /**{@link Gson}-объект для чтения/записи JSON-файлов*/
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .serializeNulls()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeChecker())
            .create();
    public FileManager(CollectionManager collectionManager, Printable console) {
        this.collectionManager = collectionManager;
        this.console = console;
    }
    /**
     * Считывает содержимое файла.
     * @throws ForcedExit Файл некорректен
     */
    public void readFile() throws ForcedExit {
        String filePath = System.getenv("filePathToRead");
        if (filePath == null) return;
        if (filePath.isBlank()) throw new ForcedExit("Путь до файла должен содержаться в переменной окружения filePathToRead");
        if (!new File(filePath).isFile()) throw new ForcedExit("Вы ввели какую-то кразябру");
        console.println("Путь до файла получен");
        File file = new File(filePath);
        FileInputStream fis;
        BufferedInputStream bis;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            Console.setIsItInFile(true);
            while (bis.available() > 0) {
                stringBuilder.append((char) bis.read());
            }
            fis.close();
            bis.close();
            String text;
            if (stringBuilder.isEmpty()) text = "";
            else text = stringBuilder.toString();
            //utility.ExecuteScriptManager.addFile(filePath);
            utility.ScannerManager.setUsersScanner(text);
        } catch (IOException e) {
            throw new ForcedExit("В файле закралась ошибка");
        }
    }

    /**Метод для создания объектов из файла*/
    public void createObjects() throws ForcedExit {
        String filePath = System.getenv("filePathToWrite");
        if (filePath == null) return;
        if (filePath.isBlank()) throw new ForcedExit("Путь до файла должен содержаться в переменной окружения filePathToWrite");
        if (!new File(filePath).isFile()) throw new ForcedExit("Вы ввели какую-то кразябру");
        console.println("Путь до файла получен");
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file)) {
            var collectionType = new TypeToken<LinkedList<StudyGroup>>() {}.getType();
            BufferedInputStream bis = new BufferedInputStream(fis);
            StringBuilder jsonString = new StringBuilder();
            while(bis.available() > 0) {
                jsonString.append((char) bis.read());
            }
            if (jsonString.isEmpty()) jsonString = new StringBuilder("[]");
            LinkedList<StudyGroup> collection = gson.fromJson(jsonString.toString(), collectionType);
            collectionManager.addElements(collection);
            StudyGroup.updateID(collection);
            Person.setSeriesAndNumber(collection); //чтобы паспорта не повторялись (или паспорты)
            console.println("Коллекция успешна загружена!");
            fis.close();
            bis.close();
        } catch (FileNotFoundException e) {
            throw new ForcedExit("Файл не найден");
        } catch (IOException e) {
            throw new ForcedExit("Ввод не найден");
        }
    }

        /**
     * Записывает коллекцию в файл.
     * @throws ForcedExit Проблема с файлом (невозможно открыть или невалидный путь)
     */
    public void writeCollection() throws ForcedExit {
        String filePath = System.getenv("filePathToWrite");
        if (filePath == null) throw new ForcedExit("Путь до файла не обнаружен в переменной окружения filePathToWrite");
        if (filePath.isBlank()) throw new ForcedExit("Путь до файла должен содержаться в переменной окружения filePathToWrite");
        console.println("Путь до файла получен");
        try (OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8)){
            osw.write(gson.toJson(collectionManager.getCollection()));
            console.println("Коллекция записана в файл!");
        } catch (FileNotFoundException e) {
            throw new ForcedExit("Файл не был найден");
        } catch (IOException e) {
            throw new ForcedExit("Невозможно сохранить коллекцию в json-файл(");
        }
    }
}