package html.ast.htmlElements;

import html.ast.AbstractASTNode;
import html.ast.htmlElements.InnerPElements.InnerPElement;
import html.visitor.HTMLVisitor;

import java.util.List;

public class P extends AbstractASTNode implements BodyElement {
    private List<InnerPElement> elements;

    public P(int row, int col, List<InnerPElement> elements) {
        super(row, col);
        this.elements = elements;
    }

    @Override
    public Object accept(HTMLVisitor v, Object param) {
        return null;
    }
}
