package models;

/**Класс-перечисление, отвечающий за форму обучения*/
public enum FormOfEducation {
    DISTANCE_EDUCATION, FULL_TIME_EDUCATION, EVENING_CLASSES;
    /**Статический метод для вывода всех констант в строковом представлении
     * @return Строковое представление констант*/
    public static String nameAll(){
        StringBuilder nameAll = new StringBuilder();
        for (FormOfEducation s : values()) {
            nameAll.append(s).append(" ");
        }
        return nameAll.toString();
    }
}
