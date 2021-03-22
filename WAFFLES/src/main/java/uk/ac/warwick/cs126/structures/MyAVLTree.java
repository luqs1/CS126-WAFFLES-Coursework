package uk.ac.warwick.cs126.structures;

import java.util.function.Function;

public class MyAVLTree<K extends Comparable<K>,V> implements IAVLTree<K,V> {
    // A specific kind of AVLTree where the Key can be obtained from the Value.

    private Node<V> root;
    private int size;
    private final Function<V, K> keyMethod;

    public static void main(String[] args) {
        // Test
        MyAVLTree<Integer, String> strings = new MyAVLTree<>(String::length);
        strings.insert("Sam");
        strings.insert("Anton");
        strings.insert("Elizabeth");
        strings.remove(5);
        strings.insert("Alice");
        strings.insert("Luqmaan");
        strings.remove(3);
        System.out.println(strings.search("Sam"));

        System.out.println(strings);
    }

    public String toString() {
        return root + "\n size=" + size +
                '}';
    }

    public MyAVLTree(Node<V> root, Function<V,K> keyMethod) {
        this.size = 0;
        this.root = root;
        this.keyMethod = keyMethod;
        if (root != null) {
            size++;
            this.root.setParent(null);
        }
    }

    public MyAVLTree(Function<V,K> keyMethod) {
        this(null, keyMethod);
    }

    private Node<V> setRoot(Node<V> node) {
        node.setParent(null);
        root = node;
        return root;
    }

    public boolean insert(V val) { // Doesn't accept identical Keys.
        K k1 = keyMethod.apply(val);
        Node<V> node = new Node<>(val);
        Node<V> ptr = root;

        if (ptr == null) { // Putting in the first node.
            setRoot(node);
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
                ptr = ptr.getRight();
            }
            else {
                if (ptr.getLeft() == null) {
                    ptr.setLeft(node);
                    added=true;
                }
                ptr = ptr.getLeft();
            }
        }
        size++;
        triRes(ptr); //TODO: TEST.
        return true;
    }

    private void removeRes(Node<V> ptr) {
        while (ptr.balanced() || ptr.getHeight() < 3) {
            if (ptr.getParent() == null) {
                return;
            }
            ptr = ptr.getParent();
        }
        Node<V> child;
        if (ptr.getRight() == null)
            child = ptr.getLeft();
        else if (ptr.getLeft() == null)
            child = ptr.getRight();
        else if (ptr.getRight().getHeight() > ptr.getLeft().getHeight())
            child = ptr.getRight();
        else
            child = ptr.getLeft();

        Node<V> grandchild;
        if (child.getRight() == null)
            grandchild = child.getLeft();
        else if (child.getLeft() == null)
            grandchild = child.getRight();
        else if (child.getRight().getHeight() > child.getLeft().getHeight())
            grandchild = ptr.getRight();
        else
            grandchild = ptr.getLeft();

        // Going to believe by induction that grandchild or child cant be null cus unbalanced.

        triRes(ptr,child,grandchild);



    }

    public V remove(K key) {
        Node<V> ptr = root;
        boolean found = false;
        while (!found) {
            int a = key.compareTo(keyMethod.apply(ptr.getVal()));
            if (a == 0) {
                found = true;
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
        V val = ptr.getVal();
        if (ptr.isExternal())
            ptr.deleteNode();
        else if (ptr.getRight() == null) {
            if (ptr.getParent() == null)

                ptr = setRoot(ptr.getLeft());
            else
                ptr.replaceWith(ptr.getLeft());
        }
        else if (ptr.getLeft() == null) {
            if (ptr.getParent() == null)
                ptr = setRoot(ptr.getRight());
            else
                ptr.replaceWith(ptr.getRight());
        }
        else {
            Node<V> min = ptr.getRight();
            while (min.getLeft() != null)
                min = min.getLeft();
            ptr.setVal(min.getVal());
            if (min.getRight() != null)
                min.replaceWith(min.getRight());
            else
                min.deleteNode();
        }
        removeRes(ptr); //TODO: Check if this is it.
        size--;
        return val;

    }

    public boolean remove(V val) {
        return remove(keyMethod.apply(val)) != null;
    }

    public int getSize() {
        return size;
    }

    private void triRes(Node<V> grand, Node<V> parent, Node<V> child) {
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
            if (grand.getParent() == null)
                setRoot(parent);
            else
                grand.replaceWith(parent);
            if (comp1) {
                grand.setLeft(parent.getRight());
                parent.setRight(grand);
            }
            else {
                grand.setRight(parent.getLeft());
                parent.setLeft(grand);
            }
            triRes(parent);
        }
        else {
            if (grand.getParent() == null)
                setRoot(child);
            else
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
            triRes(child);
        }
    }

    public void triRes(Node<V> child) {
        Node<V> parent = child.getParent();
        if (parent == null)
            return;
        Node<V> grand = parent.getParent();
        if (grand == null)
            return;
        triRes(grand,parent,child);
    }

    public V search(K key) {
        Node<V> ptr = root;
        if (ptr == null)
            return null;
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

    public boolean search(V value) {
        V a = search(keyMethod.apply(value));
        return a != null && a == value;
    }

    public Object[] preorder() { //TODO: Will implement if needed.
        return preorder(root, new MyArrayList<>()).getArray();
    }

    private MyArrayList<V> preorder(Node<V> node, MyArrayList<V> list) {
        list.add(node.getVal());
        if (node.getLeft() != null)
            preorder(node.getLeft(), list);
        if (node.getRight() != null)
            preorder(node.getRight(), list);
        return list;
    }

    public Object[] inorder() {
        return inorder(root, new MyArrayList<>()).getArray();
    }

    private MyArrayList<V> inorder(Node<V> node, MyArrayList<V> list) {
        if (node.getLeft() != null)
            inorder(node.getLeft(), list);
        list.add(node.getVal());
        if (node.getRight() != null)
            inorder(node.getRight(), list);

        return list;
    }

    public Object[] postorder() {
        return postorder(root, new MyArrayList<>()).getArray();
    }

    private MyArrayList<V> postorder(Node<V> node, MyArrayList<V> list) {
        if (node.getLeft() != null)
            postorder(node.getLeft(), list);
        if (node.getRight() != null)
            postorder(node.getRight(), list);
        list.add(node.getVal());

        return list;
    }
}
