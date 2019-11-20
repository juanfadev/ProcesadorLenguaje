package html.ast;

import html.ast.headElements.HeadElement;
import html.ast.headElements.Link;
import html.ast.headElements.Title;
import html.visitor.HTMLVisitor;

import java.util.LinkedList;
import java.util.List;

public class Head extends AbstractASTNode {
    //List of head elements. Now only has a constructor for title and link but could be extended with more elements.
    private List<HeadElement> headElements;

    public Head(int row, int col, Title title, Link link) {
        super(row, col);
        this.headElements = new LinkedList<>();
        this.headElements.add(title);
        this.headElements.add(link);
    }

    @Override
    public Object accept(HTMLVisitor v, Object param) {
        return v.visit(this,param);
    }
}
