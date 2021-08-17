/* *****************************************************************************
 *  Name: J
 *  Date: 2021-08-16
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node head;
    private Node tail;
    private int size;

    private class Node {
        Item item;
        Node prev;
        Node next;
    }

    // construct an empty deque
    public Deque() {
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Invalid input.");
        }
        Node newNode = new Node();
        newNode.item = item;
        head.next.prev = newNode;
        newNode.next = head.next;
        head.next = newNode;
        newNode.prev = head;
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Invalid input.");
        }
        Node newNode = new Node();
        newNode.item = item;
        tail.prev.next = newNode;
        newNode.prev = tail.prev;
        tail.prev = newNode;
        newNode.next = tail;
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty.");
        }
        Item popHeadItem = head.next.item;
        head.next.next.prev = head;
        head.next = head.next.next;
        size--;
        return popHeadItem;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty.");
        }
        Item popTailItem = tail.prev.item;
        tail.prev.prev.next = tail;
        tail.prev = tail.prev.prev;
        size--;
        return popTailItem;

    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head.next;

        public boolean hasNext() { return current != null && current.item != null; }
        public void remove() { throw new UnsupportedOperationException("Remove is not supported."); }
        public Item next() {
            if (current == null || current.item == null) { throw new NoSuchElementException(); }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> d = new Deque<Integer>();
        d.addFirst(8);
        d.addFirst(6);
        d.addLast(10);
        StdOut.println("size: " + d.size());

        int a = d.removeFirst();
        StdOut.println(a);
        StdOut.println("size: " + d.size());
        a = d.removeLast();
        StdOut.println(a);
        StdOut.println("size: " + d.size());
        a = d.removeLast();
        StdOut.println(a);
        StdOut.println("size: " + d.size());

        d.addFirst(2);
        d.addLast(4);
        d.addLast(6);
        d.addLast(8);
        d.addLast(14);
        StdOut.println("size: " + d.size());

        Iterator<Integer> it = d.iterator();

        try {
            it.remove();
        } catch (UnsupportedOperationException e) {
            StdOut.println(e);
        }

        while (it.hasNext()) {
            StdOut.println("has next");
            StdOut.println(it.next());
        }

        try {
            StdOut.println(it.next());
        } catch (NoSuchElementException e) {
            StdOut.println(e);
        }



    }

}