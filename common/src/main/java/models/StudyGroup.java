package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

/**Функциональный интерфейс для сортировки*/
@FunctionalInterface
interface NameRange<T> {
    long getHashCode(T t);
}
/**Класс, отвечающий за информацию об учебной группе*/
public class StudyGroup implements Validator, Comparable<StudyGroup>, Serializable {
    /**Айди группы*/
    @SerializedName("ID")
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    /**Название группы*/
    @SerializedName("Name")
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**Координаты группы*/
    @SerializedName("Coordinates")
    private Coordinates coordinates; //Поле не может быть null
    /**Дата создания группы*/
    @SerializedName("CreationDate")
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    /**Количество студентов в группе*/
    @SerializedName("StudentsCount")
    private Long studentsCount; //Значение поля должно быть больше 0, Поле не может быть null
    /**Количество студентов на исключение*/
    @SerializedName("ShouldBeExpelled")
    private Integer shouldBeExpelled; //Значение поля должно быть больше 0, Поле может быть null
    /**Форма обучения*/
    @SerializedName("FormOfEducation")
    private FormOfEducation formOfEducation; //Поле может быть null
    /**Учебный семестр*/
    @SerializedName("SemesterEnum")
    private Semester semesterEnum; //Поле может быть null
    /**Староста*/
    @SerializedName("GroupAdmin")
    private Person groupAdmin; //Поле может быть null
    private static int newId = 0;

    public StudyGroup(String name, Coordinates coordinates, Long studentsCount, Integer shouldBeExpelled, FormOfEducation formOfEducation, Semester semesterEnum, Person groupAdmin) {
        this.id = incNewID();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.studentsCount = studentsCount;
        this.shouldBeExpelled = shouldBeExpelled;
        this.formOfEducation = formOfEducation;
        this.semesterEnum = semesterEnum;
        this.groupAdmin = groupAdmin;
    }

    private int incNewID(){
        return ++newId;
    }

    public static int newID() {
        int nID = newId;
        ++newId;
        return ++nID;
    }

    public static void updateID(Collection<StudyGroup> collection) { // steam api
        newId = collection.stream()
                .filter(Objects::nonNull)
                .map(StudyGroup::getId)
                .max(Integer::compareTo)
                .orElse(0);
    }

    /** Метод проверяющий правильность полей
     * @return правильны ли поля*/
    @Override
    public boolean validate() {
        if (this.name == null || this.name.isEmpty()) return false;
        if (this.coordinates == null) return false;
        if (this.studentsCount <= 0) return false;
        return this.shouldBeExpelled > 0;
    }
    /** Метод, определяющий сортировку коллекции с объектами класса StudyGroup
     * @param o Объект класса StudyGroup, который нужно сравнить с данным
     * @return 1 - если данный "больше" о, -1 - если о "больше" данного, 0 - если они равны
     * */
    @Override
    public int compareTo(StudyGroup o) {
        if (Objects.isNull(o)) return 0;
        NameRange<String> comp = String::hashCode;
        String n1 = this.getName();
        String n2 = o.getName();
        return Integer.compare((int) comp.getHashCode(n1), (int) comp.getHashCode(n2));
    }

    public int getDistance() {
        return (int) Math.sqrt(Math.pow(coordinates.getX(), 2) + Math.pow(coordinates.getY(), 2));
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public Long getStudentsCount() {
        return studentsCount;
    }

    public void setStudentsCount(Long studentsCount) {
        this.studentsCount = studentsCount;
    }

    public Integer getShouldBeExpelled() {
        return shouldBeExpelled;
    }

    public void setShouldBeExpelled(Integer shouldBeExpelled) {
        this.shouldBeExpelled = shouldBeExpelled;
    }

    public FormOfEducation getFormOfEducation() {
        return formOfEducation;
    }

    public void setFormOfEducation(FormOfEducation formOfEducation) {
        this.formOfEducation = formOfEducation;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }

    public void setSemesterEnum(Semester semesterEnum) {
        this.semesterEnum = semesterEnum;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    @Override
    public String toString() {
        return "StudyGroup{ " +
                "id = " + id +
                ", name = '" + name + '\'' +
                ", coordinates = " + coordinates +
                ", creationDate = " + creationDate +
                ", studentsCount = " + studentsCount +
                ", shouldBeExpelled = " + shouldBeExpelled +
                ", formOfEducation = " + formOfEducation +
                ", semesterEnum = " + semesterEnum +
                ", groupAdmin = " + groupAdmin +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudyGroup that = (StudyGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(coordinates, that.coordinates) && Objects.equals(creationDate, that.creationDate) && Objects.equals(studentsCount, that.studentsCount) && Objects.equals(shouldBeExpelled, that.shouldBeExpelled) && formOfEducation == that.formOfEducation && semesterEnum == that.semesterEnum && Objects.equals(groupAdmin, that.groupAdmin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, studentsCount, shouldBeExpelled, formOfEducation, semesterEnum, groupAdmin);
    }
}
