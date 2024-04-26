package project20280.list;

import project20280.interfaces.List;

import java.util.Iterator;

public class SinglyLinkedList<E> implements List<E> {

    private static class Node<E> {

        private final E element;            // reference to the element stored at this node

        /**
         * A reference to the subsequent node in the list
         */
        private Node<E> next;         // reference to the subsequent node in the list

        /**
         * Creates a node with the given element and next node.
         *
         * @param e the element to be stored
         * @param n reference to a node that should follow the new node
         */
        public Node(E e, Node<E> n) {
            element = e;
            next = n;
        }

        // Accessor methods

        /**
         * Returns the element stored at the node.
         *
         * @return the element stored at the node
         */
        public E getElement() {
            return element;
        }

        /**
         * Returns the node that follows this one (or null if no such node).
         *
         * @return the following node
         */
        public Node<E> getNext() {
            return next;
        }

        // Modifier methods

        /**
         * Sets the node's next reference to point to Node n.
         *
         * @param n the node that should follow this one
         */
        public void setNext(Node<E> n) {
            next = n;
        }
    } //----------- end of nested Node class -----------

    /**
     * The head node of the list
     */
    private Node<E> head = null;               // head node of the list (or null if empty)


    /**
     * Number of nodes in the list
     */
    private int size = 0;                      // number of nodes in the list

    public SinglyLinkedList() {
    }              // constructs an initially empty list

    //@Override
    public int size() {
        return size;
    }

    //@Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public E get(int position) {
        Node<E> n = head;
        for(int i = 0; i<position; i++){
            n = n.getNext();
        }
        return n.getElement();
    }

    @Override
    public void add(int position, E e) {
        Node<E> n = new Node<E>(e,null);
        if(position==0){
            addFirst(e);
        }
        else{
            Node<E> newNode = head;
            for(int i=0; i<position-1; i++){
                newNode = newNode.getNext();
            }
            n.next = newNode.getNext();
            newNode.next = n;

        }
        // TODO
    }


    @Override
    public void addFirst(E e) {
        head = new Node<E>(e, head);
        size++;
    }

    @Override
    public void addLast(E e) {
        Node<E> newest = new Node<E>(e, null);
        Node<E> last = head;
        if(last == null) {
            head = newest;
        }
        else {
            while (last.getNext() != null) {
                last = last.getNext();
            }
            last.setNext(newest);
        }
        size++;
        // TODO
    }

    @Override
    public E remove(int position) {
        if(position==0){
            Node<E> n1 = head;
            head=head.getNext();
            size--;
            return n1.getElement();
        }
        else{
            Node<E> n = head;
            Node<E> n1 = null;
            for(int i = 0; i < position - 1; i++){
                n = n.getNext();
            }
            n1 = n.getNext();
            n.next=n1.getNext();
            size--;
            return n1.getElement();
        }

    }

    @Override
    public E removeFirst() {
        if(size==0){
            return null;
        }
        Node<E> n1 = head;
        head=head.getNext();
        size--;
        return n1.getElement();
    }

    @Override
    public E removeLast() {
        Node<E> n = head;
        Node<E> n1 = null;
        if(size==1){
            size--;
            head=null;
            return n.getElement();
        }
        for(int i = 0; i < size - 2; i++){
            n = n.getNext();
        }
        n1 = n.getNext();
        n.next=n1.getNext();
        size--;
        return n1.getElement();
    }

    //@Override
    public Iterator<E> iterator() {
        return new SinglyLinkedListIterator<E>();
    }

    private class SinglyLinkedListIterator<E> implements Iterator<E> {
        Node<E> curr = (Node<E>) head;

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public E next() {
            E res = curr.getElement();
            curr = curr.next;
            return res;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> curr = head;
        while (curr != null) {
            sb.append(curr.getElement());
            if (curr.getNext() != null)
                sb.append(", ");
            curr = curr.getNext();
        }
        sb.append("]");
        return sb.toString();
    }


    public static void main(String[] args) {
        SinglyLinkedList<Integer> ll = new SinglyLinkedList<Integer>();
        System.out.println("ll " + ll + " isEmpty: " + ll.isEmpty());
        //LinkedList<Integer> ll = new LinkedList<Integer>();

        ll.addFirst(0);
        ll.addFirst(1);
        ll.addFirst(2);
        ll.addFirst(3);
        ll.addFirst(4);
        ll.addLast(-1);
        //ll.removeLast();
        //ll.removeFirst();
        //System.out.println("I accept your apology");
        //ll.add(3, 2);
        System.out.println(ll);
        ll.remove(5);
        System.out.println(ll);

//        for(Integer i : ll) {
//            System.out.println(i);
//        }
        /*
        ll.addFirst(-100);
        ll.addLast(+100);
        System.out.println(ll);

        ll.removeFirst();
        ll.removeLast();
        System.out.println(ll);

        // Removes the item in the specified index
        ll.remove(2);
        System.out.println(ll);

         */
    }
}
