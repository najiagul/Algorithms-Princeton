import java.util.Arrays;
/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

public class BruteCollinearPoints {
    private LineSegment[] segments;
    private int numsegment;

    public BruteCollinearPoints(Point[] points) // finds all line segments containing 4 points
    {
        // validate input
        validInput(points);

        int length = points.length;
        this.segments = new LineSegment[length];
        this.numsegment = 0;

        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                for (int k = j + 1; k < length; k++) {
                    if (isCollinear(points[i], points[j], points[k]))
                    // if three are not collinear, don't look for the fourth one
                    {
                        for (int l = k + 1; l < length; l++) {
                            if (isCollinear(points[l], points[k], points[j])) {
                                addSegment(points[l], points[k], points[j], points[i]);
                            }
                        }
                    }
                }
            }
        }
    }

    private void validInput(Point[] point) {
        if (point == null) throw new IllegalArgumentException("No argument passed");
        for (int i = 0; i < point.length; i++) {
            if (point[i] == null) throw new IllegalArgumentException("Points can not be null");
            for (int j = i + 1; j < point.length; j++) {
                if (point[i].compareTo(point[j]) == 0)
                    throw new IllegalArgumentException("Input contains repeated points");
            }
        }
    }

    private boolean isCollinear(Point p1, Point p2, Point p3) {
        if (p1.slopeTo(p2) == p1.slopeTo(p3)) return true;
        return false;
    }

    private void addSegment(Point p1, Point p2, Point p3, Point p4) {
        Point[] collinearPoints = { p1, p2, p3, p4 };
        Arrays.sort(collinearPoints);
        segments[numsegment++] = new LineSegment(collinearPoints[0], collinearPoints[3]);
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numsegment;
    }

    public LineSegment[] segments()                // the line segments
    {
        return Arrays.copyOfRange(segments, 0, numsegment);
    }
}
