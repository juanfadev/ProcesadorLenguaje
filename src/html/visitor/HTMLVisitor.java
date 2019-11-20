package html.visitor;

import html.ast.Body;
import html.ast.HTMLProgram;
import html.ast.Head;
import html.ast.htmlElements.H1;
import html.ast.htmlElements.H2;
import html.ast.htmlElements.InnerPElements.B;
import html.ast.htmlElements.InnerPElements.I;
import html.ast.htmlElements.InnerPElements.StringElement;
import html.ast.htmlElements.InnerPElements.U;
import html.ast.htmlElements.P;

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

}
