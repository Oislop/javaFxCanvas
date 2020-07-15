package drawing.model;


import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.util.Date;

/**
 * 重新Line
 */
public class MyPen extends Line implements Serializable, MyShape {


    private static final long serialVersionUID = -1957785502049396901L;
    protected Date createDate;
    protected double[] pos;
    protected double lineWidth;
    protected String lineColor;
    protected String fillColor;
    private boolean dashLine;

    public MyPen(boolean dashLine, double startX, double startY, double endX, double endY,
                  double lineWidth, Paint lineColor, Paint fillColor) {
        super(startX, startY, endX, endY);
        this.setStrokeWidth(lineWidth);
        this.setStroke(lineColor);
        this.setFill(fillColor);
        if (dashLine) {
            this.getStrokeDashArray().addAll(8.0);
            this.setStrokeDashOffset(1.0);
        }
        this.dashLine = dashLine;
        this.pos = new double[]{startX, startY, endX, endY};
        this.createDate = new Date();
        this.lineWidth = lineWidth;
        this.lineColor = (lineColor == null ? "" : ((Color) lineColor).toString());
        this.fillColor = (fillColor == null ? "" : ((Color) fillColor).toString());
    }

    @Override
    public Shape deepCopy(double x, double y) {
        // TODO Auto-generated method stub
        double offsetX = x - this.pos[0];
        double offsetY = y - this.pos[1];
        MyPen newPen = new MyPen(dashLine, x, y, this.pos[2] + offsetX, this.pos[3] + offsetY,
                this.getStrokeWidth(), this.getStroke(), this.getFill());
        return newPen;
    }

    @Override
    public Shape deepCopy(double... points) {
        // TODO Auto-generated method stub
        return this.deepCopy(points[0], points[1]);

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
    public double[] getPos() {
        // TODO Auto-generated method stub
        return this.pos;
    }

    @Override
    public String getID() {
        // TODO Auto-generated method stub
        return "MyPen@" + this.createDate.getTime();
    }


    @Override
    public Shape reply() {
        // TODO Auto-generated method stub
        Paint fill = this.fillColor.equals("") ? null : Color.valueOf(this.fillColor);
        Paint lineColor = this.lineColor.equals("") ? null : Color.valueOf(this.lineColor);
        MyPen newShape = new MyPen(dashLine, this.getPosX(), this.getPosY(), this.pos[2], this.pos[3],
                this.lineWidth, lineColor, fill);
        newShape.createDate = this.createDate;
        return newShape;
    }

    public String toString() {
        return this.getID() + "[ pos=[startX=" + (int) this.getPosX() + ","
                + " StartY=" + (int) this.getPosY() + ", endX=" + (int) this.pos[2] + ", endY=" + (int) this.pos[3] + "] length=" + (int) this.getPerimeter() +
                ", strokeWidth=" + (int) this.lineWidth + ", stroke=" + this.lineColor + ", fill=" + this.fillColor + "]";
    }

    @Override
    public double getArea() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public double getPerimeter() {
        // TODO Auto-generated method stub
        return Math.sqrt((this.pos[0] - this.pos[2]) * (this.pos[0] - this.pos[2]) + (this.pos[1] - this.pos[3]) * (this.pos[1] - this.pos[3]));
    }

    @Override
    public boolean isDashLine() {
        return dashLine;
    }

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
}
