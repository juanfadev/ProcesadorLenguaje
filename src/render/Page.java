package render;

import java.util.List;

public class Page {
    List<StyledBlock> blocks;
    String title;

    public Page(List<StyledBlock> blocks, String title) {
        this.blocks = blocks;
        this.title = title;
    }

    public List<StyledBlock> getBlocks() {
        return blocks;
    }

    public String getTitle() {
        return title;
    }
}
