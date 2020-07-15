package drawing.utils;

import javafx.scene.Node;
import javafx.scene.shape.*;

import java.util.List;

public class Geometry {
    static final double eps = 1e-6;

    static class Point {
        double x, y;

        Point(double xx, double yy) {
            x = xx;
            y = yy;
        }
    }

    static double getCenterX(Node node) {
        return (node.getLayoutBounds().getMaxX() + node.getLayoutBounds().getMinX()) / 2;
    }

    static double getCenterY(Node node) {
        return (node.getLayoutBounds().getMaxY() + node.getLayoutBounds().getMinY()) / 2;
    }


    public static boolean inRange(double x1, double y1, double x2, double y2, Node node) {

        x1 = x1 - node.getLayoutX();
        y1 = y1 - node.getLayoutY();
        x2 = x2 - node.getLayoutX();
        y2 = y2 - node.getLayoutY();
        //被包含
        if (contain(node, x1, y1)) return true;
        if (contain(node, x1, y2)) return true;
        if (contain(node, x2, y1)) return true;
        if (contain(node, x2, y2)) return true;

        //相交
        if (intersect(x1, y1, x2, y2, node)) return true;

        //包含其任意一点
        Point point = getPoint(node);
        if (new Rectangle(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2)).contains(point.x, point.y))
            return true;

