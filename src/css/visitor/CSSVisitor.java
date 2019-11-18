package css.visitor;

import css.ast.Definition;
import css.ast.Program;
import css.ast.Rule;

public interface CSSVisitor {
    Object visit (Program a, Object p);
    Object visit (Rule a, Object p);
    Object visit (Definition a, Object p);
}
