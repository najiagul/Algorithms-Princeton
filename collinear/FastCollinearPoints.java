import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FastCollinearPoints {
    private final Point[] points;
    private final ArrayList<LineSegment> segments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new java.lang.IllegalArgumentException();
        for (Point p : points) {
            if (p == null) throw new java.lang.IllegalArgumentException();
        }

        segments = new ArrayList<>();
        this.points = points;

        findCollinearPoints();
    }

    private void findCollinearPoints() {
        for (Point base : points) {
            Point[] bufArray = points.clone();
            Arrays.sort(bufArray, base.slopeOrder());

            if (bufArray.length > 1 && bufArray[0].compareTo(bufArray[1]) == 0) {
                throw new java.lang.IllegalArgumentException();
            }
            List<Point> collinearPoints = new ArrayList<>();

            int i = 1;  // first element in sorted array is base
            while (i < bufArray.length - 2) {
                double slope = base.slopeTo(bufArray[i]);

                int j = i + 1;
                if (slope == base.slopeTo(bufArray[j])) {
                    collinearPoints.add(bufArray[i]);
                    while (j < bufArray.length && slope == base.slopeTo(bufArray[j])) {
                        collinearPoints.add(bufArray[j]);
                        j++;
                    }
                }

                if (collinearPoints.size() >= 3) {
                    Point min = base;
                    Point max = base;
                    for (Point p : collinearPoints) {
                        if (min.compareTo(p) < 0) {
                            min = p;
                        }
                        if (max.compareTo(p) > 0) {
                            max = p;
                        }
                    }
                    if (min.equals(base)) {
                        segments.add(new LineSegment(min, max));
                    }
                }

                collinearPoints.clear();
                i = j;
            }
        }
    }

    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }

    // the line segments
    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[segments.size()]);
    }
}

