package models;

/**Класс-перечисление, отвечающий за цвет волос старосты*/
public enum Colour {
    GREEN, RED, YELLOW, WHITE;
    /**Статический метод для вывода всех констант в строковом представлении
     * @return Строковое представление констант*/
    public static String nameAll(){
        StringBuilder nameAll = new StringBuilder();
        for (Colour s : values()) {
            nameAll.append(s).append(" ");
        }
        return nameAll.toString();
    }
}
