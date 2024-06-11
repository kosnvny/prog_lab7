package models;

/**Класс-перечисление, отвечающий за страну старосты*/
public enum Country {
    RUSSIA, UNITED_KINGDOM, INDIA, ITALY;
    /**Статический метод для вывода всех констант в строковом представлении
     * @return Строковое представление констант*/
    public static String nameAll(){
        StringBuilder nameAll = new StringBuilder();
        for (Country s : values()) {
            nameAll.append(s).append(" ");
        }
        return nameAll.toString();
    }
}
