package drawing.model;

import javafx.scene.shape.Shape;

/**
 * 自定义图形接口
 */
public interface MyShape {
    public Shape deepCopy(double x, double y);
    public Shape deepCopy(double... points);
    public double getPosX();
    public double getPosY();
    public double getArea();
    public double getPerimeter();
    public double[] getPos();
    public String getID();
    public Shape reply();
    public double getLineWidth();
    public String getLineColor();
    public String getFillColor();
    public void setLineWidth(double lineWidth);
    public void setLineColor(String lineColor);
    public void setFillColor(String fillColor);
    public boolean isDashLine();
}
