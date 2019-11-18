package css.visitor;

import css.ast.Definition;
import css.ast.Program;
import css.ast.Rule;

public class AbstractCSSVisitor implements CSSVisitor {
    @Override
    public Object visit(Program a, Object p) {
        return null;
    }

    @Override
    public Object visit(Rule a, Object p) {
        return null;
    }

    @Override
    public Object visit(Definition a, Object p) {
        return null;
    }
}
