package html.ast.headElements;

import html.ast.AbstractASTNode;
import html.ast.AttributesHTMLElement;
import html.visitor.HTMLVisitor;

import java.util.Map;

public class Link extends AbstractASTNode implements HeadElement, AttributesHTMLElement {
    private Map<String, String> attributes;

    public Link(int row, int col, Map<String, String> attributes) {
        super(row, col);
        this.attributes = attributes;
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    @Override
    public Object accept(HTMLVisitor v, Object param) {
        return v.visit(this, param);
    }
}
