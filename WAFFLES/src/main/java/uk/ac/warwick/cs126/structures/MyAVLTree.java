package uk.ac.warwick.cs126.structures;

import java.util.function.Function;

public class MyAVLTree<K extends Comparable<K>,V> implements IAVLTree<K,V> {
    // A specific kind of AVLTree where the Key can be obtained from the Value.

    private final Node<V> root;
    private final Function<V, K> keyMethod;

    public MyAVLTree(Node<V> root, Function<V,K> keyMethod) {
        this.root = root;
        this.keyMethod = keyMethod;
    }

    public void insert(V val) { // Doesn't accept identical Keys.
        K k1 = keyMethod.apply(val);
        Node<V> node = new Node<>(val);
        Node<V> ptr = root;
        boolean added = false;
        while (!added) {
            int a = k1.compareTo(keyMethod.apply(ptr.getVal()));
            if (a > 0){
                if (ptr.getRight() == null) {
                    ptr.setRight(node);
                    added=true;
                }
                else {
                    ptr = ptr.getRight();
                }
            }
            else {
                if (ptr.getLeft() == null) {
                    ptr.setLeft(node);
                    added=true;
                }
                else{
                    ptr = ptr.getLeft();
                }
            }
        }
        triRes(ptr); //TODO: Check if this is meant to happen like this.
    }

    public V remove(K key) {
        return null;
    }

    public void remove(V val) {

    }

    public void triRes(Node<V> a) {

    }

    public V search(K key) {
        return null;
    }

    public MyArrayList<V> preorder() {
        return null;
    }

    public MyArrayList<V> inorder() {
        return null;
    }

    public MyArrayList<V> postorder() {
        return null;
    }
}
