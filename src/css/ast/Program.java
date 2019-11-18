package css.ast;

import css.visitor.CSSVisitor;

import java.util.List;

public class Program extends AbstractASTNode {
    private List<Rule> rules;

    public Program(int row, int col, List<Rule> rules) {
        super(row, col);
        this.rules = rules;
    }

    public List<Rule> getRules() {
        return rules;
    }

    @Override
    public Object accept(CSSVisitor v, Object param) {
        return v.visit(this, param);
    }
}

