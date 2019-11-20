package html.parser;


public class Token {

    TokensId token;
    String lexeme;
    int line;
    int col;

    public Token(TokensId token, String lexeme, int line) {
        this.token = token;
        this.lexeme = lexeme;
        this.line = line;
        this.col = -1;
    }

    public Token(TokensId token, String lexeme, int line, int col) {
        this.token = token;
        this.lexeme = lexeme;
        this.line = line;
        this.col = col;
    }

    public TokensId getToken() {
        return token;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    public String toString() {
        return "TOKEN: " + token + " - LEXEMA: " + lexeme + " - LINE: " + line;
    }
}
