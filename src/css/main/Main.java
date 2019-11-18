package css.main;

import css.ast.AstCss;
import css.parser.*;
import css.visitor.BuscamParamCSSVisitor;

import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {
		FileReader filereader = new FileReader ("res/EX1.CSS");
		Lexicon lex = new Lexicon(filereader);
		listaTokens(lex);
		lex.resetIndex();
		Parser parser = new Parser (lex);
		AstCss ast = parser.parse();
		BuscamParamCSSVisitor buscar = new BuscamParamCSSVisitor();
		System.out.println(buscar.search("h1", "color",ast));
	}

	//Auxiliares
	//Lista de Tokens
	static void listaTokens (Lexicon lex) {
		Token t = lex.getToken();
		while (t.getToken() != TokensId.EOF) {
			System.out.println(t.toString());
			t = lex.getToken();
		}
		System.out.println ("\nFin de fichero. \n"+t.toString());	
	}
}
