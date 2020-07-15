package drawing.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.io.Serializable;
import java.util.Date;

/**
 * 重新Polygon
 */
public class MyPolygon extends Polygon implements Serializable, MyShape {

    private static final long serialVersionUID = 7985307099173703729L;
    private static Date createDate;
    private static double[] pos;
    protected double lineWidth;
    protected String lineColor, fillColor;
    private double rotate;
    private boolean dashLine;
    private int polygonN;

    public MyPolygon(boolean dashLine, double rotate, double lineWidth, Paint lineColor, Paint fillColor, double... points) {
        super(points);
        this.pos = new double[this.getPoints().size()];
        for (int i = 0; i < points.length; i++) {
            this.pos[i] = this.getPoints().get(i);
        }
        this.createDate = new Date();
        this.setStrokeWidth(lineWidth);
        this.setStroke(lineColor);
        this.setFill(fillColor);
        this.setRotate(rotate);
        if (dashLine) {
            this.getStrokeDashArray().addAll(8.0);
            this.setStrokeDashOffset(1.0);
        }
        polygonN = pos.length /2;
        this.dashLine = dashLine;
        this.rotate = rotate;
        this.lineWidth = lineWidth;
        this.lineColor = (lineColor == null ? "" : ((Color) lineColor).toString());
        this.fillColor = (fillColor == null ? "" : ((Color) fillColor).toString());

    }

    public int getPolygonN() {
        return polygonN;
    }

    @Override
    public double[] getPos() {
        double[] points = new double[this.getPoints().size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = this.getPoints().get(i);
        }
        return points;
    }

    @Override
    public Shape deepCopy(double x, double y) {
        // TODO Auto-generated method stub
        //MyPolygon newPolygon = new MyPolygon
        throw new UnsupportedOperationException("method unsupport");
    }

    @Override
    public Shape deepCopy(double... points) {
        // TODO Auto-generated method stub
        MyPolygon newPolygon = new MyPolygon(dashLine, rotate, this.getStrokeWidth(), this.getStroke(), this.getFill(), points);
        return newPolygon;
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
        return "MyPolygon@" + this.createDate.getTime();
    }

    @Override
    public boolean isDashLine() {
        return dashLine;
    }

    @Override
    public javafx.scene.shape.Shape reply() {
        // TODO Auto-generated method stub
        Paint fill = this.fillColor.equals("") ? null : Color.valueOf(this.fillColor);
        MyPolygon newShape = new MyPolygon(dashLine, rotate, this.lineWidth, Color.valueOf(this.lineColor), fill, this.pos);
        newShape.createDate = this.createDate;
        return newShape;
    }

    public String toString() {
        String pos = "";
        for (double p : this.pos) {
            pos += (int) p + " ";
        }
        return this.getID() + "[ pos=[" + pos + "] area=" + (int) this.getArea() + ", perimeter=" + (int) this.getPerimeter() +
                ", strokeWidth=" + this.lineWidth + ", stroke=" + this.lineColor + ", fill=" + this.fillColor + ", createDate=" + this.createDate + "]";
    }

    @Override
    public double getArea() {
        // TODO Auto-generated method stub
        double p = this.getPerimeter();
        return Math.sqrt(p *
                (p - Math.sqrt((this.pos[0] - this.pos[2]) * (this.pos[0] - this.pos[2]) + (this.pos[1] - this.pos[2]) * (this.pos[0] - this.pos[3]))) *
                (p - Math.sqrt((this.pos[2] - this.pos[4]) * (this.pos[2] - this.pos[4]) + (this.pos[3] - this.pos[5]) * (this.pos[3] - this.pos[5]))) *
                (p - Math.sqrt((this.pos[4] - this.pos[0]) * (this.pos[4] - this.pos[0]) + (this.pos[5] - this.pos[1]) * (this.pos[5] - this.pos[1]))));
    }

    @Override
    public double getPerimeter() {
        // TODO Auto-generated method stub
        double p = 0;
        for (int i = 0; i < this.pos.length; i += 2) {
            p += Math.sqrt((this.pos[i] - this.pos[(i + 2) % this.pos.length]) * (this.pos[i] - this.pos[(i + 2) % this.pos.length]) +
                    (this.pos[i + 1] - this.pos[(i + 3) % this.pos.length]) * (this.pos[i + 1] - this.pos[(i + 3) % this.pos.length]));
        }
        return p;
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
