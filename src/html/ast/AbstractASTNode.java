package html.ast;

public abstract class AbstractASTNode implements AstHTML {
    protected int row;

    public AbstractASTNode(int row, int col) {
        this.row = row;
        this.col = col;
    }

    protected int col;

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public int getCol() {
        return col;
    }


}

