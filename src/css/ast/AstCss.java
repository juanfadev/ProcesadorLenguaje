package css.ast;
import css.visitor.CSSVisitor;

public interface AstCss {
    int getRow();
    int getCol();

    Object accept(CSSVisitor v, Object param);

}
