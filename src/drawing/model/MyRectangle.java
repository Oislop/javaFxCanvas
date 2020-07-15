package drawing.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.util.Date;


/**
 * 重写Rectangle
 */
public class MyRectangle extends Rectangle implements Serializable, MyShape {

    private static final long serialVersionUID = 3852460496573449701L;
    protected Date createDate;
    protected double[] pos;
    protected double width, height, lineWidth;
    protected String lineColor;
    protected String fillColor;
    private boolean isArc;
    private double rotate;
    private boolean dashLine;

    public MyRectangle(boolean dashLine, Double rotate, boolean isArc, double x, double y, double width, double height, double lineWidth, Paint lineColor, Paint fillColor) {
        super(x, y, width, height);
        this.setFill(fillColor);
        this.setStrokeWidth(lineWidth);
        this.setStroke(lineColor);
        this.isArc = isArc;
        if (isArc) {
            this.setArcWidth(20);
            this.setArcHeight(20);
        }
        if (dashLine) {
            this.getStrokeDashArray().addAll(8.0);
            this.setStrokeDashOffset(1.0);
        }
        this.dashLine = dashLine;
        this.setRotate(rotate);
        this.rotate = rotate;
        this.pos = new double[]{x, y};
        this.createDate = new Date();
        this.width = width;
        this.height = height;
        this.lineWidth = lineWidth;
        this.lineColor = (lineColor == null ? "" : ((Color) lineColor).toString());
        this.fillColor = (fillColor == null ? "" : ((Color) fillColor).toString());
    }

    @Override
    public boolean isDashLine() {
        return dashLine;
    }

    public boolean isArc() {
        return isArc;
    }

    public double getMyWidth() {
        return width;
    }

    public double getMyHeight() {
        return height;
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
    public double[] getPos() {
        return this.pos;
    }

    @Override
    public double getPosX() {
        return this.pos[0];
    }

    @Override
    public double getPosY() {
        return this.pos[1];
    }

    @Override
    public double getArea() {
        return (int) this.getWidth() * (int) this.getHeight();
    }

    @Override
    public double getPerimeter() {
        return ((int) this.getWidth() + (int) this.getHeight()) * 2;
    }

    public Date getDate() {
        return this.createDate;
    }

    //使用创建时的秒数来唯一标记形状
    @Override
    public String getID() {
        return "MyRectangle@" + this.createDate.getTime();
    }

    @Override
    public MyRectangle deepCopy(double x, double y) {
        MyRectangle newRect = new MyRectangle(dashLine,
                rotate, isArc, x, y, this.getWidth(), this.getHeight(),
                this.getStrokeWidth(), this.getStroke(), this.getFill());

        return newRect;
    }

    @Override
    public Shape deepCopy(double... points) {
        // TODO Auto-generated method stub
        return this.deepCopy(points[0], points[1]);
    }

    @Override
    public Shape reply() {
        Paint fill = this.fillColor.equals("") ? null : Color.valueOf(this.fillColor);
        Paint lineColor = this.lineColor.equals("") ? null : Color.valueOf(this.lineColor);
        MyRectangle newShape = new MyRectangle(dashLine, rotate, isArc, this.getPosX(), this.getPosY(), this.width, this.height,
                this.lineWidth, lineColor, fill);
        newShape.createDate = this.createDate;
        return newShape;
    }

    public String toString() {
        return this.getID() + "[ pos=[x=" + (int) this.getPosX() + ", y=" + (int) this.getPosY() + "] width=" + (int) this.width + ", height=" + (int) this.height +
                        ", area=" + (int) this.getArea() + ", perimeter=" + (int) this.getPerimeter() +
                        ", strokeWidth=" + (int) this.lineWidth + ", stroke=" + this.lineColor + ", fill=" + this.fillColor + "]";
    }


    public Rectangle resize() {
        Rectangle rect = new Rectangle(this.getPosX(), this.getPosY(), this.width, this.height);
        rect.setStrokeDashOffset(10);
        rect.setFill(null);
        rect.getStrokeDashArray().addAll(5d, 5d, 5d, 5d);
        rect.setStroke(((Color) this.getStroke()).invert());
        return rect;
    }

}
