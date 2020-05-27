package util;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class JsonUtil {
    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    public static String toJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }
}
