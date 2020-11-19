/* *****************************************************************************
 *  Name:              Alan Turing
 *  Coursera User ID:  123456
 *  Last modified:     1/1/2019
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private int n;

    // construct an empty randomized queue
    public RandomizedQueue() {
        this.array = (Item[]) new Object[1];
        this.n = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return n == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return this.n;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (n == array.length) resize(2 * array.length);
        array[n++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int random = StdRandom.uniform(n);
        Item item = array[random];
        // put last item of array into the current index
        array[random] = array[--n];
        array[n] = null;
        if (n > 0 && n == array.length / 4) resize(array.length / 2);
        return item;
    }

    private void resize(int length) {
        Item[] copy = (Item[]) new Object[length];
        for (int i = 0; i < n; i++) {
            copy[i] = this.array[i];
        }
        this.array = copy;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int random = StdRandom.uniform(n);
        return array[random];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private final RandomizedQueue<Item> copy;

        RandomizedQueueIterator() {
            copy = new RandomizedQueue<Item>();

            for (int i = 0; i < n; i++) {
                copy.enqueue(array[i]);
            }
        }

        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            return copy.dequeue();
        }

        public boolean hasNext() {
            return copy.n > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> rq = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++) {
            rq.enqueue(i);
        }

        StdOut.println("Testing Iterator");
        for (int i : rq) {
            StdOut.println(i);
        }

        StdOut.println("Testing Sample Method");
        StdOut.println(rq.sample());

    }

}
