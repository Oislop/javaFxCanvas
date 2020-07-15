package drawing.utils.getcolor;

import java.awt.*;

public class BackGroundPanel extends ImagePanel {
    private static final long serialVersionUID = 1L;
    int width;
    int height;

    public BackGroundPanel() {
    }

    public BackGroundPanel(String imgUrl) {
        super(imgUrl);
    }

    public BackGroundPanel(Image img) {
        super(img);
    }

    public void refreshImg(Image img) {
        this.img = img;
        this.width = getWidth();
        this.height = getHeight();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setComposite(AlphaComposite.getInstance(3, 0.3F));
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, this.img.getWidth(null), this.img.getHeight(null));
    }
}