        return false;
    }

    static boolean contain(Node node, double x, double y) {
        Point A = new Point(x, y);
        double angle = node.getRotate();
        Point P = new Point(getCenterX(node), getCenterY(node));
        return node.contains(rotate(A, P, -angle).x, rotate(A, P, -angle).y);
    }

    static boolean intersect(double x1, double y1, double x2, double y2, Node node) {
        double angle = node.getRotate();
        if (node instanceof Line) {
            if (intersect(new Line(x1, y1, x1, y2), (Line) node)) return true;
            if (intersect(new Line(x1, y2, x2, y2), (Line) node)) return true;
            if (intersect(new Line(x2, y2, x2, y1), (Line) node)) return true;
            if (intersect(new Line(x2, y1, x1, y1), (Line) node)) return true;

            return false;
        } else if (node instanceof Rectangle) {
            double X = ((Rectangle) node).getX();
            double Y = ((Rectangle) node).getY();
            double endX = X + ((Rectangle) node).getWidth();
            double endY = Y + ((Rectangle) node).getHeight();

            Point P = new Point(getCenterX(node), getCenterY(node));
            if (intersect(x1, y1, x2, y2, rotate(new Line(X, Y, X, endY), P, angle))) return true;
            if (intersect(x1, y1, x2, y2, rotate(new Line(X, endY, endX, endY), P, angle))) return true;
            if (intersect(x1, y1, x2, y2, rotate(new Line(endX, endY, endX, Y), P, angle))) return true;
            if (intersect(x1, y1, x2, y2, rotate(new Line(endX, Y, X, Y), P, angle))) return true;

            return false;
        } else if (node instanceof Polyline) {
            List<Double> a = ((Polyline) node).getPoints();

            Point P = new Point(getCenterX(node), getCenterY(node));
            if (intersect(x1, y1, x2, y2, rotate(new Line(a.get(a.size() - 2), a.get(a.size() - 1), a.get(0), a.get(1)), P, angle)))
                return true;
            for (int i = 0; i + 3 < a.size(); i += 2)
                if (intersect(x1, y1, x2, y2, rotate(new Line(a.get(i), a.get(i + 1), a.get(i + 2), a.get(i + 3)), P, angle)))
                    return true;

            return false;
        } else if (node instanceof Ellipse) {
            Ellipse ellipse = (Ellipse) node;
            Point A = new Point(x1, y1), B = new Point(x1, y2);
            Point C = new Point(x2, y2), D = new Point(x2, y1);
            if (Math.abs(angle) > eps) {
                ellipse = new Ellipse(ellipse.getCenterX(), ellipse.getCenterY(), ellipse.getRadiusX(), ellipse.getRadiusY());
                Point P = new Point(getCenterX(node), getCenterY(node));
                A = rotate(A, P, -angle);
                B = rotate(B, P, -angle);
                C = rotate(C, P, -angle);
                D = rotate(D, P, -angle);
            }
            if (intersect(new Line(A.x, A.y, B.x, B.y), ellipse)) return true;
            if (intersect(new Line(B.x, B.y, C.x, C.y), ellipse)) return true;
            if (intersect(new Line(C.x, C.y, D.x, D.y), ellipse)) return true;
            if (intersect(new Line(D.x, D.y, A.x, A.y), ellipse)) return true;

            return false;
        }
        return false;
    }

    static Point getPoint(Node node) {
        //得到node中的一个点
        double angle = node.getRotate();
        Point P = new Point(getCenterX(node), getCenterY(node));
        if (node instanceof Line) {
            return rotate(new Point(((Line) node).getStartX(), ((Line) node).getStartY()), P, angle);
        } else if (node instanceof Rectangle) {
            return rotate(new Point(((Rectangle) node).getX(), ((Rectangle) node).getY()), P, angle);
        } else if (node instanceof Polyline) {
            return rotate(new Point(((Polyline) node).getPoints().get(0), ((Polyline) node).getPoints().get(1)), P, angle);
        } else if (node instanceof Ellipse) {
            return new Point(getCenterX(node), getCenterY(node));
        }
        return null;
    }

    static Point rotate(Point A, Point B, double angle) {
        //A绕着B转angle
        //angle在此处才转化为弧度，其他地方用的都是角度
        angle = angle / 180 * Math.PI;
        double dx = A.x - B.x, dy = A.y - B.y;
        return new Point(dx * Math.cos(angle) - dy * Math.sin(angle) + B.x, dy * Math.cos(angle) + dx * Math.sin(angle) + B.y);
    }

    static Line rotate(Line l, double angle) {
        //l自转angle
        Point A = new Point(l.getStartX(), l.getStartY());
        Point B = new Point(l.getEndX(), l.getEndY());
        Point P = new Point(getCenterX(l), getCenterY(l));
        return new Line(rotate(A, P, angle).x, rotate(A, P, angle).y, rotate(B, P, angle).x, rotate(B, P, angle).y);
    }

    static Line rotate(Line l, Point P, double angle) {
        //l绕P转angle
        Point A = new Point(l.getStartX(), l.getStartY());
        Point B = new Point(l.getEndX(), l.getEndY());
        return new Line(rotate(A, P, angle).x, rotate(A, P, angle).y, rotate(B, P, angle).x, rotate(B, P, angle).y);
    }

    static boolean left(double x, double y, Line l) {
        //点(x,y)是否在线段l的左边
        l = rotate(l, l.getRotate());
        double x1 = l.getEndX() - l.getStartX();
        double y1 = l.getEndY() - l.getStartY();
        double x2 = x - l.getStartX();
        double y2 = y - l.getStartY();
        return x1 * y2 - x2 * y1 > 0;
    }

    static boolean intersect(Line l1, Line l2) {
        //线段l1和l2是否相交
        l1 = rotate(l1, l1.getRotate());
        l2 = rotate(l2, l2.getRotate());
        if (left(l1.getStartX(), l1.getStartY(), l2) == left(l1.getEndX(), l1.getEndY(), l2)) return false;
        if (left(l2.getStartX(), l2.getStartY(), l1) == left(l2.getEndX(), l2.getEndY(), l1)) return false;
        return true;
    }

    static boolean intersect(Line l, Ellipse ellipse) {
        //线段l和椭圆ellipse是否相交
        l = rotate(l, l.getRotate());
        Point A = new Point(l.getStartX(), l.getStartY());
        Point B = new Point(l.getEndX(), l.getEndY());
        Boolean containsA, containsB;
        containsA = ellipse.contains(A.x, A.y);
        containsB = ellipse.contains(B.x, B.y);
        if (containsA != containsB) return true;
        if (containsA && containsB) return false;
        A.x -= ellipse.getCenterX();
        A.y -= ellipse.getCenterY();
        B.x -= ellipse.getCenterX();
        B.y -= ellipse.getCenterY();
        double a = ellipse.getRadiusX();
        double b = ellipse.getRadiusY();
        if (Math.abs(A.x - B.x) < eps) {
            double t = (A.x + B.x) / 2;
            t = Math.abs(b * Math.sqrt(1 - t * t / (1 * a * a)));
            return t <= Math.max(A.y, B.y) && t >= Math.min(A.y, B.y);
        }
        double k = (A.y - B.y) / (A.x - B.x);
        double t = A.x * (A.y - B.y) / (B.x - A.x) + A.y;
        double delta = a * a * a * a * t * t * k * k - (a * a * k * k + b * b) * (a * a * t * t - a * a * b * b);
        if (delta < -eps) return false;
        double x1 = (-2 * a * a * t * k - Math.sqrt(delta)) / (2 * (a * a * k * k + b * b));
        if (x1 < Math.min(A.x, B.x) || x1 > Math.max(A.x, B.x)) return false;
        double x2 = (-2 * a * a * t * k + Math.sqrt(delta)) / (2 * (a * a * k * k + b * b));
        if (x2 < Math.min(A.x, B.x) || x2 > Math.max(A.x, B.x)) return false;
        return true;
    }

}