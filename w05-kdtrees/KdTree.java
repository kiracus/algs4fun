/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class KdTree {
    private static final double SMALLPEN = 0.005;
    private static final double LARGEPEN = 0.02;
    private static final boolean VERTICAL = true;
    private static final RectHV CONTAINER = new RectHV(0, 0, 1, 1);

    private class KdTreeNode {
        private final Point2D point;
        private final double x;
        private final double y;
        private final boolean isVertical;
        private KdTreeNode left;
        private KdTreeNode right;

        public KdTreeNode(Point2D point2d, boolean direction) {
            point = point2d;
            x = point.x();
            y = point.y();
            this.isVertical = direction;
            left = null;
            right = null;
        }
    }

    private KdTreeNode root;
    private int size;

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    private void checkNullInput(Object input) {
        if (input == null) {
            throw new IllegalArgumentException("null input.");
        }
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        checkNullInput(p);
        root = insert(root, p, VERTICAL);  // vertical in root level
    }

    private KdTreeNode insert(KdTreeNode node, Point2D p, boolean direction) {
        // no node here
        if (node == null) {
            size++;
            node = new KdTreeNode(p, direction);
        }

        // already have this node
        if (node.point.equals(p)) { return node; }

        // vertical: if the point has a smaller x-coordinate than root, go left
        // !vertical: if the point has a smaller y-coordinate than current root, go left
        if ((node.isVertical && p.x() < node.x) || (!node.isVertical && p.y() < node.y)) {
            node.left = insert(node.left, p, !node.isVertical);  // reverse direction
        } else {  // go right
            node.right = insert(node.right, p, !node.isVertical);
        }

        return node;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        checkNullInput(p);
        return contains(root, p);
    }

    private boolean contains(KdTreeNode node, Point2D p) {
        if (node == null) { return false; }
        if (node.point.equals(p)) { return true; }

        // same pattern as insert
        if ((node.isVertical && p.x() < node.x) || (!node.isVertical && p.y() < node.y)) {
            return contains(node.left, p);
        } else {  // go right
            return contains(node.right, p);
        }
    }

    // draw all points to standard draw
    public void draw() {
        StdDraw.setScale();  // default between (0.0 and 1.0)
        StdDraw.setPenRadius(SMALLPEN);
        StdDraw.setPenColor(StdDraw.BLACK);
        CONTAINER.draw();
        draw(root, CONTAINER);
    }

    private void draw(KdTreeNode node, RectHV rect) {
        // no node here
        if (node == null) { return; }

        // draw point
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(LARGEPEN);
        node.point.draw();

        // draw split line, update container rectangle, recursive call
        if (node.isVertical) {
            // vertical red line, from ymin to ymax of the rect, x at point.x
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(SMALLPEN);
            StdDraw.line(node.point.x(), rect.ymin(), node.point.x(), rect.ymax());
            // get two new containers then draw left and right nodes
            draw(node.left, leftRect(rect, node.point));
            draw(node.right, rightRect(rect, node.point));
        } else {
            // horizontal blue line, from xmin to xmax of the rect, y at point.y
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(SMALLPEN);
            StdDraw.line(rect.xmin(), node.point.y(), rect.xmax(), node.point.y());
            // get two new containers then draw left and right nodes
            draw(node.left, lowerRect(rect, node.point));
            draw(node.right, upperRect(rect, node.point));
        }
    }

    private RectHV leftRect(RectHV rect, Point2D p) {
        return new RectHV(rect.xmin(), rect.ymin(), p.x(), rect.ymax());
    }

    private RectHV rightRect(RectHV rect, Point2D p) {
        return new RectHV(p.x(), rect.ymin(), rect.xmax(), rect.ymax());
    }

    private RectHV lowerRect(RectHV rect, Point2D p) {
        return new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), p.y());
    }

    private RectHV upperRect(RectHV rect, Point2D p) {
        return new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax());
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        checkNullInput(rect);

        Queue<Point2D> pointInside = new Queue<>();
        range(root, CONTAINER, rect, pointInside);

        return pointInside;
    }

    private void range(KdTreeNode node, RectHV container, RectHV rect, Queue<Point2D> pointInside) {
        if (node == null) { return; }

        // if the query rectangle does not intersect the rectangle corresponding
        // to a node, there is no need to explore that node (or its subtrees).
        if (container.intersects(rect)) {
            Point2D p = node.point;
            if (rect.contains(p)) {
                pointInside.enqueue(p);
            }

            // explore subtrees
            if (node.isVertical) {
                range(node.left, leftRect(container, p), rect, pointInside);
                range(node.right, rightRect(container, p), rect, pointInside);
            }
            else {
                range(node.left, lowerRect(container, p), rect, pointInside);
                range(node.right, upperRect(container, p), rect, pointInside);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        checkNullInput(p);
        return nearest(root, CONTAINER, p, null);
    }

    // if the current nearestPoint is closer than the distance
    // between the query point and the rectangle corresponding to a node,
    // there is no need to explore that node (or its subtrees)
    private Point2D nearest(KdTreeNode node, RectHV rect, Point2D p, Point2D candidate) {
        if (node == null) { return candidate; }  //  no more points, return

        double rectToP = 0;  // distance from rectangle of the node to p
        double nToP = 0;  // distance from current nearest node to p

        Point2D nearest = candidate;
        if (nearest != null) {  // have a candidate, update distances
            rectToP = rect.distanceSquaredTo(p);
            nToP = nearest.distanceSquaredTo(p);
        }

        // do recursive call if current container rect is nearer than node
        if (nearest == null || nToP > rectToP) {
            double nodeToP = node.point.distanceSquaredTo(p);
            // no candidate or current node is nearer than candidate, update node as candidate
            if (nearest == null || nToP > nodeToP) {
                nearest = node.point;  // update candidate
            }

            // recursive call
            if (node.isVertical) {
                if (p.x() < node.point.x()) {
                    // find in leftRect first
                    nearest = nearest(node.left, leftRect(rect, node.point), p, nearest);
                    nearest = nearest(node.right, rightRect(rect, node.point), p, nearest);
                }
                else {
                    nearest = nearest(node.right, rightRect(rect, node.point), p, nearest);
                    nearest = nearest(node.left, leftRect(rect, node.point), p, nearest);
                }
            }
            else {
                if (p.y() < node.point.y()) {
                    // find in lowerRect first
                    nearest = nearest(node.left, lowerRect(rect, node.point), p, nearest);
                    nearest = nearest(node.right, upperRect(rect, node.point), p, nearest);
                }
                else {
                    nearest = nearest(node.right, upperRect(rect, node.point), p, nearest);
                    nearest = nearest(node.left, lowerRect(rect, node.point), p, nearest);
                }
            }
        }
        return nearest;
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        tree.insert(new Point2D(0.7, 0.2));
        tree.insert(new Point2D(0.5, 0.4));
        tree.insert(new Point2D(0.2, 0.3));
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));


        Point2D p = new Point2D(0.669, 0.975);

        StdOut.println(tree.nearest(p));

        /*
        Iterable<Point2D> range = tree.range(rect);

        for (Point2D r: range) {
            StdOut.println(r);
        }

         */
    }
}
