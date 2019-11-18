package css.parser;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Lexicon {

    // Gestión de tokens
    List<Token> tokens = new ArrayList<Token>();
    int i = 0; // Último token entregado en getToken()
    // Gestión de lectura del fichero
    FileReader filereader;
    boolean charBuffUsed = false;
    char charBuff;
    int line = 1; // indica la línea del fichero fuente

    HashSet<Character> charText = new HashSet<Character>();

    public Lexicon(FileReader f) {
        filereader = f;
        String lex;
        try {
            char valor = (char) 0;
            while (valor != (char) -1) {
                valor = nextChar();
                switch (valor) {
                    case '{':
                        tokens.add(new Token(TokensId.OPENBRACKET, "{", line));
                        break;
                    case '}':
                        tokens.add(new Token(TokensId.CLOSEBRACKET, "}", line));
                        break;
                    case ';':
                        tokens.add(new Token(TokensId.SEMICOLON, ";", line));
                        break;
                    case ':':
                        tokens.add(new Token(TokensId.COLON, ":", line));
                        break;
                    case '/':
                        deleteComment();
                        break;
                    case '\n':
                        line++;
                        break;
                    case '\r':
                    case '\t':
                    case ' ':
                        break;
                    case (char) -1:
                        break;
                    default:
                        if (Character.isDigit(valor)) {
                            tokens.add(new Token(TokensId.SIZE, getSize("" + valor), line));
                        } else {
                            String text = getText("" + valor);
                            if (text.equals("color")) {
                                tokens.add(new Token(TokensId.COLOR, text, line));
                            } else if (text.equals("font-size")) {
                                tokens.add(new Token(TokensId.FONTSIZE, text, line));
                            } else if (text.equals("font-style")) {
                                tokens.add(new Token(TokensId.FONTSTYLE, text, line));
                            } else if (text.equals("text-align")) {
                                tokens.add(new Token(TokensId.TEXTALIGN, text, line));
                            } else if (text.equals("black")) {
                                tokens.add(new Token(TokensId.BLACK, text, line));
                            } else if (text.equals("blue")) {
                                tokens.add(new Token(TokensId.BLUE, text, line));
                            } else if (text.equals("red")) {
                                tokens.add(new Token(TokensId.RED, text, line));
                            } else if (text.equals("green")) {
                                tokens.add(new Token(TokensId.GREEN, text, line));
                            } else if (text.equals("center")) {
                                tokens.add(new Token(TokensId.CENTER, text, line));
                            } else if (text.equals("left")) {
                                tokens.add(new Token(TokensId.LEFT, text, line));
                            } else if (text.equals("right")) {
                                tokens.add(new Token(TokensId.RIGHT, text, line));
                            } else if (text.equals("italic")) {
                                tokens.add(new Token(TokensId.ITALIC, text, line));
                            } else if (text.equals("bold")) {
                                tokens.add(new Token(TokensId.BOLD, text, line));
                            } else if (text.equals("normal")) {
                                tokens.add(new Token(TokensId.NORMAL, text, line));
                            } else if (text.equals("underlined")) {
                                tokens.add(new Token(TokensId.UNDERLINED, text, line));
                            } else {
                                tokens.add(new Token(TokensId.IDENT, text, line));
                            }
                        }
                }
            }
            filereader.close();
        } catch (IOException e) {
            System.out.println("Error E/S: " + e);
        }

    }

    // ++
    // ++ Operaciones para el Sintactico
    // ++
    // Devolver el último token
    public void returnLastToken() {
        i--;
    }

    // Get Token
    public Token getToken() {
        if (i < tokens.size()) {
            return tokens.get(i++);
        }
        return new Token(TokensId.EOF, "EOF", line);
    }
    // ++
    // ++ Operaciones para el Sintactico
    // ++

    // Privadas
    String getSize(String lexStart) throws IOException {
        String lexReturned = lexStart;
        char valor;
        do {
            valor = nextChar();
            lexReturned = lexReturned + (valor);
        } while ((valor != 'p') && (valor != -1));
        // returnChar(valor);
        if (valor == 'p') {
            ;
            valor = nextChar();
            if (valor == 'x') {
                lexReturned = lexReturned + (valor);
            } else {
                errorLexico("Encontrado " + lexReturned + ". Se esperada un token SIZE.");
                return null;
            }
        }
        return lexReturned;
    }

    String getText(String lexStart) throws IOException {
        String lexReturned = lexStart;
        char valor = nextChar();
        while (Character.isDigit(valor) || Character.isAlphabetic(valor) || (valor == '-')) {
            lexReturned = lexReturned + (valor);
            valor = nextChar();
        }
        returnChar(valor);
        return lexReturned;
    }

    char nextChar() throws IOException {
        if (charBuffUsed) {
            charBuffUsed = false;
            return charBuff;
        } else {
            int valor = filereader.read();
            return ((char) valor);
        }
    }

    void returnChar(char r) {
        charBuffUsed = true;
        charBuff = r;
    }

    boolean deleteComment() throws IOException {
        if (nextChar() != '*') {
            errorLexico("Se esperaba un comentario.");
            return false;
        } else {
            char value = nextChar();
            while (value != '*') {
                value = nextChar();
            }
            return value == '/';
        }
    }

    void errorLexico(String e) {
        System.out.println("Error léxico en : " + e);
    }

    public void resetIndex() {
        i = 0;
    }
}
