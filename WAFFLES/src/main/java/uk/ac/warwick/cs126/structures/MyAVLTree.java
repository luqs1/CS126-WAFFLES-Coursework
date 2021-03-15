package uk.ac.warwick.cs126.structures;

import java.util.function.Function;

public class MyAVLTree<K extends Comparable<K>,V> implements IAVLTree<K,V> {
    // A specific kind of AVLTree where the Key can be obtained from the Value.

    private Node<V> root;
    private int size;
    private final Function<V, K> keyMethod;

    public MyAVLTree(Node<V> root, Function<V,K> keyMethod) {
        this.size = 0;
        this.root = root;
        this.keyMethod = keyMethod;
        if (root != null)
            size++;
    }

    public boolean insert(V val) { // Doesn't accept identical Keys.
        K k1 = keyMethod.apply(val);
        Node<V> node = new Node<>(val);
        Node<V> ptr = root;

        if (ptr == null) { // Putting in the first node.
            root = node;
            size++;
            return true;
        }
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
        size++;
        triRes(ptr); //TODO: TEST.
        return true;
    }

    public void removeRes(Node<V> ptr) {
        while (ptr.balanced()) {
            if (ptr.getParent() == null) {
                return;
            }
            ptr = ptr.getParent();
        }
        Node<V> child;
        if (ptr.getRight().getHeight() > ptr.getLeft().getHeight())
            child = ptr.getRight();
        else
            child = ptr.getLeft();
        Node<V> grandchild;
        if (child.getRight().getHeight() > child.getLeft().getHeight())
            grandchild = ptr.getRight();
        else
            grandchild = ptr.getLeft();

        // Going to believe by induction that grandchild or child cant be null cus unbalanced.

        triRes(ptr,child,grandchild);



    }

    public V remove(K key) {
        Node<V> ptr = root;
        while (true) {
            int a = key.compareTo(keyMethod.apply(ptr.getVal()));
            if (a == 0) {
                if (ptr.getParent() == null) {
                    root = null;
                    size--;
                    return ptr.getVal();
                }
                Node<V> parent = ptr.getParent();
                if (parent.getLeft().equals(ptr))
                    parent.setLeft(null);
                else
                    parent.setRight(null);
                size--;
                removeRes(ptr);
                return ptr.getVal();
            }
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

    public boolean remove(V val) {
        return remove(keyMethod.apply(val)) != null;
    }

    public void triRes(Node<V> grand, Node<V> parent, Node<V> child) {
        if (grand.balanced()) // Important because it calls itself.
            return;

        K k1 = keyMethod.apply(grand.getVal());
        K k2 = keyMethod.apply(parent.getVal());
        K k3 = keyMethod.apply(child.getVal());

        boolean comp1 = k1.compareTo(k2) > 0;
        boolean comp2 = k2.compareTo(k3) > 0;
        //TODO: TEST.

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

    public V[] preorder() { //TODO: Will implement if needed.
        return null;
    }

    @SuppressWarnings("unchecked")
    public V[] inorder() {
        return (V[]) inorder(root, new MyArrayList<>()).getArray();
    }

    private MyArrayList<V> inorder(Node<V> node, MyArrayList<V> list) {
        if (node.getLeft() != null)
            list = inorder(node.getLeft(), list);
        list.add(node.getVal());
        if (node.getRight() != null)
            list = inorder(node.getRight(), list);

        return list;
    }

    public V[] postorder() { //TODO: Will implement if needed.
        return null;
    }
}
