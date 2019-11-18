package css.visitor;

import css.ast.AstCss;
import css.ast.Definition;
import css.ast.Program;
import css.ast.Rule;

public class BuscamParamCSSVisitor extends AbstractCSSVisitor {
    String label = null;
    String ident = null;

    @Override
    public Object visit (Program p, Object param){
        for (Rule r : p.getRules()){
            if (r.getIdent().equals(ident)){
                return (String) r.accept(this, null);
            }
        }
        return null;
    }

    @Override
    public Object visit(Rule r, Object param){
        for (Definition d : r.getDefs()){
            if (d.getName().equals(label)){
                return (String) d.accept (this,null);
            }
        }
        return null;
    }

    @Override
    public Object visit (Definition d, Object param){
        return d.getValue();
    }

    public String search (String ident, String label, AstCss prog){
        this.ident = ident;
        this.label = label;

        if ((ident == null) || (label == null)){
            return null;
        }
        return (String) prog.accept(this,null);
    }

}
