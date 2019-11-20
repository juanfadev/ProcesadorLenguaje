package html.ast;

import html.ast.htmlElements.BodyElement;
import html.visitor.HTMLVisitor;

import java.util.List;

public class Body extends AbstractASTNode{
    private List<BodyElement> elements;

    public Body(int row, int col, List<BodyElement> elements) {
        super(row, col);
        this.elements = elements;
    }

    @Override
    public Object accept(HTMLVisitor v, Object param) {
        return null;
    }
}
