package drawing.utils.getcolor;

import drawing.config.ShapeProperty;
import javafx.application.Platform;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.swing.JWindow;
// ScreenCapture


public class ScreenCapture extends JWindow implements MouseListener,
        MouseMotionListener {
    private static final long serialVersionUID = 1L;
    private BufferedImage desktopImg;
    private boolean captured = false;
    private boolean draging = false;
    private boolean toolPanelAtRight = true;
    private int x = 0;
    private int y = 0;
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 1;
    private int y2 = 1;
    private int point_x;
    private int point_y;
    private Color point_color;
    private ImagePanel toolPanel;
    private MouseInfo info = null;
    private Point point = null;

    private InfoPanel infoPanel;
    private ToolImagePanel pickImgPanel;
    private Label colorLabel;
    private ColorPicker colorPicker;


    public ScreenCapture(ColorPicker colorPicker,Label colorLabel,BufferedImage desktopImg) {
        this.desktopImg = desktopImg;
        this.colorLabel = colorLabel;
        this.colorPicker = colorPicker;
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        setVisible(true);
        setAlwaysOnTop(true);
        requestFocus();
    }


    void init() {
        setContentPane(new BackGroundPanel(this.desktopImg));
        setLayout(null);
        this.toolPanel = new ImagePanel();
        this.infoPanel = new InfoPanel();
        this.toolPanel.setLayout(null);
        this.pickImgPanel = new ToolImagePanel();
        pickImgPanel.setBounds(0, 0, 240, 160);
        infoPanel.setBounds(0, 160, 240, 100);

        this.toolPanel.add(this.pickImgPanel);/* 用于跟踪鼠标的移动的 */
        this.toolPanel.add(this.infoPanel);

        infoPanel.getCopy().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                setClipboard(infoPanel.getColorField().getText());
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //javaFX operations should go here
                        String color1 = infoPanel.getColorField().getText();
                        javafx.scene.paint.Color presentColor = javafx.scene.paint.Color.web(color1);
                        ShapeProperty.setColor(presentColor);
                        colorLabel.setStyle("-fx-background-color:" + color1 + ";");
                        colorPicker.setValue(presentColor);
                    }
                });
                exit();
            }

        });

        infoPanel.getCancel().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setVisible(false);
                exit();

            }

        });

        this.toolPanel.setSize(200, 260);
        getLayeredPane().add(this.toolPanel, 300);
        addMouseListener(this);
        addMouseMotionListener(this);

    }

    public void refreshBackGround(ColorPicker colorPicker,Label colorLabel,BufferedImage img) {
        this.colorLabel = colorLabel;
        this.colorPicker = colorPicker;
        this.desktopImg = img;
        setContentPane(new BackGroundPanel(this.desktopImg));
        setVisible(true);
        setAlwaysOnTop(true);

        requestFocus();
    }

    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(Color.BLACK);
        if (this.captured) {
            if (this.draging) {
                g.drawLine(this.point_x, 0, this.point_x, getHeight());
                g.drawLine(0, this.point_y, getWidth(), this.point_y);
            }
        } else {
            g.drawLine(this.point_x, 0, this.point_x, getHeight());
            g.drawLine(0, this.point_y, getWidth(), this.point_y);
        }
        repaintToolPanel();
    }

    public void repaintToolPanel() {
        if (this.toolPanelAtRight) {
            if ((this.point_x > getWidth() - 200 - 100) && (this.point_y < 400)) {
                this.toolPanel.setLocation(0, 0);
                this.toolPanelAtRight = false;
            }
        } else if ((this.point_x < 300) && (this.point_y < 400)) {
            this.toolPanel.setLocation(getWidth() - 200, 0);
            this.toolPanelAtRight = true;
        }

        this.point_color = new Color(this.desktopImg.getRGB(this.point_x,
                this.point_y));

        refreshInfoPanel();

    }

    public void refreshInfoPanel() {
        if (this.captured) {
            infoPanel.getLocations().setText("(" + this.point_x + "," + this.point_y + ")");
            infoPanel.getColorPanel().setBackground(point_color);
            String s16 = "#" + Integer.toHexString(point_color.getRed())
                    + Integer.toHexString(point_color.getGreen())
                    + Integer.toHexString(point_color.getBlue());// 得到颜色的十六进制表示。
            infoPanel.getColorField().setText(s16);
        } else {
            infoPanel.getLocations().setText(
                    "(" + this.point_x + "," + this.point_y + ")");
            infoPanel.getColorPanel().setBackground(point_color);
            String s16 = "#" + Integer.toHexString(point_color.getRed())
                    + Integer.toHexString(point_color.getGreen())
                    + Integer.toHexString(point_color.getBlue());// 得到颜色的十六进制表示。
            infoPanel.getColorField().setText(s16);
        }

        int pickImg_x;
        int pick_x1;
        int pick_x2;

        if (this.point_x - 20 < 0) {
            pick_x1 = 0;
            pick_x2 = this.point_x + 20;
            pickImg_x = 20 - this.point_x;
        } else {

            if (this.point_x + 20 > getWidth()) {
                pick_x1 = this.point_x - 20;
                pick_x2 = getWidth();
                pickImg_x = 0;
            } else {
                pick_x1 = this.point_x - 20;
                pick_x2 = this.point_x + 20;
                pickImg_x = 0;
            }
        }
        int pickImg_y;
        int pick_y1;
        int pick_y2;

        if (this.point_y - 20 < 0) {
            pick_y1 = 0;
            pick_y2 = this.point_y + 20;
            pickImg_y = 20 - this.point_y;
        } else {

            if (this.point_y + 20 > getHeight()) {
                pick_y1 = this.point_y - 20;
                pick_y2 = getHeight();
                pickImg_y = 0;
            } else {
                pick_y1 = this.point_y - 20;
                pick_y2 = this.point_y + 20;
                pickImg_y = 0;
            }
        }
        BufferedImage pickImg = new BufferedImage(40, 40, 1);
        Graphics pickGraphics = pickImg.getGraphics();
        pickGraphics.drawImage(this.desktopImg.getSubimage(pick_x1, pick_y1,
                pick_x2 - pick_x1, pick_y2 - pick_y1), pickImg_x, pickImg_y,
                Color.black, null);

        this.pickImgPanel.refreshImg(pickImg.getScaledInstance(200, 200, 16));
        this.toolPanel.validate();

    }


    public void exit() {
        this.x = 0;
        this.y = 0;
        this.x1 = 0;
        this.y1 = 0;
        this.x2 = 1;
        this.y2 = 1;
        this.point_x = 0;
        this.point_y = 0;
        this.captured = false;
        this.draging = false;
        this.toolPanel.setLocation(getWidth() - 200, 0);
        this.toolPanelAtRight = true;
        dispose();
        setVisible(false);
    }

    /* 复制到剪贴板中去 */
    public void setClipboard(String str) {
        StringSelection ss = new StringSelection(str);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == 1) {
            if (e.getClickCount() == 2) {
                setVisible(false);
                setClipboard(infoPanel.getColorField().getText());

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        //javaFX operations should go here
                        String color1 = infoPanel.getColorField().getText();
                        javafx.scene.paint.Color presentColor = javafx.scene.paint.Color.web(color1);
                        ShapeProperty.setColor(presentColor);
                        colorLabel.setStyle("-fx-background-color:" + color1 + ";");
                        colorPicker.setValue(presentColor);
                    }
                });
                exit();
            }
        } else if (e.getClickCount() == 2) {
            exit();
        }
    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

    public void mousePressed(MouseEvent e) {
        if ((e.getButton() == 1) && (!this.captured)) {
            this.point_x = e.getX();
            this.point_y = e.getY();
            this.x = this.point_x;
            this.y = this.point_y;
            this.draging = true;
            this.captured = true;
        }
    }

    public void mouseReleased(MouseEvent e) {
        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        if (e.getButton() == 1) {
            if (this.draging) {
                this.point_x = e.getX();
                this.point_y = e.getY();
                this.x2 = this.point_x;
                this.y2 = this.point_y;
                repaint();
                this.draging = false;
            }
        } else {
            this.draging = false;
            this.captured = false;
            this.point_x = e.getX();
            this.point_y = e.getY();
            repaint();
        }
    }

    public void mouseDragged(MouseEvent e) {
        // if (this.draging) {
        //     this.point_x = e.getX();
        //     this.point_y = e.getY();
        //     this.x2 = this.point_x;
        //     this.y2 = this.point_y;
        //     try {
        //         Robot robot = new Robot();
        //     } catch (AWTException e1) {
        //         // TODO Auto-generated catch block
        //         e1.printStackTrace();
        //     }
        //     point = info.getPointerInfo().getLocation();
        //     this.toolPanel.setLocation(point.x, point.y);
        //     repaint();
        // }
    }

    public void mouseMoved(MouseEvent e) {
        if (!this.captured) {
            this.point_x = e.getX();
            this.point_y = e.getY();
            try {
                Robot robot = new Robot();
            } catch (AWTException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            point = info.getPointerInfo().getLocation();
            this.toolPanel.setLocation(point.x, point.y);
            repaint();
        }
    }


}