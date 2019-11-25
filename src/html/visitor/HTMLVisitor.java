package html.visitor;

import html.ast.Body;
import html.ast.HTMLProgram;
import html.ast.Head;
import html.ast.headElements.Link;
import html.ast.headElements.Title;
import html.ast.htmlElements.*;
import html.ast.htmlElements.InnerPElements.*;

public interface HTMLVisitor {

    Object visit(HTMLProgram htmlProgram, Object param);
    Object visit(Head htmlProgram, Object param);
    Object visit(Body htmlProgram, Object param);
    Object visit(P html, Object param);
    Object visit(H1 html, Object param);
    Object visit(H2 html, Object param);
    Object visit(B html, Object param);
    Object visit(I html, Object param);
    Object visit(U html, Object param);
    Object visit(StringElement html, Object param);
    Object visit(Link link, Object param);
    Object visit(Title title, Object param);
    Object visit(IMG img, Object param);
    Object visit(A a, Object param);
    Object visit(InnerA innerA, Object param);
}
