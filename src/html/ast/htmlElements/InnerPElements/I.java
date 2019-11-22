package html.ast.htmlElements.InnerPElements;

import html.ast.AbstractASTNode;
import html.visitor.HTMLVisitor;

public class I extends AbstractASTNode implements InnerPElement {
    private String value;

    public I(int row, int col, String value) {
        super(row, col);
        this.value = value;
    }


    @Override
    public Object accept(HTMLVisitor v, Object param) {
        return v.visit(this, param);
    }

    @Override
    public String getValue() {
        return value;
    }
}
