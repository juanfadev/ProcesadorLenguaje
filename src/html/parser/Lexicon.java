package html.parser;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Lexicon {

    public static final Map<String, TokensId> stringTokenMap = new HashMap<String, TokensId>() {
        {
            put("<html>", TokensId.HTML);
            put("</html>", TokensId.HTMLCLOSE);
            put("<head>", TokensId.HEADOPEN);
            put("</head>", TokensId.HEADCLOSE);
            put("<title>", TokensId.TITLEOPEN);
            put("</title>", TokensId.TITLECLOSE);
            put("<body>", TokensId.BODYOPEN);
            put("</body>", TokensId.BODYCLOSE);
            put("<h1>", TokensId.H1OPEN);
            put("</h1>", TokensId.H1CLOSE);
            put("<h2>", TokensId.H2OPEN);
            put("</h2>", TokensId.H2CLOSE);
            put("<h3>", TokensId.H3OPEN);
            put("</h3>", TokensId.H3CLOSE);
            put("<p>", TokensId.POPEN);
            put("</p>", TokensId.PCLOSE);
            put("<i>", TokensId.IOPEN);
            put("</i>", TokensId.ICLOSE);
            put("<b>", TokensId.BOPEN);
            put("</b>", TokensId.BCLOSE);
            put("<u>", TokensId.UOPEN);
            put("</u>", TokensId.UCLOSE);
        }
    };

    // Gestión de tokens
    List<Token> tokens = new ArrayList<Token>();
    int i = 0; // Último token entregado en getToken()
    // Gestión de lectura del fichero
    FileReader filereader;
    boolean charBuffUsed = false;
    char charBuff;
    int line = 1; // indica la línea del fichero fuente
    int col = 1;

    HashSet<Character> charText = new HashSet<Character>();

    public Lexicon(FileReader f) {
        filereader = f;
        String lex;
        try {
            char valor = (char) 0;
            while (valor != (char) -1) {
                valor = nextChar();
                switch (valor) {
                    case '<':
                        findTag();
                        break;
                    /*case '/':
                        deleteComment();
                        break;*/
                    case '\n':
                        line++;
                        col = 1;
                        break;
                    case '\r':
                    case '\t':
                    case ' ':
                        break;
                    case (char) -1:
                        break;
                    default:
                        String text = getText("" + valor);
                        tokens.add(new Token(TokensId.CONTENT, text, line));
                }
            }
            filereader.close();
            tokens.add(new Token(TokensId.EOF, "EOF", line));
        } catch (IOException e) {
            System.out.println("Error E/S: " + e);
        }

    }

    private void findTag() throws IOException {
        boolean openTag = false;
        String lexeme = "<";
        char value = nextChar();
        while (value != '>') {
            if (value == ' ') {
                readOpenTag(lexeme);
                openTag = true;
            }
            lexeme += value;
            value = nextChar();
        }
        if (!openTag) {
            lexeme += value;
            TokensId tokenId = stringTokenMap.get(lexeme);
            if (tokenId != null) {
                tokens.add(new Token(tokenId, lexeme, line));
            } else {
                errorLexico("Error, no existe la etiqueta " + lexeme);
            }
        } else {
            tokens.add(new Token(TokensId.TAGCLOSE, ">", line));
        }
    }

    private void readOpenTag(String tag) throws IOException {
        switch (tag) {
            case ("<link"):
                tokens.add(new Token(TokensId.LINKOPEN, tag, line));
                break;
            default:
                errorLexico("No existe la etiqueta abierta " + tag);
        }
        readProperties();
    }

    private void readProperties() throws IOException {
        String lexeme = "";
        char value = ' ';
        while (value != '>') {
            value = nextChar();
            while (value != '=') {
                if (value == ' ' || value == '"') {
                    errorLexico("Espacio dentro de una string de propiedad");
                }
                lexeme += value;
                value = nextChar();
            }
            tokens.add(new Token(TokensId.PROPERTY, lexeme, line));
            lexeme = "";
            tokens.add(new Token(TokensId.EQUAL, "=", line));
            value = nextChar();
            if (value == '"') {
                value = nextChar();
            }
            while (value != '"') {
                if (value == ' ') {
                    errorLexico("Espacio dentro de una string de propiedad");
                }
                lexeme += value;
                value = nextChar();
            }
            value = nextChar();
            tokens.add(new Token(TokensId.VALUE, lexeme, line));
            lexeme = "";
        }
        returnChar('>');
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
        char[] chars = new char[]{'-', ' '};
        String lexReturned = lexStart;
        char valor = nextChar();
        //while (Character.isDigit(valor) || Character.isAlphabetic(valor) || (Arrays.asList(chars).contains(valor))) {
        while (valor != '<') {
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
            col++;
            int valor = filereader.read();
            return ((char) valor);
        }
    }

    void returnChar(char r) {
        charBuffUsed = true;
        charBuff = r;
        col --;
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
        System.out.println("Error léxico en línea " + line + ", Columna " + col + " :" + e);
    }

    public void resetIndex() {
        i = 0;
    }
}
