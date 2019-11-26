package html.ast.htmlElements.InnerPElements;

import html.ast.AbstractASTNode;
import html.ast.AttributesHTMLElement;
import html.visitor.HTMLVisitor;

import java.util.Map;

public class InnerA extends AbstractASTNode implements InnerPElement, AttributesHTMLElement {
    private Map<String, String> attributes;
    private String value;

    public InnerA(int row, int col, Map<String, String> attributes, String value) {
        super(row, col);
        this.attributes = attributes;
        this.value = value;
    }

    @Override
    public Map<String, String> getAttributes() {
        return this.attributes;
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
