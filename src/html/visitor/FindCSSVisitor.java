package html.visitor;

import html.ast.Body;
import html.ast.HTMLProgram;
import html.ast.Head;
import html.ast.headElements.HeadElement;
import html.ast.headElements.Link;
import html.ast.headElements.Title;
import html.ast.htmlElements.H1;
import html.ast.htmlElements.H2;
import html.ast.htmlElements.IMG;
import html.ast.htmlElements.InnerPElements.B;
import html.ast.htmlElements.InnerPElements.I;
import html.ast.htmlElements.InnerPElements.StringElement;
import html.ast.htmlElements.InnerPElements.U;
import html.ast.htmlElements.P;

public class FindCSSVisitor implements HTMLVisitor {
    @Override
    public Object visit(HTMLProgram htmlProgram, Object param) {
        String cssFile = null;
        cssFile = (String) htmlProgram.getHead().accept(this, null);
        return cssFile.replace("\"", "");
    }

    @Override
    public Object visit(Head head, Object param) {
        StringBuilder s = new StringBuilder();
        for (HeadElement bE : head.getHeadElements()) {
            String a = (String) bE.accept(this, null);
            if (a != null) {
                s.append(a);
            }
        }
        return s.toString();
    }

    @Override
    public Object visit(Body body, Object param) {
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
        String rel = link.getAttributes().get("rel");
        String type = link.getAttributes().get("type");
        if (rel.equals("stylesheet") && type.equals("text/css")) {
            return link.getAttributes().get("href");
        }
        return null;
    }

    @Override
    public Object visit(Title title, Object param) {
        return null;
    }

    @Override
    public Object visit(IMG img, Object param) {
        return null;
    }
}
