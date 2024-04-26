package project20280.stacksqueues;

import project20280.interfaces.Stack;
import project20280.list.DoublyLinkedList;

public class LinkedStack<E> implements Stack<E> {
    DoublyLinkedList<E> ll = new DoublyLinkedList<E>();
    private int size = 0;

    public static void main(String[] args) {
    }

    public LinkedStack() {
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
    public void push(E e) {
        ll.addFirst(e);
        size++;
        // TODO
    }

    @Override
    public E top() {
        // TODO
        return ll.first();
    }

    @Override
    public E pop() {
        // TODO
        if (isEmpty()) {
            return null;
        }
        E e = ll.remove(((Integer) ll.last() )- 1);
        size--;
        return e;
    }

    public String toString() {
        return ll.toString();
    }
}
