package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**Класс, отвечающий за человека*/
public class Person implements Validator, Serializable {
    /**Имя человека*/
    @SerializedName("Name")
    private String name; //Поле не может быть null, Строка не может быть пустой
    /**Вес человека*/
    @SerializedName("Weight")
    private Float weight; //Поле может быть null, Значение поля должно быть больше 0
    /**Паспортные данные человека*/
    @SerializedName("PassportID")
    private String passportID; //Значение этого поля должно быть уникальным, Длина строки должна быть не меньше 5
    /**Цвет волос человека*/
    @SerializedName("HairColor")
    private Colour hairColor; //Поле не может быть null
    /**Страна, откуда приехал человек*/
    @SerializedName("Nationality")
    private Country nationality; //Поле может быть null
    private static Long seriesAndNumber = 911L;
    public Person(String name, Float weight, Colour hairColor, Country nationality) {
        this.name = name;
        this.weight = weight;
        this.passportID = setSeriesAndNumber();
        this.hairColor = hairColor;
        this.nationality = nationality;
    }
    private String setSeriesAndNumber() {
        seriesAndNumber = 11*seriesAndNumber + 5;
        return seriesAndNumber.toString();
    }

    public static void setSeriesAndNumber(Collection<StudyGroup> collection) {
        long maxx = 0;
        for(StudyGroup sG: collection) {
            if (Long.parseLong(sG.getGroupAdmin().passportID) > maxx) {
                maxx = Long.parseLong(sG.getGroupAdmin().passportID);
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Colour getHairColor() {
        return hairColor;
    }

    public void setHairColor(Colour hairColor) {
        this.hairColor = hairColor;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    /** Метод проверяющий правильность полей
     * @return правильны ли поля*/
    @Override
    public boolean validate() {
        if (this.name == null || this.name.isEmpty()) return false;
        if (this.weight <= 0) return false;
        if (this.hairColor == null) return false;
        return true;
    }

    @Override
    public String toString() {
        return  "name='" + name + '\'' +
                ", weight=" + weight +
                ", passportID='" + passportID + '\'' +
                ", hairColor=" + hairColor +
                ", nationality=" + nationality +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(name, person.name) && Objects.equals(weight, person.weight) && Objects.equals(passportID, person.passportID) && hairColor == person.hairColor && nationality == person.nationality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, weight, passportID, hairColor, nationality);
    }
}
