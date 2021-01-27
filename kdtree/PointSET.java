/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
    private Set<Point2D> points;

    // construct an empty set of points
    public PointSET() {
        this.points = new TreeSet<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        validate(p);
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        validate(p);
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        validate(rect);
        List<Point2D> result = new ArrayList<Point2D>();
        for (Point2D p : points) {
            if (rect.contains(p)) {
                result.add(p);
            }
        }
        return result;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        validate(p);
        Point2D nearest = null;
        for (Point2D z : points) {
            if (nearest == null || p.distanceTo(z) < p.distanceTo(nearest))
                nearest = z;
        }
        return nearest;
    }

    private void validate(Object o) {
        if (o == null) throw new IllegalArgumentException("Null argument");
    }

    public static void main(String[] args) {
        StdDraw.setPenRadius(0.01);

        List<Point2D> points = new ArrayList<>();
        points.add(new Point2D(0.372, 0.497));
        points.add(new Point2D(0.564, 0.413));
        points.add(new Point2D(0.226, 0.577));
        points.add(new Point2D(0.144, 0.179));
        points.add(new Point2D(0.083, 0.510));
        points.add(new Point2D(0.320, 0.708));
        points.add(new Point2D(0.417, 0.362));
        points.add(new Point2D(0.862, 0.825));
        points.add(new Point2D(0.785, 0.725));
        points.add(new Point2D(0.499, 0.208));

        PointSET set = new PointSET();
        for (Point2D p : points) {
            set.insert(p);
        }

        set.draw();

        RectHV rect = new RectHV(0.144, 0.179, 0.372, 0.497);
        rect.draw();

        StdDraw.setPenColor(StdDraw.RED);
        for (Point2D interseptionPoint : set.range(rect)) {
            interseptionPoint.draw();
        }

        Point2D testPoint = new Point2D(0.550, 0.400);
        StdDraw.setPenColor(StdDraw.ORANGE);
        testPoint.draw();

        Point2D nearest = set.nearest(testPoint);
        StdDraw.setPenColor(StdDraw.GREEN);
        nearest.draw();
    }
}
