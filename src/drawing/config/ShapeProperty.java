package drawing.config;

import javafx.scene.paint.Color;

/**
 * 图形类，纪录当前图形需要的属性
 */
public class ShapeProperty {
    public static String toolName = "PEN";
    public static String borderSize = "3";
    public static int lineSize = 3;
    public static int fontSize = 12;
    public static String fontName = "Arial";
    public static Color color = Color.BLACK;
    public static String text = "";
    public static int polygonN = 3;
    public static boolean drawPolygon = false;
    public static boolean isDashLine = false;

    public static boolean isIsDashLine() {
        return isDashLine;
    }

    public static void setIsDashLine(boolean isDashLine) {
        ShapeProperty.isDashLine = isDashLine;
    }

    public static boolean isDrawPolygon() {
        return drawPolygon;
    }

    public static void setDrawPolygon(boolean drawPolygon) {
        ShapeProperty.drawPolygon = drawPolygon;
    }

    public static int getPolygonN() {
        return polygonN;
    }

    public static void setPolygonN(int polygonN) {
        ShapeProperty.polygonN = polygonN;
    }

    public static void setToolName(String toolName) {
        ShapeProperty.toolName = toolName;
    }

    public static void setBorderSize(String borderSize) {
        ShapeProperty.borderSize = borderSize;
    }

    public static void setLineSize(int lineSize) {
        ShapeProperty.lineSize = lineSize;
    }

    public static void setFontSize(int fontSize) {
        ShapeProperty.fontSize = fontSize;
    }

    public static void setFontName(String fontName) {
        ShapeProperty.fontName = fontName;
    }

    public static void setColor(Color color) {
        ShapeProperty.color = color;
    }

    public static void setText(String text) {
        ShapeProperty.text = text;
    }
}