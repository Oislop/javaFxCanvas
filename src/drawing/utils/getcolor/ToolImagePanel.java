package drawing.utils.getcolor;

import java.awt.*;

public class ToolImagePanel extends ImagePanel {
    private static final long serialVersionUID = 1L;
    int width;
    int height;
    int QUANTITY = 1;

    public ToolImagePanel() {

    }

    public void refreshImg(Image img) {
        this.img = img;
        this.width = getWidth();
        this.height = getHeight();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.drawLine(100, 0, 100, this.height);
        g.drawLine(0, this.height / 2, this.width, this.height / 2);

        for (int i = 1; i < this.QUANTITY / 2 ; i++) {
            g.drawLine(this.width / 2 - i, 0, this.width / 2 - i, this.height);
            g.drawLine(0, this.height / 2 - i, this.width, this.height / 2 - i);
        }
        for (int i = 1; i < this.QUANTITY / 2 ; i++) {
            g.drawLine(this.width / 2 + i, 0, this.width / 2 + i, this.height);
            g.drawLine(0, this.height / 2 + i, this.width, this.height / 2 + i);
        }
    }
}