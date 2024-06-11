package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**Класс, отвечающий за координаты*/
public class Coordinates implements Validator, Serializable {
    @SerializedName("x")
    private float x;
    @SerializedName("y")
    private Float y; //Значение поля должно быть больше -964, Поле не может быть null

    public Coordinates(float x, Float y) {
        this.x = x;
        this.y = y;
    }
    /** Метод проверяющий правильность полей
     * @return правильны ли поля*/
    @Override
    public boolean validate() {
        return this.y != null && this.y > -964;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Координата x=" + x + ", координата y=" + y + '}';
    }
}
