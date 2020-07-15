package drawing.utils.getcolor;


import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private static final long serialVersionUID = 1L;
    protected Image img;

    public ImagePanel() {

    }

    public ImagePanel(String imgUrl) {
        this.img = new ImageIcon(getClass().getResource(imgUrl)).getImage();
        Dimension size = new Dimension(this.img.getWidth(null), this.img.getHeight(null));
        setSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setOpaque(false);
        setLayout(null);
    }

    public ImagePanel(Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setSize(size);
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setOpaque(false);
        setLayout(null);
    }

    public void refreshImg(Image img) {
        this.img = img;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.img, 0, 0, null);
    }
}