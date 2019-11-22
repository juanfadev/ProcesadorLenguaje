package html.visitor;

import css.ast.AstCss;
import css.visitor.BuscamParamCSSVisitor;
import html.ast.Body;
import html.ast.HTMLProgram;
import html.ast.Head;
import html.ast.headElements.Link;
import html.ast.headElements.Title;
import html.ast.htmlElements.BodyElement;
import html.ast.htmlElements.H1;
import html.ast.htmlElements.H2;
import html.ast.htmlElements.InnerPElements.*;
import html.ast.htmlElements.P;
import render.StyledLine;
import render.StyledString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RenderVisitor implements HTMLVisitor {
    AstCss astCss;
    BuscamParamCSSVisitor css = new BuscamParamCSSVisitor();
    List<String> properties = Collections.unmodifiableList(new ArrayList<String>() {{
        add("color");
        add("font-size");
        add("text-align");
        add("font-style");
    }});

    @Override
    public Object visit(HTMLProgram htmlProgram, Object param) {
        astCss = (AstCss) param;
        return htmlProgram.getBody().accept(this, null);
    }

    @Override
    public Object visit(Head htmlProgram, Object param) {
        return null;
    }

    @Override
    public Object visit(Body htmlProgram, Object param) {
        List<StyledLine> bodyElements = new ArrayList<>();
        for (BodyElement e : htmlProgram.getElements()) {
            bodyElements.add((StyledLine) e.accept(this, null));
        }
        return bodyElements;
    }

    @Override
    public Object visit(P p, Object param) {
        StyledLine line = new StyledLine();
        for (InnerPElement e : p.getElements()) {
            line.addStyledString((StyledString) e.accept(this, "p"));
        }
        return line;
    }

    @Override
    public Object visit(H1 html, Object param) {
        StyledLine sL = new StyledLine();
        StyledString sS = new StyledString(html.getContent());
        for (String property : this.properties) {
            sS.addProperty(property, css.search("h1", property, astCss));
        }
        sL.addStyledString(sS);
        return sL;
    }

    @Override
    public Object visit(H2 html, Object param) {
        StyledLine sL = new StyledLine();
        StyledString sS = new StyledString(html.getContent());
        for (String property : this.properties) {
            sS.addProperty(property, css.search("h2", property, astCss));
        }
        sL.addStyledString(sS);
        return sL;
    }

    @Override
    public Object visit(B html, Object param) {
        String upperTag = (String) param;
        StyledString sS = new StyledString(html.getValue());
        for (String property : this.properties) {
            sS.addProperty(property, css.search(upperTag, property, astCss));
        }
        sS.addProperty("font-style", "bold");
        return sS;
    }

    @Override
    public Object visit(I html, Object param) {
        String upperTag = (String) param;
        StyledString sS = new StyledString(html.getValue());
        for (String property : this.properties) {
            sS.addProperty(property, css.search(upperTag, property, astCss));
        }
        sS.addProperty("font-style", "italic");
        return sS;
    }

    @Override
    public Object visit(U html, Object param) {
        String upperTag = (String) param;
        StyledString sS = new StyledString(html.getValue());
        for (String property : this.properties) {
            sS.addProperty(property, css.search(upperTag, property, astCss));
        }
        sS.addProperty("font-style", "underlined");
        return sS;
    }

    @Override
    public Object visit(StringElement html, Object param) {
        String upperTag = (String) param;
        StyledString sS = new StyledString(html.getValue());
        for (String property : this.properties) {
            sS.addProperty(property, css.search(upperTag, property, astCss));
        }
        return sS;
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
