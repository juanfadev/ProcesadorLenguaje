package html.ast.htmlElements;

import html.visitor.HTMLVisitor;

public interface BodyElement {
    Object accept(HTMLVisitor v, Object param);
}
