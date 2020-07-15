package drawing.utils;

/**
 * 计算多边形
 */
public final class PolygonUtil {
    public static double[] nextPoint(double x, double y, double r, double arc) {// arc为弧度，在顶点处建立直角坐标系，用r和arc确定下一个点的坐标
        double[] p = new double[2];
        p[0] = (x - r * Math.sin(arc));
        p[1] = (y + r - r * Math.cos(arc));
        return p;
    }
}
