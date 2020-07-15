package drawing.utils.getcolor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 显示图片的Label
 */
public class BackgroundImage extends JLabel {

    private int lineX;
    private int lineY;
    private int x;
    private int y;
    private int h;
    private int w;

    private boolean toolPanelAtRight = true;
    private ImagePanel toolPanel;
    private BufferedImage desktopImg;
    private ToolImagePanel pickImgPanel;

    public BackgroundImage() {
        toolPanel = new ImagePanel();
        pickImgPanel = new ToolImagePanel();
        pickImgPanel.setBounds(0, 0, 240, 160);
        this.toolPanel.add(this.pickImgPanel);/* 用于跟踪鼠标的移动的 */
        this.toolPanel.setSize(200, 260);
        //getLayeredPane().add(this.toolPanel, 300);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //画十字交叉线，交叉点为截屏起点
        g.drawLine(lineX, 0, lineX, getHeight());
        g.drawLine(0, lineY, getWidth(), lineY);

        //repaintToolPanel();

    }

    public void repaintToolPanel() {
        if (this.toolPanelAtRight) {
            if ((this.lineX > getWidth() - 200 - 100) && (this.lineY < 400)) {
                this.toolPanel.setLocation(0, 0);
                this.toolPanelAtRight = false;
            }
        } else if ((this.lineX < 300) && (this.lineY < 400)) {
            this.toolPanel.setLocation(getWidth() - 200, 0);
            this.toolPanelAtRight = true;
        }

        // this.point_color = new Color(this.desktopImg.getRGB(this.lineX,
        //         this.lineY));

        refreshInfoPanel();

    }

    private void refreshInfoPanel() {
        int pickImg_x;
        int pick_x1;
        int pick_x2;

        if (this.lineX - 40 < 0) {
            pick_x1 = 0;
            pick_x2 = this.lineX + 40;
            pickImg_x = 40 - this.lineX;
        } else {

            if (this.lineX + 40 > getWidth()) {
                pick_x1 = this.lineX - 40;
                pick_x2 = getWidth();
                pickImg_x = 0;
            } else {
                pick_x1 = this.lineX - 40;
                pick_x2 = this.lineX + 40;
                pickImg_x = 0;
            }
        }
        int pickImg_y;
        int pick_y1;
        int pick_y2;

        if (this.lineY - 40 < 0) {
            pick_y1 = 0;
            pick_y2 = this.lineY + 40;
            pickImg_y = 40 - this.lineY;
        } else {

            if (this.lineY + 40 > getHeight()) {
                pick_y1 = this.lineY - 40;
                pick_y2 = getHeight();
                pickImg_y = 0;
            } else {
                pick_y1 = this.lineY - 40;
                pick_y2 = this.lineY + 40;
                pickImg_y = 0;
            }
        }
        BufferedImage pickImg = new BufferedImage(80, 80, 1);
        Graphics pickGraphics = pickImg.getGraphics();
        pickGraphics.drawImage(this.desktopImg.getSubimage(pick_x1, pick_y1,
                pick_x2 - pick_x1, pick_y2 - pick_y1), pickImg_x, pickImg_y,
                Color.black, null);
        this.pickImgPanel.refreshImg(pickImg.getScaledInstance(200, 200, 16));
    }

    public void drawCross(int x, int y) {
        this.lineX = x;
        this.lineY = y;
        repaint();
    }

    public void reset() {
        this.x = -1;
        this.y = -1;
        this.w = -1;
        this.h = -1;
    }
}
