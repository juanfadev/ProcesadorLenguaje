package css.ast;

import css.visitor.CSSVisitor;


public class Definition extends AbstractASTNode {
    protected String value;
    protected String name;

    public Definition(int row, int col, String name, String value) {
        super(row, col);
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public Object accept(CSSVisitor v, Object param) {
        return v.visit(this, param);
    }
}
