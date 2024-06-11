package managers;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**Класс для сериализации и десериализации дат в JSON*/
public class LocalDateTimeChecker implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime>{

    /** Метод для сериализации дат
     * @param date дата
     * @param context -
     * @param typeOfSrc -
     * @return элемент в формате JSON*/
    @Override
    public JsonElement serialize(LocalDateTime date, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }

    /** Метод для десериализации JSON-элемента в дату
     * @param json JSON-элемента
     * @param type -
     * @param context -
     * @return дата*/
    @Override
    public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString());
    }
}
