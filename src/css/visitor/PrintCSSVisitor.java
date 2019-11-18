package css.visitor;

import css.ast.Definition;
import css.ast.Program;
import css.ast.Rule;

public class PrintCSSVisitor extends AbstractCSSVisitor {

    String sp = "  ";

    @Override public Object visit(Program p, Object param){
        String sd = "", sr;
        for (Rule r : p.getRules()){
            sd = sd + (String) r.accept (this, sp);
        }
        sr = "(CSS declariations \n" + sd + ")";
        return sr;
    }

    @Override
    public Object visit(Rule r, Object param){
        String spar = "", sr;
        for (Definition d : r.getDefs()){
            spar = spar + (String) d.getName();
        }
        sr = "(RULES \n" + spar + ")";
        return sr;
    }
}
