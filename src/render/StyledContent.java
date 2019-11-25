package render;

public interface StyledContent {
    void addProperty(String key, String value);

    String getString();

    String getProperty(String key);
}
