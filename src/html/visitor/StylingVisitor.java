package html.visitor;

import html.ast.Body;
import html.ast.HTMLProgram;
import html.ast.Head;
import html.ast.headElements.Link;
import html.ast.headElements.Title;
import html.ast.htmlElements.H1;
import html.ast.htmlElements.H2;
import html.ast.htmlElements.InnerPElements.B;
import html.ast.htmlElements.InnerPElements.I;
import html.ast.htmlElements.InnerPElements.StringElement;
import html.ast.htmlElements.InnerPElements.U;
import html.ast.htmlElements.P;

public class StylingVisitor implements HTMLVisitor {
    @Override
    public Object visit(HTMLProgram htmlProgram, Object param) {
        return null;
    }

    @Override
    public Object visit(Head htmlProgram, Object param) {
        return null;
    }

    @Override
    public Object visit(Body htmlProgram, Object param) {
        return null;
    }

    @Override
    public Object visit(P html, Object param) {
        return null;
    }

    @Override
    public Object visit(H1 html, Object param) {
        return null;
    }

    @Override
    public Object visit(H2 html, Object param) {
        return null;
    }

    @Override
    public Object visit(B html, Object param) {
        return null;
    }

    @Override
    public Object visit(I html, Object param) {
        return null;
    }

    @Override
    public Object visit(U html, Object param) {
        return null;
    }

    @Override
    public Object visit(StringElement html, Object param) {
        return null;
    }

    @Override
    public Object visit(Link link, Object param) {
        return null;
    }

    @Override
    public Object visit(Title title, Object param) {
        return null;
    }
}
