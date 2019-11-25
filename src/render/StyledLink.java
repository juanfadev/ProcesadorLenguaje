package render;

import java.util.HashMap;
import java.util.Map;

public class StyledLink implements StyledContent {
    String string;
    Map<String, String> properties;
    Map<String, String> attributes;

    public StyledLink(String string, Map<String, String> attributes) {
        this.properties = new HashMap<>();
        this.string = string;
        this.attributes = attributes;
    }

    @Override
    public void addProperty(String key, String value){
        properties.put(key, value);
    }


    @Override
    public String getString() {
        return string;
    }

    public String getAttribute(String key){
        return attributes.get(key);
    }
    @Override
    public String getProperty(String key){
        return properties.get(key);
    }
}
