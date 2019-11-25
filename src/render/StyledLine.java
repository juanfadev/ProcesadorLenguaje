package render;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StyledLine  implements StyledBlock{
    List<StyledContent> strings;
    Map<String, String> paragraphProperties;

    public StyledLine() {
        this.strings = new ArrayList<>();
    }

    public void addStyledString (StyledContent s){
        strings.add(s);
    }

    public List<StyledContent> getStrings() {
        return strings;
    }

    public String getLineString(){
        return strings.stream().map(StyledContent::getString).reduce("", String::concat);
    }
}
