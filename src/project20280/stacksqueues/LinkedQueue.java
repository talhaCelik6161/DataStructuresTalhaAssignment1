package project20280.stacksqueues;

import org.junit.platform.engine.support.hierarchical.Node;
import project20280.interfaces.Queue;
import project20280.list.DoublyLinkedList;

public class LinkedQueue<E> implements Queue<E> {

    private E front = null;
    private int size = 0;
    private E rear = null;

    DoublyLinkedList<E> ll = new DoublyLinkedList<E>();

    public static void main(String[] args) {
    }

    public LinkedQueue() {
        // TODO
    }

    @Override
    public int size() {
        return ll.size();
    }

    @Override
    public boolean isEmpty() {
        return ll.isEmpty();
    }

    @Override
    public void enqueue(E e) {
        ll.addLast(e);
        size++;
        // TODO
    }

    @Override
    public E first() {
        // TODO
        return ll.first();
    }

    @Override
    public E dequeue() {
        // TODO
        if (isEmpty()) {
            return null;
        }
        E e = ll.remove(((Integer) ll.first() )- 1);
        size--;
        return e;
    }

    public String toString() {
        return ll.toString();
    }
}
