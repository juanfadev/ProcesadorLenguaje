package render;

import java.util.Map;

public class UrlBlock implements StyledBlock {
    private Map<String, String> attributes;
    private StyledBlock innerBlock;

    public UrlBlock(Map<String, String> attributes, StyledBlock innerBlock) {
        this.attributes = attributes;
        this.innerBlock = innerBlock;
    }

    public StyledBlock getInnerBlock() {
        return innerBlock;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }
}
