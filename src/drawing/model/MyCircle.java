package drawing.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.util.Date;


/**
 * 重新Circle
 */
public class MyCircle extends Circle implements Serializable, MyShape {

    private static final long serialVersionUID = 8068768920500871671L;

    private double[] pos;
    private Date createDate;
    protected double radius, lineWidth;
    protected String lineColor;
    protected String fillColor;
    private boolean dashLine;

    public MyCircle(boolean dashLine, double x, double y, double radius, double lineWidth, Paint lineColor, Paint fillColor) {
        super(x, y, radius);

        this.setStrokeWidth(lineWidth);
        this.setStroke(lineColor);
        this.setFill(fillColor);
        if (dashLine) {
            this.getStrokeDashArray().addAll(8.0);
            this.setStrokeDashOffset(1.0);
        }
        this.dashLine = dashLine;
        this.pos = new double[]{x, y};
        this.createDate = new Date();
        this.radius = radius;
        this.lineColor = ((Color) lineColor).toString();
        this.fillColor = fillColor == null ? "" : ((Color) fillColor).toString();
        this.lineWidth = lineWidth;
    }


    public double getMyRadius() {
        return radius;
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
        return "MyCircle@" + this.createDate.getTime();
    }

    @Override
    public javafx.scene.shape.Shape deepCopy(double x, double y) {
        // TODO Auto-generated method stub
        MyCircle newCircle = new MyCircle(dashLine, x, y, this.getRadius(),
                this.getStrokeWidth(), this.getStroke(), this.getFill());
        return newCircle;
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
    public Shape reply() {
        // TODO Auto-generated method stub
        Paint fill = this.fillColor.equals("") ? null : Color.valueOf(this.fillColor);
        MyCircle newShape = new MyCircle(dashLine, this.getPosX(), this.getPosY(), this.radius, this.lineWidth,
                Color.valueOf(this.lineColor), fill);
        newShape.createDate = this.createDate;
        return newShape;
    }

    @Override
    public double getLineWidth() {
        return lineWidth;
    }

    @Override
    public String getLineColor() {
        return lineColor;
    }

    @Override
    public String getFillColor() {
        return fillColor;
    }

    @Override
    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public void setLineColor(String lineColor) {
        this.lineColor = lineColor;
    }

    @Override
    public void setFillColor(String fillColor) {
        this.fillColor = fillColor;
    }

    @Override
    public boolean isDashLine() {
        return dashLine;
    }

    public String toString() {
        return this.getID() + "[ pos=[x=" + (int) this.getPosX() + ", y=" + (int) this.getPosY() + "] radius=" + (int) this.radius +
                ", area=" + this.getArea() +
                ", strokeWidth=" + this.lineWidth +
                ", stroke=" + this.lineColor + ", fill=" + this.fillColor + ", createDate=" + this.createDate + "]";
    }

    @Override
    public double getArea() {
        // TODO Auto-generated method stub
        return Math.PI * (int) this.radius * (int) this.radius;
    }

    @Override
    public double getPerimeter() {
        // TODO Auto-generated method stub
        return 2 * Math.PI * (int) this.radius;
    }
}
