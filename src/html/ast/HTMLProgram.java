package html.ast;
import html.visitor.HTMLVisitor;

public class HTMLProgram extends AbstractASTNode {
    private Head head;

    public Head getHead() {
        return head;
    }

    public Body getBody() {
        return body;
    }

    private Body body;

    public HTMLProgram(int row, int col, Head head, Body body) {
        super(row, col);
       this.head = head;
       this.body = body;
    }

    @Override
    public Object accept(HTMLVisitor v, Object param) {
        return v.visit(this, param);
    }
}

