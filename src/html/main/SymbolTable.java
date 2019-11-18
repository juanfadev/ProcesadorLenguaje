package html.main;

import css.ast.Rule;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {

    private int scope = 0;
    private Map<String, Rule> map;

    public SymbolTable() {
        map = new HashMap<>();
    }
/*
    public boolean insert(Rule definition) {
        Definition d = find(definition.getName());
        if (d != null)
            return false;
        return true;
    }

    public Definition find(String id) {
        if (map.containsKey(id))
            return map.get(id);
        return null;
    }*/
}
