package paint;

import css.ast.AstCss;
import html.ast.HTMLProgram;
import html.parser.Lexicon;
import html.parser.Parser;
import html.visitor.FindCSSVisitor;
import html.visitor.RenderVisitor;
import render.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class Browser {
    private static JFrame frame;
    List<StyledBlock> page;

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
    private JTextField url;
    int nextGridY = -1;
    GridBagConstraints c;

    private void createUIComponents() {
        htmlElementsPanel = new JPanel(new GridBagLayout());
        c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        createTopPanel();
    }

    private void createTopPanel() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        c.gridx = 0;
        c.gridy = 0;
        url = new JTextField();
        url.setText("res/EX5.HTML");
        url.setPreferredSize(new Dimension(200, 30));
        topPanel.add(url, c);
        c.gridx = 2;
        c.gridy = 0;
        JButton go = new JButton("Buscar");
        go.setPreferredSize(new Dimension(20, 50));
        go.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNodes();
            }
        });
        topPanel.add(go, c);
        nextGridY++;
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = nextGridY;
        htmlElementsPanel.add(topPanel, c);
    }

    private void createNodes() {
        if (htmlElementsPanel.getComponentCount() > 1) {
            for (int i = htmlElementsPanel.getComponentCount() - 1; i > 0; i--) {
                htmlElementsPanel.remove(i);
            }
        }
        try {
            page = parseHTML(url.getText());
        } catch (IOException e) {
            e.printStackTrace();
        }
        addNodes();
    }


    private void addNodes() {
        for (StyledBlock line : page) {
            nextGridY++;
            c.gridx = 0;
            c.gridwidth = 3;
            c.gridy = nextGridY;
            if (line instanceof StyledLine) {
                htmlElementsPanel.add(addStyledLine((StyledLine) line), c);
            } else if (line instanceof ImageBlock) {
                htmlElementsPanel.add(addImageBlock((ImageBlock) line), c);
            } else if (line instanceof UrlBlock) {
                htmlElementsPanel.add(addUrlBlock((UrlBlock) line), c);
            }
        }
    }

    /**
     * 
     * @param line
     * @return
     */
    private JTextPane addStyledLine(StyledLine line) {
        StyleContext sc = new StyleContext();
        StyledDocument doc = new DefaultStyledDocument(sc);
        try {
            for (StyledContent text : line.getStrings()) {
                MutableAttributeSet attrs = new SimpleAttributeSet();
                Color color = getColorByName(text.getProperty("color"));
                String pixelSize = text.getProperty("font-size");
                int fontSize;
                if (pixelSize == null) {
                    fontSize = 14;
                } else if (pixelSize.contains("px")) {
                    //https://docs.oracle.com/javase/7/docs/api/java/awt/Font.html#getSize()
                    fontSize = (int) (Integer.parseInt(pixelSize.replace("px", "")) * Toolkit.getDefaultToolkit().getScreenResolution() / 72.0);
                } else if (pixelSize.contains("pt")) {
                    fontSize = Integer.parseInt(pixelSize.replace("pt", ""));
                } else {
                    fontSize = 14;
                }
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
                        StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
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
                doc.insertString(doc.getLength(), " ", null); // Whitespace between tags
                if (text instanceof StyledLink) {
                    attrs.addAttribute("href", ((StyledLink) text).getAttribute("href"));
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
        tA.setPreferredSize(new Dimension(10, getContentHeight(tA.getStyledDocument())));
        tA.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                Element h = getHyperlinkElement(e);
                if (h != null) {
                    AttributeSet attribute = h.getAttributes();
                    if (attribute != null) {
                        String href = (String) attribute.getAttribute("href");
                        if (href != null) {
                            try {
                                Desktop.getDesktop().browse(new URI(href));
                            } catch (IOException | URISyntaxException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }

            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // the mouse has entered the label
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // the mouse has exited the label
            }
        });
        return tA;
        /*nextGridY++;
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = nextGridY;
        tA.setPreferredSize(new Dimension(10, getContentHeight(tA.getStyledDocument())));
        htmlElementsPanel.add(tA, c);*/
    }

    /**
     * Resilient method.
     * If url is not accessible, alt is shown.
     * If neither url neither alt is present, it will not add anything.
     *
     * @param line
     * @return
     */
    private JLabel addImageBlock(ImageBlock line) {
        int width = line.getAttributes().get("width") != null ? Integer.parseInt(line.getAttributes().get("width")) : 0;
        int height = line.getAttributes().get("height") != null ? Integer.parseInt(line.getAttributes().get("width")) : 0;
        URL src;
        JLabel wIcon = new JLabel(line.getAttributes().get("alt"));
        BufferedImage wPic = null;
        try {
            if (!line.getAttributes().get("src").contains("http")) {
                src = this.getClass().getResource("/" + line.getAttributes().get("src"));
            } else {
                src = new URL(line.getAttributes().get("src"));
            }
            wPic = ImageIO.read(src);
            if (width != 0 && height != 0) {
                wIcon = new JLabel(new ImageIcon(new ImageIcon(wPic).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT)));
            } else {
                wIcon = new JLabel(new ImageIcon(wPic));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return wIcon;
            /*nextGridY++;
            c.gridx = 0;
            c.gridwidth = 3;
            c.gridy = nextGridY;
            htmlElementsPanel.add(wIcon, c);*/
        }
    }

    /**
     * Add component for each block type
     * @param urlB
     * @return
     */
    private JComponent addUrlBlock(UrlBlock urlB) {
        JComponent urlComponent = new JTextPane();
        StyledBlock line = urlB.getInnerBlock();
        if (line instanceof StyledLine) {
            urlComponent = addStyledLine((StyledLine) line);
        } else if (line instanceof ImageBlock) {
            urlComponent = addImageBlock((ImageBlock) line);
        } else if (line instanceof UrlBlock) {
            urlComponent = addUrlBlock((UrlBlock) line);
        }
        urlComponent.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        urlComponent.setForeground(Color.BLUE.darker());
        urlComponent.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI(urlB.getAttributes().get("href")));
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // the mouse has entered the label
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // the mouse has exited the label
            }
        });
        return urlComponent;
    }

    /**
     * Parse html from url
     * @param htmlUrl
     * @return
     * @throws IOException
     */
    private List<StyledBlock> parseHTML(String htmlUrl) throws IOException {
        InputStreamReader filereader;
        // Preparing for online browsing experience
        if (htmlUrl.contains("http")) {
            filereader = new InputStreamReader(new URL(htmlUrl).openConnection().getInputStream());
        } else {
            filereader = new FileReader(htmlUrl);
        }
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
        Page page = (Page) render.visit(ast, astCss);
        System.out.println(page);
        frame.setTitle(page.getTitle());
        return page.getBlocks();
    }

    /**
     * Aux method to get color constant by name using reflection
     * @param name
     * @return
     */
    private static Color getColorByName(String name) {
        try {
            return (Color) Color.class.getField(name.toUpperCase()).get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a dummy component with frame width to get paragraph height
     * @param doc
     * @return
     */
    public static int getContentHeight(StyledDocument doc) {
        JTextPane dummyEditorPane = new JTextPane();
        dummyEditorPane.setSize(frame.getWidth(), Short.MAX_VALUE);
        dummyEditorPane.setDocument(doc);

        return dummyEditorPane.getPreferredSize().height;
    }

    /**
     * Change text area dimensions based on dummy component.
     * (Called from listener)
     * @param tA
     */
    public static void changeTextDimensions(JTextPane tA) {
        tA.setPreferredSize(new Dimension(10, getContentHeight(tA.getStyledDocument())));
    }

    /**
     * Aux method to get href attribute based on mouse event position (usually click event)
     * @param event
     * @return
     */
    private Element getHyperlinkElement(MouseEvent event) {
        JEditorPane editor = (JEditorPane) event.getSource();
        int pos = editor.getUI().viewToModel(editor, event.getPoint());
        if (pos >= 0 && editor.getDocument() != null) {
            DefaultStyledDocument hdoc = (DefaultStyledDocument) editor.getDocument();
            Element elem = hdoc.getCharacterElement(pos);
            if (elem.getAttributes().getAttribute("href") != null) {
                return elem;
            }
        }
        return null;
    }
}
