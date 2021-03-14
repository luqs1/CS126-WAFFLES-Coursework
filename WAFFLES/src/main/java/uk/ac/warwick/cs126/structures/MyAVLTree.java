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

    public boolean insert(V val) { // Doesn't accept identical Keys.
        K k1 = keyMethod.apply(val);
        Node<V> node = new Node<>(val);
        Node<V> ptr = root;
        boolean added = false;
        while (!added) {
            int a = k1.compareTo(keyMethod.apply(ptr.getVal()));
            if (a == 0) // The key is already in use.
                return false;
            else if (a > 0){
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
        triRes(ptr); //TODO: TEST.
        return true;
    }

    public V remove(K key) { //TODO: Implement.
        return null;
    }

    public boolean remove(V val) {
        return remove(keyMethod.apply(val)) != null;
    }

    public void triRes(Node<V> grand, Node<V> parent, Node<V> child) {
        K k1 = keyMethod.apply(grand.getVal());
        K k2 = keyMethod.apply(parent.getVal());
        K k3 = keyMethod.apply(child.getVal());

        boolean comp1 = k1.compareTo(k2) > 0;
        boolean comp2 = k2.compareTo(k3) > 0;
        //TODO: TEST.

        if (grand.balanced())
            return;

        /* comp1 tells us if we're doing a L first or a R first. This is the basis
           for a lot of reflection.
           comp1 == comp2 tells us if we're going to need a single or double
           rotation.
        */

        if (comp1 == comp2) {
            grand.replaceWith(parent);
            if (comp1) {
                grand.setLeft(parent.getRight());
                parent.setRight(grand);
            }
            else {
                grand.setRight(parent.getLeft());
                parent.setLeft(grand);
            }
        }
        else {
            grand.replaceWith(child);
            if (comp1) {
                parent.setRight(child.getLeft());
                grand.setLeft(child.getRight());
                child.setLeft(parent);
                child.setRight(grand);
            }
            else {
                parent.setLeft(child.getRight());
                grand.setRight(child.getLeft());
                child.setRight(parent);
                child.setLeft(grand);
            }

        }
        triRes(parent);
    }

    public void triRes(Node<V> child) {
        Node<V> parent = child.getParent();
        Node<V> grand = parent.getParent();
        triRes(grand,parent,child);
    }

    public V search(K key) {
        Node<V> ptr = root;
        while (true) {
            int a = key.compareTo(keyMethod.apply(ptr.getVal()));
            if (a == 0) // The key is already in use.
                return ptr.getVal();
            else if (a > 0){
                if (ptr.getRight() == null)
                    return null;
                else
                    ptr = ptr.getRight();
            }
            else {
                if (ptr.getLeft() == null) {
                   return null;
                }
                else
                    ptr = ptr.getLeft();
            }
        }
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
