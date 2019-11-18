package html.ast;
import html.visitor.HTMLVisitor;

public interface AstHTML {
    int getRow();
    int getCol();

    Object accept(HTMLVisitor v, Object param);

}
