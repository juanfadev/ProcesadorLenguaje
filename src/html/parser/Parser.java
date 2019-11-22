package html.parser;


import html.ast.Body;
import html.ast.HTMLProgram;
import html.ast.Head;
import html.ast.headElements.HeadElement;
import html.ast.headElements.Link;
import html.ast.headElements.Title;
import html.ast.htmlElements.BodyElement;
import html.ast.htmlElements.H1;
import html.ast.htmlElements.H2;
import html.ast.htmlElements.InnerPElements.*;
import html.ast.htmlElements.P;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Parser {

    private static final List<TokensId> HEAD_ELEMENTS_OPENTAG = new ArrayList<TokensId>() {
        {
            add(TokensId.TITLEOPEN);
            add(TokensId.LINKOPEN);
        }
    };
    private static final List<TokensId> BODY_ELEMENTS_OPENTAG = new ArrayList<TokensId>() {
        {
            add(TokensId.H1OPEN);
            add(TokensId.H2OPEN);
            add(TokensId.POPEN);
        }
    };

    private static final List<TokensId> PARAGRAPH_ELEMENTS_OPENTAG = new ArrayList<TokensId>() {
        {
            add(TokensId.BOPEN);
            add(TokensId.IOPEN);
            add(TokensId.UOPEN);
            add(TokensId.CONTENT);
        }
    };


    private Lexicon lex;
    private boolean errorSint = false;

    public Parser(Lexicon lex) {
        this.lex = lex;
    }

    public HTMLProgram parse() {
        HTMLProgram prog = null;
        Head head;
        Body body;
        Token token = lex.getToken();
        if (token.getToken() == TokensId.HTML) {
            token = lex.getToken();
            if (token.getToken() == TokensId.HEADOPEN) {
                lex.returnLastToken();
                head = parseHead();
                token = lex.getToken();
                if (token.getToken() == TokensId.BODYOPEN) {
                    lex.returnLastToken();
                    body = parseBody();
                    token = lex.getToken();
                    if (token.getToken() == TokensId.HTMLCLOSE) {
                        prog = new HTMLProgram(0, 0, head, body);
                    } else {
                        errorSintactico("No se encuentra la etiqueta html de cierre", token.getLine());
                    }
                } else {
                    errorSintactico("No se encuentra el body open", token.getLine());
                }
            } else {
                errorSintactico("No se encuentra el head", token.getLine());
            }
        } else {
            errorSintactico("No se encuentra la etiqueta html", token.getLine());
        }
        return prog;
    }

    private Head parseHead() {
        Head head = null;
        Title title = null;
        Link link;
        Token token = lex.getToken();
        if (token.getToken() == TokensId.HEADOPEN) {
            token = lex.getToken();
            if (HEAD_ELEMENTS_OPENTAG.contains(token.getToken())) {
                lex.returnLastToken();
                title = (Title) parseHeadElement();
                link = (Link) parseHeadElement();
                token = lex.getToken();
                if (token.getToken() == TokensId.HEADCLOSE) {
                    head = new Head(token.getLine(), token.getCol(), title, link);
                } else {
                    errorSintactico("Error al cerrar el head", token.getLine());
                }
            } else {
                errorSintactico("Error, uno de los elementos no puede ir dentro de head", token.getLine());
            }

        } else {
            errorSintactico("Head mal construido", token.getLine());
        }
        return head;
    }

    private HeadElement parseHeadElement() {
        HeadElement he = null;
        Token token = lex.getToken();
        switch (token.getToken()) {
            case TITLEOPEN:
                lex.returnLastToken();
                he = parseTitleElement();
                break;
            case LINKOPEN:
                lex.returnLastToken();
                he = parseLinkElement();
                break;
            default:
                errorSintactico("Error, etiqueta '" + token.getLexeme() + "' no reconocida en el head", token.getLine());
        }
        return he;
    }

    private Link parseLinkElement() {
        Link l = null;
        Map<String, String> attributes = new HashMap<>();
        Token token = lex.getToken();
        if (token.getToken() == TokensId.LINKOPEN) {
            token = lex.getToken();
            while (token.getToken() == TokensId.PROPERTY) {
                String property = token.getLexeme();
                token = lex.getToken();
                if (token.getToken() == TokensId.EQUAL) {
                    token = lex.getToken();
                    if (token.getToken() == TokensId.VALUE) {
                        String value = token.getLexeme();
                        attributes.put(property, value);
                        token = lex.getToken();
                    } else {
                        errorSintactico("Error en propiedad", token.getLine());
                    }
                } else {
                    errorSintactico("Error en propiedad", token.getLine());
                }
            }
            if (token.getToken() == TokensId.TAGCLOSE) {
                l = new Link(token.getLine(), token.getCol(), attributes);
            } else {
                errorSintactico("No se ha cerrado correctamente LINK", token.getLine());
            }
        }
        return l;
    }

    private Title parseTitleElement() {
        StringBuilder text = new StringBuilder();
        Title t = null;
        Token token = lex.getToken();
        if (token.getToken() == TokensId.TITLEOPEN) {
            token = lex.getToken();
            while (token.getToken() == TokensId.CONTENT) {
                text.append(token.getLexeme());
                token = lex.getToken();
            }
            if (token.getToken() != TokensId.TITLECLOSE) {
                errorSintactico("Error, etiqueta title no cerrada", token.getLine());
            } else {
                t = new Title(token.getLine(), token.getCol(), text.toString());
            }
        }
        return t;
    }

    private Body parseBody() {
        Body body = null;
        List<BodyElement> elements = new ArrayList<>();
        Token token = lex.getToken();
        if (token.getToken() == TokensId.BODYOPEN) {
            token = lex.getToken();
            while (BODY_ELEMENTS_OPENTAG.contains(token.getToken())) {
                lex.returnLastToken();
                BodyElement bodyEl = parseBodyElement();
                elements.add(bodyEl);
                token = lex.getToken();
            }
            if (token.getToken() == TokensId.BODYCLOSE) {
                body = new Body(token.getLine(), token.getCol(), elements);
            } else {
                errorSintactico("Body mal cerrado", token.getLine());
            }
        } else {
            errorSintactico("Body mal abierto", token.getLine());
        }

        return body;
    }

    private BodyElement parseBodyElement() {
        BodyElement bodyEl = null;
        Token token = lex.getToken();
        switch (token.getToken()) {
            case H1OPEN:
                lex.returnLastToken();
                bodyEl = parseH1Element();
                break;
            case H2OPEN:
                lex.returnLastToken();
                bodyEl = parseH2Element();
                break;
            case POPEN:
                lex.returnLastToken();
                bodyEl = parsePElement();
                break;
            default:
                errorSintactico("Error, etiqueta '" + token.getLexeme() + "' no reconocida en el head", token.getLine());
        }
        return bodyEl;
    }


    private H2 parseH2Element() {
        H2 h2 = null;
        Token token = lex.getToken();
        if (token.getToken() == TokensId.H2OPEN) {
            token = lex.getToken();
            if (token.getToken() == TokensId.CONTENT) {
                String content = token.getLexeme();
                token = lex.getToken();
                if (token.getToken() == TokensId.H2CLOSE) {
                    h2 = new H2(token.getLine(), token.getCol(), content);
                } else {
                    errorSintactico("Error al cerrar la etiqueta h2", token.getLine());
                }
            } else {
                errorSintactico("Error en el contenido de la etiqueta h2", token.getLine());
            }
        } else {
            errorSintactico("Error al abrir la etiqueta h2", token.getLine());
        }
        return h2;
    }

    private H1 parseH1Element() {
        H1 h1 = null;
        Token token = lex.getToken();
        if (token.getToken() == TokensId.H1OPEN) {
            token = lex.getToken();
            if (token.getToken() == TokensId.CONTENT) {
                String content = token.getLexeme();
                token = lex.getToken();
                if (token.getToken() == TokensId.H1CLOSE) {
                    h1 = new H1(token.getLine(), token.getCol(), content);
                } else {
                    errorSintactico("Error al cerrar la etiqueta h1", token.getLine());
                }
            } else {
                errorSintactico("Error en el contenido de la etiqueta h1", token.getLine());
            }
        } else {
            errorSintactico("Error al abrir la etiqueta h1", token.getLine());
        }
        return h1;
    }

    private P parsePElement() {
        P p = null;
        List<InnerPElement> innerElements = new ArrayList<>();
        Token token = lex.getToken();
        if (token.getToken() == TokensId.POPEN) {
            token = lex.getToken();
            while (PARAGRAPH_ELEMENTS_OPENTAG.contains(token.getToken())) {
                lex.returnLastToken();
                InnerPElement innerEl = parseInnerP();
                innerElements.add(innerEl);
                token = lex.getToken();
            }
            if (token.getToken() == TokensId.PCLOSE) {
                p = new P(token.getLine(), token.getCol(), innerElements);
            } else {
                errorSintactico("P mal cerrado", token.getLine());
            }
        } else {
            errorSintactico("P mal abierto", token.getLine());
        }
        return p;
    }

    private InnerPElement parseInnerP() {
        InnerPElement iPE = null;
        Token token = lex.getToken();
        switch (token.getToken()) {
            case BOPEN:
                lex.returnLastToken();
                iPE = parseBElement();
                break;
            case IOPEN:
                lex.returnLastToken();
                iPE = parseIElement();
                break;
            case UOPEN:
                lex.returnLastToken();
                iPE = parseUElement();
                break;
            case CONTENT:
                lex.returnLastToken();
                iPE = parseStringElement();
                break;
            default:
                errorSintactico("Error, etiqueta '" + token.getLexeme() + "' no reconocida en el p", token.getLine());
        }
        return iPE;
    }

    private StringElement parseStringElement() {
        StringElement sE = null;
        Token token = lex.getToken();
        if (token.getToken() == TokensId.CONTENT){
            sE = new StringElement(token.getLine(), token.getCol(), token.getLexeme());
        }else{
            errorSintactico("Error en string" , token.getLine());
        }
        return sE;
    }

    private B parseBElement() {
        B b = null;
        Token token = lex.getToken();
        if (token.getToken() == TokensId.BOPEN){
            StringElement sE = parseStringElement();
            token = lex.getToken();
            if (token.getToken() == TokensId.BCLOSE){
                b = new B(token.getLine(), token.getCol(), sE.getValue());
            }else{
                errorSintactico("Error al cerrar etiqueta B" , token.getLine());
            }

        }else{
            errorSintactico("Error al abrir etiqueta B" , token.getLine());
        }
        return b;
    }
    private I parseIElement() {
        I b = null;
        Token token = lex.getToken();
        if (token.getToken() == TokensId.IOPEN){
            StringElement sE = parseStringElement();
            token = lex.getToken();
            if (token.getToken() == TokensId.ICLOSE){
                b = new I(token.getLine(), token.getCol(), sE.getValue());
            }else{
                errorSintactico("Error al cerrar etiqueta I" , token.getLine());
            }

        }else{
            errorSintactico("Error al abrir etiqueta I" , token.getLine());
        }
        return b;
    }
    private U parseUElement() {
        U b = null;
        Token token = lex.getToken();
        if (token.getToken() == TokensId.UOPEN){
            StringElement sE = parseStringElement();
            token = lex.getToken();
            if (token.getToken() == TokensId.UCLOSE){
                b = new U(token.getLine(), token.getCol(), sE.getValue());
            }else{
                errorSintactico("Error al cerrar etiqueta U" , token.getLine());
            }

        }else{
            errorSintactico("Error al abrir etiqueta U" , token.getLine());
        }
        return b;
    }


    //Gesti�n de Errores Sint�ctico
    private void errorSintactico(String e, int line) {
        errorSint = true;
        System.out.println("Error Sint�ctico : " + e + " en la l�nea " + line);
    }
}
