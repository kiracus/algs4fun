/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    private void checkNullInput(Object input) {
        if (input == null) {
            throw new IllegalArgumentException("null input.");
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNullInput(p);
        pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNullInput(p);
        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNullInput(rect);
        SET<Point2D> pointInside = new SET<>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                pointInside.add(p);
            }
        }
        return pointInside;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNullInput(p);
        if (pointSet.isEmpty()) { return null; }

        double minDistance = Double.POSITIVE_INFINITY;
        Point2D nearestPoint = null;
        for (Point2D point : pointSet) {
            if (p.distanceSquaredTo(point) < minDistance) {
                minDistance = p.distanceSquaredTo(point);
                nearestPoint = point;
            }
        }
        return nearestPoint;
    }

}
