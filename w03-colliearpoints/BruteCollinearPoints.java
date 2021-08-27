/* *****************************************************************************
 *  Name: J
 *  Date: 2021-08-19
 *  Description: Brute force. Write a program BruteCollinearPoints.java that
 *  examines 4 points at a time and checks whether they all lie on the same line
 *  segment, returning all such line segments. To check whether the 4 points
 *  p, q, r, and s are collinear, check whether the three slopes between p and q, between p and r, and between p and s are all equal.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private final LineSegment[] segments;
    private int counter = 0;

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        checkPoints(points);
        ArrayList<LineSegment> lines = new ArrayList<LineSegment>();
        for (int i = 0; i < points.length-3; i++) {
            for (int j = i+1; j < points.length-2; j++) {
                for (int m = j+1; m < points.length-1; m++) {
                    for (int n = m+1; n < points.length; n++) {
                        if (points[i].compareTo(points[j]) == 0
                                || points[i].compareTo(points[m]) == 0
                                || points[i].compareTo(points[n]) == 0) {
                            throw new IllegalArgumentException("");
                        }
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[m])
                                && points[i].slopeTo(points[j]) == points[i].slopeTo(points[n])
                                && points[i].slopeTo(points[m]) == points[i].slopeTo(points[n]))
                        {
                            Point start = min(min(min(points[i], points[j]), points[m]), points[n]);
                            Point end = max(max(max(points[i], points[j]), points[m]), points[n]);
                            lines.add(new LineSegment(start, end));
                            counter++;
                        }
                    }
                }
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

    // the line segments
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, counter);
    }

    private Point max(Point a, Point b) {
        return a.compareTo(b) > 0 ? a : b;
    }

    private Point min(Point a, Point b) {
        return a.compareTo(b) < 0 ? a : b;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

