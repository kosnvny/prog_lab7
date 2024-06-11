package managers;

import exceptions.IllegalArguments;
import models.Semester;
import models.StudyGroup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

// управление коллекцией объектов с серверной стороны
/**Класс для хранения коллекции и работы с ней*/
public class CollectionManager {
    /**Коллекция*/
    private LinkedList<StudyGroup> collection = new LinkedList<>();
    /**Дата создания коллекции*/
    private LocalDateTime lastInitTime;
    /**Дата последнего сохранения*/
    private LocalDateTime lastSaveTime;
    private final HashSet<Semester> semesters = new HashSet<>();
    public CollectionManager() {
        this.lastInitTime = LocalDateTime.now();
        this.lastSaveTime = null;
    }

    public LinkedList<StudyGroup> getCollection() {
        return collection;
    }

    /** Метод, который скрывают дату, сели она сегодняшняя (остаётся только время)
     * @param localDateTime дата, которую мыы проверяем на "сегодняшность" //да, я немного Маяковский
     * @return дата в строковом представлении
     * */
    public String timeFormatChange(LocalDateTime localDateTime) {
        if (localDateTime == null) return null;
        if (localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")).equals(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
            return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        }
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**Метод проверяющий, существует ли учебная группа с заданным id в коллекции
     * @param id id учебной группы
     * @return true, если существует, false, если нет*/
    public boolean checkIfExists(int id) {
        for (StudyGroup sG: collection) {
            if (sG.getId() == id) return true;
        }
        return false;
    }
    public String getLastInitTime() {
        return timeFormatChange(lastInitTime);
    }

    public String getLastSaveTime() {
        return timeFormatChange(lastSaveTime);
    }

    public String getCollectionType() {
        return collection.getClass().getName();
    }

    public void setLastSaveTime(LocalDateTime lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    /**Метод, выводящий информацию о коллекции (тип, размер, дата последнего изменения, дата последнего сохранения)
     * @return строковое представление информации о коллекции*/
    public String info() {
        return "Тип коллекции: " + getCollectionType() + ", размер коллекции: "
                + getCollectionSize() + ", дата последнего изменения: " + getLastInitTime() +
                ", дата последнего сохранения: " + getLastSaveTime();
    }

    public int getCollectionSize() {
        return collection.size();
    }

    /** Метод, которые выводит элементы коллекции в строковом представлении
     * @return строковое представление элементов коллекции
     * */
    public String show() {
        StringBuilder stringBuilder = new StringBuilder();
        StudyGroup studyGroup = collection.getLast();
        for (StudyGroup sG : collection) {
            if (sG == studyGroup) stringBuilder.append(sG.toString());
            else stringBuilder.append(sG.toString()).append("\n");
        }
        return stringBuilder.toString();
    }

    /**Метод, очищающий коллекцию*/
    public void clearCollection() {
        collection.clear();
        lastInitTime = LocalDateTime.now();
    }

    /**Метод, удаляющий первый элемент коллекции*/
    public void removeFirstElement() {
        collection.remove(0);
        lastInitTime = LocalDateTime.now();
    }

    /**Метод, ищущий уникальные значения поля Semester
     * @return Строковое представление уникальных полей Semester коллекции*/
    public String printUniqueSemester() {
        for(StudyGroup sG : collection) {
            semesters.add(sG.getSemesterEnum());
        }
        StringBuilder text = new StringBuilder();
        for (Semester s: semesters) {
            text.append(s.toString()).append(" ");
        }
        return text.toString();
    }

    /**Метод, считающий сколько элементов коллекции меньше заданного по полю Semester
     * @return Строковое представление количества элементов*/
    public String countLessThanSemester(Semester semester) {
        int count = 0;
        for(StudyGroup sG : collection) {
            if (sG.getSemesterEnum().ordinal() < semester.ordinal()) {
                count++;
            }
        }
        return Integer.toString(count);
    }

    /**Метод, сортирующий коллекцию в обратном порядке
     * @return Строковое представление отсортированной коллекции*/
    public String descendingOrder() {
        Collections.sort(collection);
        Collections.reverse(collection);
        StudyGroup studyGroup = collection.getLast();
        StringBuilder stringBuilder = new StringBuilder();
        for (StudyGroup sG : collection) {
            if (sG == studyGroup) stringBuilder.append(sG.toString());
            else stringBuilder.append(sG.toString()).append("\n");
        }
        lastInitTime = LocalDateTime.now();
        return stringBuilder.toString();
    }

    /**Метод, возвращающий первый элемент коллекции
     * @return Первый элемент коллекции*/
    public StudyGroup head() {
        return collection.getFirst();
    }

    /**Метод, удаляющий элемент по заданному айди
     * @param id Айди, по которому нужно удалить элемент*/
    public void removeByID(int id) throws IllegalArguments {
        if (checkIfExists(id)) {
            collection.removeIf(sG -> sG.getId() == id);
            lastInitTime = LocalDateTime.now();
        }
        else throw new IllegalArguments("Такого ID не существует");
    }

    /**Метод, удаляющий элемент, который больше заданного
     * @param studyGroup Элемент, с которым сравниваем все элементы коллекции
     * @return Строковое представление новой коллекции
     * */
    public String removeGreater(StudyGroup studyGroup) {
        for(StudyGroup sG : collection) {
            if (studyGroup.compareTo(sG) < 0) {
                collection.remove(sG);
            }
        }
        lastInitTime = LocalDateTime.now();
        return collection.toString();
    }

    /**Метод, добавляющий новый элемент в коллекцию
     * @param studyGroup Добавляемый элемент*/
    public void addElement(StudyGroup studyGroup) {
        collection.add(studyGroup);
        lastInitTime = LocalDateTime.now();
    }

    /**Метод, добавляющий коллекцию в первоначальную
     * @param collection Добавляемая коллекция*/
    public void addElements(Collection<StudyGroup> collection) {
        if (collection == null) return;
        this.collection.addAll(collection);
        lastInitTime = LocalDateTime.now();
    }

    /**Метод, обновляющий значение по айди
     * @param id Айди, по которому мы изменяем элемент
     * @param studyGroup Новый элемент
     * @throws IllegalArguments Невалидные аргументы*/
    public void updateID(int id, StudyGroup studyGroup) throws IllegalArguments {
        if (checkIfExists(id)) {
            for(StudyGroup sG: collection) {
                if (sG.getId() == id) {
                    studyGroup.setId(id);
                    collection.set(collection.indexOf(sG), studyGroup);
                    break;
                }
            }
            lastInitTime = LocalDateTime.now();
        } else {
            throw new IllegalArguments("Такого ID не существует");
        }
    }
}
