package html.ast.htmlElements;

import html.ast.AbstractASTNode;
import html.visitor.HTMLVisitor;

public class H1 extends AbstractASTNode implements BodyElement {
    private String content;

    public H1(int row, int col, String content) {
        super(row, col);
        this.content = content;
    }

    @Override
    public Object accept(HTMLVisitor v, Object param) {
            return v.visit(this, param);
    }
}
