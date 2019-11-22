package render;

import java.util.Map;

public class ImageBlock implements StyledBlock {
    private Map<String, String> attributes;

    public ImageBlock(Map<String, String> attributes) {
        this.attributes = attributes;
    }


    public Map<String, String> getAttributes() {
        return attributes;
    }
}
