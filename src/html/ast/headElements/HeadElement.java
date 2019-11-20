package html.ast.headElements;

import html.visitor.HTMLVisitor;

public interface HeadElement {
    Object accept(HTMLVisitor v, Object param);
}
