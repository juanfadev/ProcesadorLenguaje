package render;

import java.util.HashMap;
import java.util.Map;

public class StyledString implements StyledContent {
    String string;
    Map<String, String> properties;

    public StyledString(String string) {
        this.properties = new HashMap<>();
        this.string = string;
    }

    @Override
    public void addProperty(String key, String value){
        properties.put(key, value);
    }

    @Override
    public String getString() {
        return string;
    }

    @Override
    public String getProperty(String key){
        return properties.get(key);
    }
}
