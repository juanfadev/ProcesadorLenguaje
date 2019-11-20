package html.ast.headElements;

import html.ast.AbstractASTNode;
import html.visitor.HTMLVisitor;

public class Title extends AbstractASTNode implements HeadElement {

    private String content;


    public Title(int row, int col, String content) {
        super(row, col);
        this.content = content;
    }

    @Override
    public Object accept(HTMLVisitor v, Object param) {
        return null;
    }
}
