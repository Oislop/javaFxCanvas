package drawing.model;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.Serializable;
import java.util.Date;


public class MyText extends Text implements Serializable, MyShape {
    /**
     *
     */
    private static final long serialVersionUID = -7436172851065445687L;
    private double[] pos;
    private Date createDate;
    protected double lineWidth, size;
    private boolean dashLine = false;

    @Override
    public double getLineWidth() {
        return lineWidth;
    }

    @Override
    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public String getLineColor() {
        return lineColor;
    }

    @Override
    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    @Override
    public String getFillColor() {
        return fillColor;
    }

    @Override
    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    protected String lineColor, fillColor, fontFamily, text;


    public MyText(double x, double y, String text, Font font, double lineWidth, Paint lineColor, Paint fillColor) {
        super(x, y, text);
        this.setStrokeWidth(lineWidth);
        this.setStroke(lineColor);
        this.setFill(fillColor);
        this.setFont(font);
        this.pos = new double[]{x, y};
        this.createDate = new Date();
        this.lineWidth = lineWidth;
        this.lineColor = (lineColor == null ? "" : ((Color) lineColor).toString());
        this.fillColor = (fillColor == null ? "" : ((Color) fillColor).toString());
        this.fontFamily = font.getFamily();
        this.size = font.getSize();
        this.text = text;
    }

    @Override
    public boolean isDashLine() {
        return dashLine;
    }

    @Override
    public double getPosX() {
        // TODO Auto-generated method stub
        return this.pos[0];
    }

    @Override
    public double getPosY() {
        // TODO Auto-generated method stub
        return this.pos[1];
    }

    @Override
    public String getID() {
        // TODO Auto-generated method stub
        return "MyText@" + this.createDate.getTime();
    }

    @Override
    public javafx.scene.shape.Shape deepCopy(double x, double y) {
        // TODO Auto-generated method stub
        MyText newText = new MyText(x, y, this.getText(), this.getFont(), this.getStrokeWidth(), this.getStroke(),
                this.getFill());
        return newText;
    }

    @Override
    public javafx.scene.shape.Shape deepCopy(double... points) {
        // TODO Auto-generated method stub
        return this.deepCopy(points[0], points[1]);
    }

    @Override
    public double[] getPos() {
        // TODO Auto-generated method stub
        return this.pos;
    }


    @Override
    public javafx.scene.shape.Shape reply() {
        // TODO Auto-generated method stub
        Paint fill = this.fillColor.equals("") ? null : Color.valueOf(this.fillColor);
        MyText newShape = new MyText(this.getPosX(), this.getPosY(), this.text, new Font(this.fontFamily, this.size),
                this.lineWidth, Color.valueOf(this.lineColor), fill);
        newShape.createDate = this.createDate;
        return newShape;
    }


    public String toString() {
        return this.getID() + "[ pos=[x=" + (int) this.getPosX() + ", y=" + (int) this.getPosY() + "] " + " text=" + this.text + " size=" + this.size + ", strokeWidth=" + this.lineWidth +
                ", stroke=" + this.lineColor + ", fill=" + this.fillColor + ", createDate=" + this.createDate + "]";
    }


    @Override
    public double getArea() {
        // TODO Auto-generated method stub
        return 0;
    }


    @Override
    public double getPerimeter() {
        // TODO Auto-generated method stub
        return 0;
    }


    public void changeTextAttr(double lineWidth, double size, String fontFamily, Paint lineColor, Paint fillColor) {
        this.setStrokeWidth(lineWidth);
        this.setStroke(lineColor);
        this.setFill(fillColor);
        this.setFont(new Font(fontFamily, size));
        this.lineWidth = lineWidth;
        this.size = size;
        this.fontFamily = fontFamily;
        this.lineColor = (lineColor == null ? "" : ((Color) lineColor).toString());
        this.fillColor = (fillColor == null ? "" : ((Color) fillColor).toString());
    }

}


