package css.visitor;

public class IdentificationCSSVisitor extends AbstractCSSVisitor {
/*
    SymbolTable st = new SymbolTable();

    @Override
    public Object visit(Rule a, Object p) {
        if (!st.insert(a)) {
            new ErrorType(a.getRow(), a.getCol(), "Variable is already declared in this scope");
        }
        return null;
    }

    @Override
    public Object visit(Definition a, Object p) {
        if (!st.insert(a)){
            new ErrorType(a.getRow(), a.getCol(), "Function is already declared in this scope");
        }
        st.set();
        a.getType().accept(this, null);
        for (Statement s : a.statementList){
            s.accept(this, null);
        }
        st.reset();
        return null;
    }
*/
}
