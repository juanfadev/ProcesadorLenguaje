package css.parser;

import css.ast.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parser {


    Lexicon lex;
    boolean errorSint = false;
    static final TokensId[] VARCONFTYPES = {TokensId.TEXTALIGN, TokensId.FONTSIZE, TokensId.FONTSTYLE, TokensId.COLOR};

    public Parser(Lexicon lex) {
        this.lex = lex;
    }

    public AstCss parse() {
        AstCss ast = parseProgram();
        return ast;
    }

    Program parseProgram() {
        Program prog = null;
        List<Rule> rules = new ArrayList<>();
        Token token = lex.getToken();
        while (token.getToken() == TokensId.IDENT) {
            lex.returnLastToken();
            Rule rule = parseRule();
            if ((rule != null) && (!errorSint)) {
                rules.add(rule);
            }
            token = lex.getToken();
        }
        if (token.getToken() != TokensId.EOF) {
            errorSintactico("Econtrado " + token.getLexeme() + "Error", lex.line);
        }
        if (!errorSint) {
            prog = new Program(this.lex.line, 0, rules);
        }
        return prog;
    }

    Rule parseRule() {
        Rule rule = null;
        String ident = null;
        List<Definition> defs = new ArrayList<>();
        Token token = lex.getToken();
        ident = token.getLexeme();
        token = lex.getToken();
        if (token.getToken() != TokensId.OPENBRACKET) {
            errorSintactico("Encontrado " + token.getLexeme() + " en vez de '{'. Error", lex.line);
        } else {
            token = lex.getToken();
            while (Arrays.asList(VARCONFTYPES).contains(token.getToken())) {
                lex.returnLastToken();
                Definition def = parseDefinition();
                defs.add(def);
                token = lex.getToken();
            }
            if (token.getToken() != TokensId.CLOSEBRACKET) {
                errorSintactico("Encontrado " + token.getLexeme() + "Error", lex.line);
            }
            if (!errorSint) {
                rule = new Rule(this.lex.line, 0, ident, defs);
            }
        }
        return rule;
    }

    Definition parseDefinition() {
        Definition def = null;
        Token token = lex.getToken();
        Token name = token;
        token = lex.getToken();
        if (token.getToken() != TokensId.COLON) {
            errorSintactico("Encontrado " + token.getLexeme() + "Error", lex.line);
        }
        if (!errorSint) {
            switch (name.getToken()) {
                case TEXTALIGN:
                    def = new Definition(this.lex.line, 0, name.getLexeme(), parseAlignments());
                    break;
                case FONTSTYLE:
                    def = new Definition(this.lex.line, 0, name.getLexeme(), parseStyles());
                    break;
                case COLOR:
                    def = new Definition(this.lex.line, 0, name.getLexeme(), parseColors());
                    break;
                case FONTSIZE:
                    def = new Definition(this.lex.line, 0, name.getLexeme(), parseSizes());
                    break;
                default:
            }
            token = lex.getToken();
            if (token.getToken() != TokensId.SEMICOLON) {
                errorSintactico("Encontrado " + token.getLexeme() + "Error", lex.line);
            }
        }
        return def;
    }

    String parseAlignments() {
        Token token = lex.getToken();
        switch (token.getToken()) {
            case LEFT:
                return ("left");
            case RIGHT:
                return ("right");
            case CENTER:
                return ("center");
            default:
                errorSintactico("Encontrado " + token.getLexeme() + ". Se esperaba un alineamiento de texto: left, right o center", token.getLine());
        }
        return null;
    }

    String parseColors() {
        Token token = lex.getToken();
        switch (token.getToken()) {
            case BLUE:
                return ("blue");
            case BLACK:
                return ("black");
            case GREEN:
                return ("green");
            case RED:
                return ("red");
            default:
                errorSintactico("Encontrado " + token.getLexeme() + ". Se esperaba un color de texto: azul, negro verde o rojo", token.getLine());
        }
        return null;
    }

    public String parseStyles() {
        Token token = lex.getToken();
        switch (token.getToken()) {
            case NORMAL:
                return ("normal");
            case ITALIC:
                return ("italic");
            case BOLD:
                return ("bold");
            default:
                errorSintactico("Encontrado " + token.getLexeme() + ". Se esperaba un estilo de texto: normal, italic o bold", token.getLine());
        }
        return null;
    }

    public String parseSizes() {
        Token token = lex.getToken();
        if (token.getToken() == TokensId.SIZE) {
            return token.getLexeme();
        } else {
            errorSintactico("Encontrado " + token.getLexeme() + ". Se esperaba un estilo de texto: normal, italic o bold", token.getLine());
        }
        return null;
    }


    //Gesti�n de Errores Sint�ctico
    void errorSintactico(String e, int line) {
        errorSint = true;
        System.out.println("Error Sint�ctico : " + e + " en la l�nea " + line);
    }
}
