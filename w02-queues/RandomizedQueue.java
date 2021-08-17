/* *****************************************************************************
 *  Name: J
 *  Date: 2021-08-16
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int size;
    private Item[] q;

    // construct an empty randomized queue
    public RandomizedQueue() {
        size = 0;
        q = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return size;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Invalid input.");
        }

        if (size == q.length) {
            resize(2 * q.length);
        }
        q[size++] = item;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            copy[i] = q[i];
        }
        q = copy;
    }

    // remove and return a random item
    public Item dequeue() {
        if (size() == 0) {
            throw new NoSuchElementException("Empty queue.");
        }
        int popIndex = StdRandom.uniform(size);
        Item popItem = q[popIndex];
        if (popIndex != size - 1) {
            q[popIndex] = q[size - 1];
        }
        q[size - 1] = null;
        size--;

        if (size > 0 && size == q.length / 4) {
            resize(q.length / 2);
        }
        return popItem;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (size() == 0) {
            throw new NoSuchElementException("Empty queue.");
        }
        int sampleIndex = StdRandom.uniform(size);
        return q[sampleIndex];

    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new QueueIterator();
    }

    private class QueueIterator implements Iterator<Item> {
        private int currentIndex = 0;
        private Item[] copyQ;

        private QueueIterator() {
            copyQ = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                copyQ[i] = q[i];
            }
            for (int i = size - 1; i > 0; i--) {
                int randomIndex = StdRandom.uniform(i + 1);
                Item swap = copyQ[randomIndex];
                copyQ[randomIndex] = copyQ[i];
                copyQ[i] = swap;
            }
        }

        public boolean hasNext() { return currentIndex < size; }
        public void remove() { throw new UnsupportedOperationException("Remove is not supported."); }
        public Item next() {
            if (currentIndex >= size) {
                throw new NoSuchElementException("No elements");
            }
            Item i = copyQ[currentIndex];
            currentIndex++;
            return i;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        rq.enqueue(1);
        rq.enqueue(3);
        rq.enqueue(5);
        rq.enqueue(7);
        rq.enqueue(9);

        StdOut.println(rq.size());

        StdOut.println("sample: " + rq.sample());
        StdOut.println(rq.size());

        StdOut.println("dequeue: " + rq.dequeue());
        StdOut.println(rq.size());

        Iterator<Integer> it = rq.iterator();
        Iterator<Integer> it2 = rq.iterator();
        while (it.hasNext()) {
            StdOut.println("Itering 1: " + it.next());
            StdOut.println("Itering 2: " + it2.next());
        }

        StdOut.println(rq.size());

    }

}
