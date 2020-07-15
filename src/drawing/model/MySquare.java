package drawing.model;

import javafx.scene.paint.Paint;



/**
 * 正方形
 */
public class MySquare  extends MyRectangle {

    private static final long serialVersionUID = -8970717072923363068L;

    public MySquare(boolean dashLine,double rotate,boolean isArc, double x, double y, double edgeLen, double lineWidth, Paint lineColor, Paint fillColor) {
        super(dashLine,rotate,isArc,x, y, edgeLen, edgeLen, lineWidth, lineColor, fillColor);
        if(isArc) {
            this.setArcWidth(20);
            this.setArcHeight(20);
        }
    }
}
