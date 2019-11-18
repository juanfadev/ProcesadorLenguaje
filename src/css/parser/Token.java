package css.parser;


public class Token {
	TokensId token;
	String lexeme;
	int line;
	
	public Token (TokensId token, String lexeme, int line) {
		this.token = token;
		this.lexeme = lexeme;
		this.line = line;
	}
	
	public TokensId getToken () {
		return token;
	}
	
	public String getLexeme () {
		return lexeme;
	}
	
	public int getLine() {
		return line;
	}

	public String toString() {
		return "TOKEN: "+token+" - LEXEMA: "+lexeme+" - LINE: "+line;
	}
}
