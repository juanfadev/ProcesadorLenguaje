package paint;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImagePanel extends JPanel{

    private BufferedImage image;

    public ImagePanel(String route) {
        try {
            image = ImageIO.read(new File("res/" + route));
        } catch (IOException ex) {
            System.out.println("Error");
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 100, 100, this); // see javadoc for more info on the parameters
    }

}