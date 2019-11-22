package render;

import java.util.HashMap;
import java.util.Map;

public class StyledString {
    String string;
    Map<String, String> properties;

    public StyledString(String string) {
        this.properties = new HashMap<>();
        this.string = string;
    }

    public void addProperty(String key, String value){
        properties.put(key, value);
    }

    public String getString() {
        return string;
    }

    public String getProperty(String key){
        return properties.get(key);
    }
}
