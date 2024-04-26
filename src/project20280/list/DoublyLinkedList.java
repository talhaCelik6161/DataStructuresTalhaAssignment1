package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class DoublyLinkedList<E> implements List<E> {

    private static class Node<E> {
        private final E data;

        public void setNext(Node<E> next) {
            this.next = next;
        }

        private Node<E> next;

        public void setPrev(Node<E> prev) {
            this.prev = prev;
        }

        private  Node<E> prev;

        public Node(E e, Node<E> p, Node<E> n) {
            data = e;
            prev = p;
            next = n;
        }

        public E getData() {
            return data;
        }

        public Node<E> getNext() {
            return next;
        }

        public Node<E> getPrev() {
            return prev;
        }

    }

    private Node<E> head;
    private final Node<E> tail;
    private int size = 0;

    public DoublyLinkedList() {
        head = new Node<E>(null, null, null);
        tail = new Node<E>(null, head, null);
        head.next = tail;
    }

    private void addBetween(E e, Node<E> pred, Node<E> succ) {
        Node<E> newest = new Node<E>(e,pred,succ);
        pred.setNext(newest);
        succ.setPrev(newest);
        size++;
        // TODO
    }

    @Override
    public int size() {
        // TODO
        return size;
    }

    @Override
    public boolean isEmpty() {
        // TODO
        return size==0;
    }

    @Override
    public E get(int i) {
        //TODO
        Node<E> n = head;
        for(int a = 0; a<i+1; a++){
            n = n.getNext();
        }
        return n.getData();
    }

    @Override
    public void add(int i, E e) {
        Node<E> n = new Node<E>(e,null,null);
        if(i==0){
            addFirst(e);
        }
        else{
            Node<E> newNode = head;
            for(int a=0; a<i; a++){
                newNode = newNode.getNext();
            }
            n.next = newNode.getNext();
            n.prev = newNode;
            newNode.next = n;

        }
        // TODO
    }

    @Override
    public E remove(int i) {
        // TODO
        if(i==0){
            Node<E> n1 = head;
            head=head.getNext();
            size--;
            return n1.getData();
        }
        else{
            Node<E> n = head;
            Node<E> n1 = null;
            for(int a = 0; a < i; a++){
                n = n.getNext();
            }
            n1 = n.getNext();
            n.next=n1.getNext();
            size--;
            return n1.getData();
        }
    }

    private class DoublyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head.next;

        @Override
        public boolean hasNext() {
            return curr != tail;
        }

        @Override
        public E next() {
            E res = curr.data;
            curr = curr.next;
            return res;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new DoublyLinkedListIterator<E>();
    }

    private E remove(Node<E> n) {
        // TODO
        return null;
    }

    public E first() {
        if (isEmpty()) {
            return null;
        }
        return head.next.getData();
    }

    public E last() {
        // TODO
        return tail.getPrev().getData();
    }

    @Override
    public E removeFirst() {
        // TODO
        if(size==0){
            return null;
        }
        Node<E> n1 = head;
        head=head.getNext();
        size--;
        if(size == 0) { // If the list becomes empty after removing the first element
            tail.setPrev(null); // Update tail to null
        }
        return n1.getData();

    }

    @Override
    public E removeLast() {
        // TODO
        Node<E> n = head;
        Node<E> n1 = null;
        if(size==1){
            size--;
            head=null;
            return n.getData();
        }
        for(int i = 0; i < size - 1; i++){
            n = n.getNext();
        }
        n1 = n.getNext();
        n.next=n1.getNext();
        size--;
        return n1.getData();
    }

    @Override
    public void addLast(E e) {
        addBetween(e,tail.getPrev(),tail);
        // TODO
    }

    @Override
    public void addFirst(E e) {
        addBetween(e,head,head.getNext());
        // TODO
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head.next;
        while (curr != tail) {
            sb.append(curr.data);
            curr = curr.next;
            if (curr != tail) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public static void main(String[] args) {
        DoublyLinkedList<Integer> ll = new DoublyLinkedList<Integer>();
        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addLast(-1);
        System.out.println(ll);

        ll.removeFirst();
        System.out.println(ll);

        ll.removeLast();
        System.out.println(ll);

        for (Integer e : ll) {
            System.out.println("value: " + e);
        }
    }
}