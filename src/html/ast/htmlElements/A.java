package html.ast.htmlElements;

import html.ast.AbstractASTNode;
import html.ast.AttributesHTMLElement;
import html.visitor.HTMLVisitor;

import java.util.Map;

public class A extends AbstractASTNode implements BodyElement, AttributesHTMLElement {
    private Map<String, String> attributes;
    private BodyElement innerElement;

    public A(int row, int col, Map<String, String> attributes, BodyElement innerElement) {
        super(row, col);
        this.attributes = attributes;
        this.innerElement = innerElement;
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public BodyElement getInnerElement() {
        return innerElement;
    }

    @Override
    public Object accept(HTMLVisitor v, Object param) {
        return v.visit(this, param);
    }
}
