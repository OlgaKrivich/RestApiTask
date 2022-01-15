package utils;

import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

public class Storage {

    private static Storage instance;
    private static Map<String, Object> storage = new HashMap<>();

    private Storage() {
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    public static void rememberThatThe(String key, Object value) {
        storage.put(key, Optional.ofNullable(value));
    }

    public void rememberTheResponse(Response value) {
        rememberThatThe("Response", value);
    }

    public static String whatIsThe(String key) {
        if (storage.containsKey(key)) {
            return storage.get(key).toString();
        } else {
            throw new NoSuchElementException("Key '" + key + "' not found");
        }
    }


}