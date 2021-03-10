package uk.ac.warwick.cs126.structures;

public class LLElement<E> {
    E val;
    LLElement<E> next;

    public LLElement(E val, LLElement<E> next) {
        this.val = val;
        this.next = next;
    }

    public LLElement(E val) {
        this(val, null);
    }

    public LLElement() {
        this(null, null);
    }

    public void setVal(E val){
        this.val = val;
    }

    public void setNext(LLElement<E> next){
        this.next = next;
    }

    public E getVal(){
        return val;
    }

    public LLElement<E> getNext(){
        return next;
    }

    public String toString() {
        return val.toString();
    }
}
