package paint;

import css.ast.AstCss;
import html.ast.HTMLProgram;
import html.parser.Lexicon;
import html.parser.Parser;
import html.visitor.FindCSSVisitor;
import html.visitor.RenderVisitor;
import render.StyledLine;
import render.StyledString;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class Browser {
    private static JFrame frame;
    List<StyledLine> page;

    public static void main(String[] args) {
        frame = new JFrame("Web Browser");
        Browser browser = new Browser();
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent evt) {
                for (Component c : browser.htmlElementsPanel.getComponents()) {
                    System.out.println("Components resized");
                    if (c instanceof JTextPane) changeTextDimensions((JTextPane) c);
                }
            }
        });
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setContentPane(browser.scroll);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private JPanel htmlElementsPanel;
    private JScrollPane scroll;
    int nextGridY = -1;
    GridBagConstraints c;

    private void createUIComponents() {
        htmlElementsPanel = new JPanel(new GridBagLayout());
        try {
            page = parseHTML();
        } catch (IOException e) {
            e.printStackTrace();
        }
        c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        addNodes();
    }

    private void addNodes() {
        for (StyledLine line : page) {
            StyleContext sc = new StyleContext();
            StyledDocument doc = new DefaultStyledDocument(sc);
            try {
                for (StyledString text : line.getStrings()) {
                    MutableAttributeSet attrs = new SimpleAttributeSet();
                    Color color = getColorByName(text.getProperty("color"));
                    String pixelSize = text.getProperty("font-size");
                    //https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html#getSize()
                    int fontSize = (int) (Integer.parseInt(pixelSize == null ? "12" : pixelSize.replace("px", "")) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0);
                    StyleConstants.setFontSize(attrs, fontSize);
                    StyleConstants.setForeground(attrs, color == null ? Color.BLACK : color);
                    switch (text.getProperty("text-align")) {
                        case ("center"):
                            StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
                            break;
                        case ("left"):
                            StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
                            break;
                        case ("right"):
                            StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
                            break;
                        case ("justify"):
                            StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_JUSTIFIED);
                            break;
                        default:
                            StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
                            System.out.println("No alignment for text");
                    }

                    switch (text.getProperty("font-style")) {
                        case ("bold"):
                            StyleConstants.setBold(attrs, true);
                            break;
                        case ("underlined"):
                            StyleConstants.setUnderline(attrs, true);
                            break;
                        case ("italic"):
                            StyleConstants.setItalic(attrs, true);
                            break;
                        default:
                            System.out.println("No style for text");
                    }
                    doc.insertString(doc.getLength(), text.getString(), attrs);
                }
                MutableAttributeSet attrs = new SimpleAttributeSet();
                switch (line.getStrings().get(0).getProperty("text-align")) {
                    case ("center"):
                        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
                        break;
                    case ("left"):
                        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
                        break;
                    case ("right"):
                        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
                        break;
                    case ("justify"):
                        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_JUSTIFIED);
                        break;
                    default:
                        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_CENTER);
                        System.out.println("No alignment for text");
                }
                doc.setParagraphAttributes(0, doc.getLength(), attrs, false);
            } catch (BadLocationException ex) {
                System.out.println("Error, bad location");
            }
            JTextPane tA = new JTextPane(doc);
            tA.setEditable(false);
            nextGridY++;
            c.gridx = 0;
            c.gridwidth = 3;
            c.gridy = nextGridY;
            tA.setPreferredSize(new Dimension(10, getContentHeight(tA.getStyledDocument())));
            htmlElementsPanel.add(tA, c);
        }
    }

    private List<StyledLine> parseHTML() throws IOException {
        FileReader filereader = new FileReader("res/EX4.HTML");
        Lexicon lex = new Lexicon(filereader);
        Parser parser = new Parser(lex);
        HTMLProgram ast = parser.parse();
        FindCSSVisitor buscar = new FindCSSVisitor();
        String cssRoute = (String) ast.accept(buscar, null);

        FileReader fileReaderCSS = new FileReader("res/" + cssRoute);
        css.parser.Lexicon cssLex = new css.parser.Lexicon(fileReaderCSS);
        css.parser.Parser cssParser = new css.parser.Parser(cssLex);
        AstCss astCss = cssParser.parse();

        RenderVisitor render = new RenderVisitor();
        List<StyledLine> page = (List<StyledLine>) render.visit(ast, astCss);
        System.out.println(page);
        return page;
    }

    private static Color getColorByName(String name) {
        try {
            return (Color) Color.class.getField(name.toUpperCase()).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int getContentHeight(StyledDocument doc) {
        JTextPane dummyEditorPane = new JTextPane();
        dummyEditorPane.setSize(frame.getWidth(), Short.MAX_VALUE);
        dummyEditorPane.setDocument(doc);

        return dummyEditorPane.getPreferredSize().height;
    }

    public static void changeTextDimensions(JTextPane tA) {
        tA.setPreferredSize(new Dimension(10, getContentHeight(tA.getStyledDocument())));
    }

}
