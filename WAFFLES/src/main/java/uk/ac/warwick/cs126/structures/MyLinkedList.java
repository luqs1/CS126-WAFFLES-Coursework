package uk.ac.warwick.cs126.structures;

public class MyLinkedList<E> {

    LLElement<E> head;
    LLElement<E> tail;
    int count;

    public MyLinkedList() {
        this.head = null;
        this.tail = null;
        this.count = 0;
    }

    public boolean isEmpty() {
        // Returns whether the list is empty.
        return (head == null);
    }

    public boolean add(E element) {
        // Adds an element to the head of the list.
        LLElement<E> temp = new LLElement<>(element);

        // if the list is not empty, point the new link to head
        if (!isEmpty()) {
            temp.setNext(head);
        } else {
            tail = temp;
        }
        // update the head
        head = temp;
        count++;
        return true;
    }

    public int size() {
        // Returns the number of elements in stored in this list.
        return count;
    }

    public String toString() {
        // Returns a string representation of this list.
        StringBuilder str = new StringBuilder("[");
        LLElement<E> curr = head;
        while (curr != null) {
            str.append(curr.toString());
            curr = curr.getNext();
            if (curr != null)
                str.append(", ");
        }
        return str + "]";
    }

    public boolean addToTail(E element) {
        // Adds element to tail of the list
        LLElement<E> toAdd = new LLElement<>(element);
        if (size() == 0) {
            add(element);
            return true;
        }
        tail.setNext(toAdd);
        tail = toAdd;
        count++;
        return true;
    }

    public E removeFromHead() {
        // Removes and returns the head element
        if (size() == 0)
            return null;
        if (size() == 1) {
            tail = null;
        }
        E val = head.getVal();
        head = head.getNext();
        count--;
        return val;
    }

    public E removeFromTail() {
        // Removes and returns the tail element
        if (size() == 0)
            return null;
        if (size() == 1) {
            return removeFromHead();
        }
        LLElement<E> curr = head;
        while (curr.getNext() != tail) {
            curr = curr.getNext();
        }
        E val = tail.getVal();
        tail = curr;
        tail.setNext(null);
        count--;
        return val;

    }


    public E get(int index) {
        // Gets the element at index in the list
        LLElement<E> ptr = head;
        for (int i = size() - 1; i > index; i--) {
            ptr = ptr.getNext();
        }
        return ptr.getVal();
    }

    public int indexOf(E element) {
        // Gets the index of element in the list
        LLElement<E> ptr = head;
        int i = 0;
        while (ptr != null) {
            if (element.equals(ptr.getVal())) {
                return i;
            }
            i++;
            ptr = ptr.getNext();
        }
        return -1;
    }

    public E set(int index, E element) {
        // Sets element at index in the list
        LLElement<E> ptr = head;
        for (int i = 0; i < index; i++) {
            ptr = ptr.getNext();
        }
        E ret = ptr.getNext().getVal();
        LLElement<E> newlink = new LLElement<>(element);
        newlink.setNext(ptr.getNext().getNext());
        ptr.setNext(newlink);
        return ret;
    }

    public void clear() {
        // Clears the list
        head = null;
    }

    public boolean contains(E element) {
        // Returns whether the element exists in the list
        return indexOf(element) != -1;
    }

    public boolean remove(E element) {
        // Removes element from the list
        if (isEmpty()) return false;
        LLElement<E> ptr = head;
        while (ptr.getNext().getNext() != null) {
            if (element.equals(ptr.getNext().getVal())) {
                ptr.setNext(ptr.getNext().getNext());
                count--;
                return true;
            }
            ptr = ptr.getNext();
        }
        if (element.equals(ptr.getNext().getVal())) {
            ptr.setNext(null);
            count--;
            return true;
        }
        return false;
    }


}