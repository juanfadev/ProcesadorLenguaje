package render;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StyledLine  implements StyledBlock{
    List<StyledString> strings;
    Map<String, String> paragraphProperties;

    public StyledLine() {
        this.strings = new ArrayList<>();
    }

    public void addStyledString (StyledString s){
        strings.add(s);
    }

    public List<StyledString> getStrings() {
        return strings;
    }

    public String getLineString(){
        return strings.stream().map(s-> s.string).reduce("", String::concat);
    }
}
