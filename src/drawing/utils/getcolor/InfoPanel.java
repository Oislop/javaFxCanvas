package drawing.utils.getcolor;

import javax.swing.*;
import java.awt.*;

public class InfoPanel extends ImagePanel {

    private JLabel title = new JLabel("单击选择颜色");
    private JButton copy = new JButton("复制");
    private JButton cancel = new JButton("取消");
    private JPanel colorPanel = null;
    private JLabel red = null;
    private JLabel green = null;
    private JLabel blue = null;
    private JLabel locations = null;
    private JLabel color16 = null;
    private JTextField colorField = null;

    public InfoPanel() {
        super();
        this.setBackground(new Color(74, 128, 248));
        init();
    }

    /*初始化一些组件*/
    public void init() {
        this.setLayout(null);
        copy.setBounds(40, 73, 60, 20);
        cancel.setBounds(110, 73, 60, 20);

        locations = new JLabel("(0,0)");
        locations.setBounds(57, 5, 160, 20);
        locations.setFont(new Font("楷体", Font.BOLD, 17));
        locations.setForeground(new Color(255, 255, 255));

        colorPanel = new JPanel();
        colorPanel.setBounds(50, 30, 15, 15);
        colorPanel.setBackground(Color.white);
        red = new JLabel();
        green = new JLabel();
        blue = new JLabel();

        color16 = new JLabel("Hex:");
        color16.setBounds(70, 28, 40, 20);
        color16.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        color16.setForeground(Color.white);

        colorField = new JTextField("FFFF");
        colorField.setBounds(110, 28, 60, 20);
        colorField.setForeground(Color.white);
        colorField.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        colorField.setBackground(null);
        colorField.setBorder(BorderFactory.createLineBorder(Color.BLACK, 0));


        title.setBounds(60, 50, 180, 20);
        title.setFont(new Font("Microsoft YaHei", Font.BOLD, 15));
        title.setForeground(Color.white);

        /*组件添加*/
        this.add(locations);
        this.add(colorPanel);
        this.add(color16);
        this.add(colorField);
        this.add(title);
        this.add(copy);
        this.add(cancel);

    }

    public JLabel getTitle() {
        return title;
    }

    public void setTitle(JLabel title) {
        this.title = title;
    }

    public JButton getCopy() {
        return copy;
    }


    public JButton getCancel() {
        return cancel;
    }


    public JPanel getColorPanel() {
        return colorPanel;
    }

    public void setColorPanel(JPanel colorPanel) {
        this.colorPanel = colorPanel;
    }

    public JLabel getRed() {
        return red;
    }

    public void setRed(JLabel red) {
        this.red = red;
    }

    public JLabel getGreen() {
        return green;
    }

    public void setGreen(JLabel green) {
        this.green = green;
    }

    public JLabel getBlue() {
        return blue;
    }

    public void setBlue(JLabel blue) {
        this.blue = blue;
    }


    public JLabel getLocations() {
        return locations;
    }

    public JLabel getColor16() {
        return color16;
    }

    public void setColor16(JLabel color16) {
        this.color16 = color16;
    }

    public JTextField getColorField() {
        return colorField;
    }

    public void setColorField(JTextField colorField) {
        this.colorField = colorField;
    }

}
