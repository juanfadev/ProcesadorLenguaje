package css.ast;

import css.visitor.CSSVisitor;

import java.util.List;

public class Rule extends AbstractASTNode {
    private String ident;
    private List<Definition> defs;

    public Rule(int row, int col, String ident, List<Definition> defs) {
        super(row, col);
        this.defs = defs;
        this.ident = ident;
    }

    @Override
    public Object accept(CSSVisitor v, Object param) {
        return v.visit(this, param);
    }

    public List<Definition> getDefs() {
        return defs;
    }

    public String getIdent() {
        return ident;
    }
}
