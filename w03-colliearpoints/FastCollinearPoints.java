/* *****************************************************************************
 * A faster, sorting-based solution.
 * Think of p as the origin.
 * For each other point q, determine the slope it makes with p.
 * Sort the points according to the slopes they make with p.
 * Check if any 3 (or more) adjacent points in the sorted order have equal
 * slopes with respect to p. If so, these points, together with p, are collinear.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] segments;
    private int counter = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        checkPoints(points);
        ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
        Point[] copyP = points.clone();


        for (int i = 0; i < points.length-3; i++) {
            Arrays.sort(copyP);  // need this step to place points in order, the furthest point at the 'end'
            Arrays.sort(copyP, copyP[i].slopeOrder());  // sort by slope

            // have to find at least 4 points
            // first(copyP[0]), second, ...,end(>= second + 2)
            for (int second = 1, end = 2; end < copyP.length;) {
                // if we find two points have the same slope with points[i](copyP[0])
                // get the 'end' (the last point)

                // used to use an if block first to find the first points but not necessary, would cause test failure
                while (end < copyP.length && copyP[0].slopeTo(copyP[second]) == copyP[0].slopeTo(copyP[end])) {
                    end++;
                }

                // add iff copyP[0] is the smallest point
                if (end >= second + 3 && copyP[0].compareTo(copyP[second]) < 0) {  // after the while loop 'end' is 1 bigger
                    lines.add(new LineSegment(copyP[0], copyP[end-1]));
                    counter++;
                }
                second = end;
                end++;
            }
        }
        segments = lines.toArray(new LineSegment[counter]);
    }


    // check if input is valid
    private void checkPoints(Point[] points) {
        if (points == null || points.length == 0) {
            throw new IllegalArgumentException("No points given.");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Null point given.");
            }
        }
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Equal points given.");
                }
            }
        }
    }


    // the number of line segments
    public int numberOfSegments() {
        return counter;
    }

    // the number of line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, counter);
    }


    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
