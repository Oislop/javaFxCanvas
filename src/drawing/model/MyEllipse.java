package drawing.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.util.Date;

/**
 * 重新Ellipse
 */
public class MyEllipse extends Ellipse implements Serializable, MyShape {

    private static final long serialVersionUID = -4332225396150436010L;
    private double[] pos;
    private Date createDate;
    protected double lineWidth, radiusX, radiusY;
    protected String lineColor, fillColor;
    private double rotate;
    private boolean dashLine;

    public MyEllipse(boolean dashLine,double rotate,double x, double y, double radiusX, double radiusY, double lineWidth, Paint lineColor, Paint fillColor) {
        super(x, y, radiusX, radiusY);
        this.setStroke(lineColor);
        this.setFill(fillColor);
        this.setStrokeWidth(lineWidth);
        this.setRotate(rotate);
        if(dashLine) {
            this.getStrokeDashArray().addAll(8.0);
            this.setStrokeDashOffset(1.0);
        }
        this.dashLine = dashLine;
        this.rotate = rotate;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.createDate = new Date();
        this.pos = new double[]{x, y};
        this.lineWidth = lineWidth;
        this.lineColor = (lineColor == null ? "" : ((Color) lineColor).toString());
        this.fillColor = (fillColor == null ? "" : ((Color) lineColor).toString());
    }


    public double getMyRadiusX() {
        return radiusX;
    }

    public double getMyRadiusY() {
        return radiusY;
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
        return "MyEllipse@" + this.createDate.getTime();
    }


    @Override
    public Shape reply() {
        // TODO Auto-generated method stub
        Paint fill = this.fillColor.equals("") ? null : Color.valueOf(this.fillColor);
        Paint lineColor = this.lineColor.equals("") ? null : Color.valueOf(this.lineColor);
        MyEllipse newShape = new MyEllipse(dashLine,rotate,this.getPosX(), this.getPosY(), this.radiusX, this.radiusY,
                this.lineWidth, lineColor, fill);
        newShape.createDate = this.createDate;
        return newShape;
    }

    @Override
    public Shape deepCopy(double x, double y) {
        // TODO Auto-generated method stub
        MyEllipse newShape = new MyEllipse(dashLine,rotate,x, y, this.radiusX, this.radiusY,
                this.lineWidth, this.getStroke(), this.getFill());
        newShape.createDate = this.createDate;
        System.out.println("new Ellipse:" + newShape.toString());
        return newShape;
    }

    @Override
    public Shape deepCopy(double... points) {
        // TODO Auto-generated method stub
        return this.deepCopy(points[0], points[1]);
    }

    public String toString() {
        return this.getID() + "[ pos=[x=" + (int) this.getPosX() + ", y=" + (int) this.getPosY() + "] " + " Xradius=" + (int) this.radiusX +
                " Yradius=" + (int) this.radiusY + ", area=" + (int) this.getArea() + ", perimeter=" + (int) this.getPerimeter() +
                ", strokeWidth=" + this.lineWidth + ", stroke=" + this.lineColor + ", fill=" + this.fillColor + ", createDate=" + this.createDate + "]";
    }

    @Override
    public double getArea() {
        // TODO Auto-generated method stub
        return (int) this.radiusX * (int) this.radiusY * Math.PI;
    }

    @Override
    public double getPerimeter() {
        // TODO Auto-generated method stub
        return 4 * ((int) this.radiusX + (int) this.radiusY - (4 - Math.PI) * ((int) this.radiusX * (int) this.radiusY) / ((int) this.radiusX + (int) this.radiusY));
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

    @Override
    public boolean isDashLine() {
        return dashLine;
    }
}