package html.main;

import css.ast.AstCss;
import html.ast.AstHTML;
import html.ast.HTMLProgram;
import html.parser.Lexicon;
import html.parser.Parser;
import html.parser.Token;
import html.parser.TokensId;
import html.visitor.FindCSSVisitor;
import html.visitor.RenderVisitor;
import render.StyledLine;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        FileReader filereader = new FileReader("res/EX4.HTML");
        Lexicon lex = new Lexicon(filereader);
        listaTokens(lex);
        lex.resetIndex();
        Parser parser = new Parser(lex);
        HTMLProgram ast = parser.parse();
        System.out.println("Ruta al CSS:");
        System.out.println(Main.cssRoute(ast));

        FileReader fileReaderCSS = new FileReader("res/" + Main.cssRoute(ast));
        css.parser.Lexicon cssLex = new css.parser.Lexicon(fileReaderCSS);
        css.parser.Parser cssParser = new css.parser.Parser(cssLex);
        AstCss astCss = cssParser.parse();
        RenderVisitor render = new RenderVisitor();
        List<StyledLine> page = (List<StyledLine>) render.visit(ast, astCss);
        System.out.println(page);
    }

    //Auxiliares
    //Lista de Tokens
    static void listaTokens(Lexicon lex) {
        Token t = lex.getToken();
        while (t.getToken() != TokensId.EOF) {
            System.out.println(t.toString());
            t = lex.getToken();
        }
        System.out.println("\nFin de fichero. \n" + t.toString());
    }

    static String cssRoute(AstHTML ast) {
        FindCSSVisitor buscar = new FindCSSVisitor();
        return (String) ast.accept(buscar, null);
    }
}
