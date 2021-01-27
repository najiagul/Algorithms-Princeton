import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private int length;
    private Node root;

    private static class Node {
        private Point2D point;      // the point
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p) {
            this.point = p;
        }
    }

    // construct an empty set of points
    public KdTree() {
        length = 0;

    }

    // is the set empty?
    public boolean isEmpty() {
        return length == 0;
    }

    // number of points in the set
    public int size() {
        return length;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        root = put(root, p, true);
    }

    // even              - left/right
    // odd (!even)       - bellow/above
    private Node put(Node node, Point2D point, boolean even) {
        if (node == null) {
            length++;
            return new Node(point);
        }
        if (node.point.equals(point)) return node;

        int cmp = even ? Point2D.X_ORDER.compare(point, node.point) :
                  Point2D.Y_ORDER.compare(point, node.point);
        if (cmp < 0) node.lb = put(node.lb, point, !even);
        else node.rt = put(node.rt, point, !even);
        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D point) {
        if (point == null) throw new java.lang.IllegalArgumentException();

        return get(root, point, true) != null;
    }

    private Node get(Node node, Point2D point, boolean even) {
        if (node == null) return null;

        if (node.point.equals(point)) return node;

        int cmp = even ? Point2D.X_ORDER.compare(point, node.point) :
                  Point2D.Y_ORDER.compare(point, node.point);
        if (cmp < 0) return get(node.lb, point, !even);
        else return get(node.rt, point, !even);
    }

    // draw all points to standard draw
    public void draw() {
        drawBranch(root, true, 0, 1, 0, 1);
    }

    private void drawBranch(Node node, boolean even, double minX, double maxX, double minY,
                            double maxY) {
        if (node == null) return;
        drawNode(node);
        drawLineNode(node, even, minX, maxX, minY, maxY);

        if (even) {
            drawBranch(node.lb, false, minX, node.point.x(), minY, maxY);
            drawBranch(node.rt, false, node.point.x(), maxX, minY, maxY);
        }
        else {
            drawBranch(node.lb, true, minX, maxX, minY, node.point.y());
            drawBranch(node.rt, true, minX, maxX, node.point.y(), maxY);
        }
    }

    private void drawNode(Node x) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        x.point.draw();
    }

    private void drawLineNode(Node node, boolean even, double minX, double maxX, double minY,
                              double maxY) {
        StdDraw.setPenRadius();
        if (even) {
            //vertical
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), minY, node.point.x(), maxY);
        }
        else {
            //horizontal
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(minX, node.point.y(), maxX, node.point.y());
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.IllegalArgumentException();

        List<Point2D> result = new ArrayList<>();

        intersect(result, rect, root, true);

        return result;
    }

    private void intersect(List<Point2D> result, RectHV rect, Node node, boolean even) {
        if (node == null) return;

        if (rect.contains(node.point)) result.add(node.point);

        if (even) {
            if (node.point.x() <= rect.xmax() && node.point.x() > rect.xmin()) {
                intersect(result, rect, node.lb, false);
                intersect(result, rect, node.rt, false);
            }
            else if (node.point.x() > rect.xmax()) {
                intersect(result, rect, node.lb, false);
            }
            else {
                intersect(result, rect, node.rt, false);
            }
        }
        else {
            if (node.point.y() <= rect.ymax() && node.point.y() > rect.ymin()) {
                intersect(result, rect, node.lb, true);
                intersect(result, rect, node.rt, true);
            }
            else if (node.point.y() > rect.ymax()) {
                intersect(result, rect, node.lb, true);
            }
            else {
                intersect(result, rect, node.rt, true);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.IllegalArgumentException();
        if (isEmpty()) return null;

        return nearest(root, p, root.point, true);
    }

    private Point2D nearest(Node node, Point2D point, Point2D champion, boolean even) {
        if (node == null) return champion;

        Point2D newChampion = champion;
        if (node.point.distanceSquaredTo(point) < champion.distanceSquaredTo(point)) {
            newChampion = node.point;
        }

        int cmp = even ? Point2D.X_ORDER.compare(point, node.point) :
                  Point2D.Y_ORDER.compare(point, node.point);

        Double orientationDifference = even ? node.point.x() - point.x() :
                                       node.point.y() - point.y();

        if (cmp < 0) {
            newChampion = nearest(node.lb, point, newChampion, !even);

            if (newChampion.distanceSquaredTo(point)
                    > orientationDifference * orientationDifference) {
                newChampion = nearest(node.rt, point, newChampion, !even);
            }
        }
        else {
            newChampion = nearest(node.rt, point, newChampion, !even);

            if (newChampion.distanceSquaredTo(point)
                    > orientationDifference * orientationDifference) {
                newChampion = nearest(node.lb, point, newChampion, !even);
            }
        }

        return newChampion;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        StdDraw.setPenRadius(0.01);

        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.206107, 0.095492));
        points.add(new Point2D(0.975528, 0.654508));
        points.add(new Point2D(0.024472, 0.345492));
        points.add(new Point2D(0.793893, 0.095492));
        points.add(new Point2D(0.793893, 0.904508));
        points.add(new Point2D(0.975528, 0.345492));
        points.add(new Point2D(0.206107, 0.904508));
        points.add(new Point2D(0.500000, 0.000000));
        points.add(new Point2D(0.024472, 0.654508));
        points.add(new Point2D(0.500000, 1.000000));

        KdTree tree = new KdTree();
        for (Point2D p : points) {
            tree.insert(p);
        }

        tree.draw();
        Point2D testPoint = new Point2D(0.550, 0.600);
        StdDraw.setPenColor(StdDraw.ORANGE);
        testPoint.draw();

        Point2D nearest = tree.nearest(testPoint);
        StdDraw.setPenColor(StdDraw.GREEN);
        nearest.draw();
    }
}
