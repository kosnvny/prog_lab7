package models;

/**Класс-перечисление, отвечающий за семестр*/
public enum Semester {
    FIRST, SECOND, THIRD, FOURTH, SIXTH;
    /**Статический метод для вывода всех констант в строковом представлении
     * @return Строковое представление констант*/
    public static String nameAll(){
        StringBuilder nameAll = new StringBuilder();
        for (Semester s : values()) {
            nameAll.append(s).append(" ");
        }
        return nameAll.toString();
    }
}
